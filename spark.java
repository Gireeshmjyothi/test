private Map<String, Dataset<Row>> findMatchedUnmatchedAndDuplicateReconRecords(Dataset<Row> transactionDataset, Dataset<Row> reconFileDataset, UUID reconFileId) {
        logger.info("Starting classification of recon data...");
        final String reconFieldFormat = "recon.%s";
        final String txnFieldFormat = "txn.%s";

        // Step 1: Normalize matching keys to avoid type/whitespace mismatches
        Dataset<Row> recon = reconFileDataset
                .withColumn("ROW_NUMBER", monotonically_increasing_id())
                .alias("recon");

        Dataset<Row> txn = transactionDataset.alias("txn");


        // Step 3.1.1: Apply row_number to tag first match per (ATRN_NUM, DEBIT_AMT, RFD_ID)
        logger.info("Step-3.1: Apply row_number to tag first match per (ATRN_NUM, DEBIT_AMT, RFD_ID)");
        WindowSpec matchWindow = Window.partitionBy(reconFieldFormat.formatted(ATRN_NUM), reconFieldFormat.formatted(DEBIT_AMT)).orderBy(reconFieldFormat.formatted("ROW_NUMBER")); // can use any ordering

        // Step 3.2: Perform left_outer join on ATRN_NUM + DEBIT_AMT
        logger.info("Step-3.2: Performing join between recon and transaction data.");
        Dataset<Row> joined = recon.join(txn, col(reconFieldFormat.formatted(ATRN_NUM)).equalTo(col(txnFieldFormat.formatted(ATRN_NUM))).and(col(reconFieldFormat.formatted(DEBIT_AMT)).equalTo(col(txnFieldFormat.formatted(DEBIT_AMT)))), "left_outer").withColumn(EXACT_MATCH, when(col(txnFieldFormat.formatted(ATRN_NUM)).isNotNull(), lit(1)).otherwise(lit(0))).withColumn(MATCH_RANK, when(col(EXACT_MATCH).equalTo(1), row_number().over(matchWindow)));

        // Step 3.3: Find distinct matched ATRN_NUM
        logger.info("Step-3.3: Find distinct matched ATRN_NUM.");
        Dataset<Row> matchedAtrns = joined.filter(col(MATCH_RANK).equalTo(1)).select(col(reconFieldFormat.formatted(ATRN_NUM)).alias(MATCHED_ATRN)).distinct();

        // Step 3.4: Re-join matched ATRN_NUM to mark duplicates (same ATRN_NUM, different DEBIT_AMT)
        logger.info("Step-3.4: Re-join matched ATRN_NUM to mark duplicates");
        Dataset<Row> withMatchedFlag = joined.join(matchedAtrns, col(reconFieldFormat.formatted(ATRN_NUM)).equalTo(col(MATCHED_ATRN)), "left_outer").withColumn(ATRN_MATCHED, col(MATCHED_ATRN).isNotNull());

        // Step 3.5: Apply classification logic
        logger.info("Step-3.5: Applying classification logic to tag rows with status.");
        Dataset<Row> finalStatus = withMatchedFlag.withColumn(RECON_STATUS, when(col(EXACT_MATCH).equalTo(1).and(col(MATCH_RANK).equalTo(1)), lit(RECON_STATUS_MATCHED)).when(col(EXACT_MATCH).equalTo(1).and(col(MATCH_RANK).gt(1)), lit(RECON_STATUS_DUPLICATE)).when(col(EXACT_MATCH).equalTo(0).and(col(ATRN_MATCHED).equalTo(true)), lit(RECON_STATUS_DUPLICATE)).otherwise(lit(RECON_STATUS_UNMATCHED)));

        // Step 3.6: Filter Dataset for the matched,unmatched and duplicate Recon Data
        logger.info("Step-3.6: Filter Dataset for the matched,unmatched and duplicate Recon Data");

        Dataset<Row> matched = finalStatus
                .filter(col(RECON_STATUS).equalTo(RECON_STATUS_MATCHED))
                .withColumn(RECON_STATUS, lit(RECON_STATUS_MATCHED))
                .withColumn(RF_ID, lit(reconFileId.toString().replace("-", "")))
                .withColumn("CREATED_DATE", unix_timestamp(current_timestamp()).multiply(1000))
                .withColumn("UPDATED_DATE", unix_timestamp(current_timestamp()).multiply(1000))
                .select(reconFieldFormat.formatted(ATRN_NUM), reconFieldFormat.formatted(DEBIT_AMT), RECON_STATUS, RF_ID, MERCHANT_ID, "ROW_NUMBER","CREATED_DATE", "UPDATED_DATE");

        Dataset<Row> unmatchedOrDuplicate = finalStatus
                .filter(col(RECON_STATUS).isin(RECON_STATUS_UNMATCHED, RECON_STATUS_DUPLICATE))
                .withColumn(RF_ID, lit(reconFileId.toString().replace("-", "")))
                .withColumn("CREATED_DATE", unix_timestamp(current_timestamp()).multiply(1000))
                .withColumn("UPDATED_DATE", unix_timestamp(current_timestamp()).multiply(1000))
                .select(reconFieldFormat.formatted(ATRN_NUM), reconFieldFormat.formatted(DEBIT_AMT), RECON_STATUS, RF_ID, MERCHANT_ID, "ROW_NUMBER","CREATED_DATE", "UPDATED_DATE");

        // Step 3.7: Return as Map data.
        Map<String, Dataset<Row>> result = new HashMap<>();
        result.put(RECON_STATUS_MATCHED, matched);
        result.put(OTHERS, unmatchedOrDuplicate);

        return result;
    }
