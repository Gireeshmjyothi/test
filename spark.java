private Map<String, Dataset<Row>> findMatchedUnmatchedAndDuplicateReconRecords(Dataset<Row> transactionDataset, Dataset<Row> reconFileDataset, UUID reconFileId) {
        logger.info("Starting classification of recon data...");
        final String reconFieldFormat = "recon.%s";
        final String txnFieldFormat = "txn.%s";
        // Step 3.1: Apply aliasing
        Dataset<Row> recon = reconFileDataset.alias("recon");
        Dataset<Row> txn = transactionDataset.alias("txn");

        // Step 3.1.1: Apply row_number to tag first match per (ATRN_NUM, DEBIT_AMT, RFD_ID)
        logger.info("Step-3.1: Apply row_number to tag first match per (ATRN_NUM, DEBIT_AMT, RFD_ID)");
        WindowSpec matchWindow = Window.partitionBy(reconFieldFormat.formatted(OperationsConstant.ATRN_NUM), reconFieldFormat.formatted(OperationsConstant.DEBIT_AMT)).orderBy(reconFieldFormat.formatted(OperationsConstant.RFD_ID)); // can use any ordering

        // Step 3.2: Perform left_outer join on ATRN_NUM + DEBIT_AMT
        logger.info("Step-3.2: Performing join between recon and transaction data.");
        Dataset<Row> joined = recon.join(txn, functions.col(reconFieldFormat.formatted(OperationsConstant.ATRN_NUM)).equalTo(functions.col(txnFieldFormat.formatted(OperationsConstant.ATRN_NUM))).and(functions.col(reconFieldFormat.formatted(OperationsConstant.DEBIT_AMT)).equalTo(functions.col(txnFieldFormat.formatted(OperationsConstant.DEBIT_AMT)))), "left_outer").withColumn(OperationsConstant.EXACT_MATCH, functions.when(functions.col(txnFieldFormat.formatted(OperationsConstant.ATRN_NUM)).isNotNull(), functions.lit(1)).otherwise(functions.lit(0))).withColumn(OperationsConstant.MATCH_RANK, functions.when(functions.col(OperationsConstant.EXACT_MATCH).equalTo(1), functions.row_number().over(matchWindow)));

        // Step 3.3: Find distinct matched ATRN_NUM
        logger.info("Step-3.3: Find distinct matched ATRN_NUM.");
        Dataset<Row> matchedAtrns = joined.filter(functions.col(OperationsConstant.MATCH_RANK).equalTo(1)).select(functions.col(reconFieldFormat.formatted(OperationsConstant.ATRN_NUM)).alias(OperationsConstant.MATCHED_ATRN)).distinct();

        // Step 3.4: Re-join matched ATRN_NUM to mark duplicates (same ATRN_NUM, different DEBIT_AMT)
        logger.info("Step-3.4: Re-join matched ATRN_NUM to mark duplicates");
        Dataset<Row> withMatchedFlag = joined.join(matchedAtrns, functions.col(reconFieldFormat.formatted(OperationsConstant.ATRN_NUM)).equalTo(functions.col(OperationsConstant.MATCHED_ATRN)), "left_outer").withColumn(OperationsConstant.ATRN_MATCHED, functions.col(OperationsConstant.MATCHED_ATRN).isNotNull());

        // Step 3.5: Apply classification logic
        logger.info("Step-3.5: Applying classification logic to tag rows with status.");
        Dataset<Row> finalStatus = withMatchedFlag.withColumn(OperationsConstant.RECON_STATUS, functions.when(functions.col(OperationsConstant.EXACT_MATCH).equalTo(1).and(functions.col(OperationsConstant.MATCH_RANK).equalTo(1)), functions.lit(OperationsConstant.RECON_STATUS_MATCHED)).when(functions.col(OperationsConstant.EXACT_MATCH).equalTo(1).and(functions.col(OperationsConstant.MATCH_RANK).gt(1)), functions.lit(OperationsConstant.RECON_STATUS_DUPLICATE)).when(functions.col(OperationsConstant.EXACT_MATCH).equalTo(0).and(functions.col(OperationsConstant.ATRN_MATCHED).equalTo(true)), functions.lit(OperationsConstant.RECON_STATUS_DUPLICATE)).otherwise(functions.lit(OperationsConstant.RECON_STATUS_UNMATCHED)));

        // Step 3.6: Filter Dataset for the matched,unmatched and duplicate Recon Data
        logger.info("Step-3.6: Filter Dataset for the matched,unmatched and duplicate Recon Data");
        Dataset<Row> matched = finalStatus
                .filter(functions.col(OperationsConstant.RECON_STATUS).equalTo(OperationsConstant.RECON_STATUS_MATCHED))
                .withColumn(OperationsConstant.RECON_STATUS, functions.lit(OperationsConstant.RECON_STATUS_MATCHED))
                .withColumn(OperationsConstant.SETTLEMENT_STATUS,
                        functions.when(functions.col(OperationsConstant.SETTLEMENT_STATUS).isNull()
                                        .or(functions.col(OperationsConstant.SETTLEMENT_STATUS).equalTo(Status.PENDING.name())),
                                functions.lit(OperationsConstant.SETTLEMENT_STATUS_SETTLED)))
                .withColumn(OperationsConstant.RF_ID, functions.lit(String.valueOf(reconFileId)))
                .select(OperationsConstant.RFD_ID, OperationsConstant.RECON_STATUS, OperationsConstant.SETTLEMENT_STATUS, OperationsConstant.RF_ID);

        Dataset<Row> unmatchedOrDuplicate = finalStatus
                .filter(functions.col(OperationsConstant.RECON_STATUS).isin(OperationsConstant.RECON_STATUS_UNMATCHED, OperationsConstant.RECON_STATUS_DUPLICATE))
                .withColumn(OperationsConstant.RF_ID, functions.lit(String.valueOf(reconFileId)))
                .select(OperationsConstant.RFD_ID, OperationsConstant.RECON_STATUS, OperationsConstant.RF_ID);

        // Step 3.7: Return as Map data.
        Map<String, Dataset<Row>> result = new HashMap<>();
        result.put(OperationsConstant.RECON_STATUS_MATCHED, matched);
        result.put(OperationsConstant.OTHERS, unmatchedOrDuplicate);

        return result;
    }
