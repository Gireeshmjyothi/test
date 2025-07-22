 private Map<String, Dataset<Row>> findMatchedUnmatchedAndDuplicateReconRecords(Dataset<Row> transactionDataset, Dataset<Row> reconFileDataset, UUID reconFileId) {
        logger.info("Starting classification of recon data...");
        final String reconFieldFormat = "recon.%s";
        final String txnFieldFormat = "txn.%s";
        // Step 3.1: Apply aliasing
        Dataset<Row> recon = reconFileDataset.alias("recon");
        Dataset<Row> txn = transactionDataset.alias("txn");

        // Step 3.1.1: Apply row_number to tag first match per (ATRN_NUM, DEBIT_AMT, RFD_ID)
        logger.info("Step-3.1: Apply row_number to tag first match per (ATRN_NUM, DEBIT_AMT, RFD_ID)");
        WindowSpec matchWindow = Window.partitionBy(reconFieldFormat.formatted(ATRN_NUM), reconFieldFormat.formatted(DEBIT_AMT)).orderBy(reconFieldFormat.formatted(RFD_ID)); // can use any ordering

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
        // Step 3.6: Filter Dataset for the matched, unmatched, and duplicate Recon Data
        Dataset<Row> matched = addStatus(finalStatus.filter(col(RECON_STATUS).equalTo(RECON_STATUS_MATCHED)), RECON_STATUS_MATCHED, SETTLEMENT_STATUS);
        Dataset<Row> unmatched = addStatus(finalStatus.filter(col(RECON_STATUS).equalTo(RECON_STATUS_UNMATCHED)), RECON_STATUS_UNMATCHED, null);
        Dataset<Row> duplicate = addStatus(finalStatus.filter(col(RECON_STATUS).equalTo(RECON_STATUS_DUPLICATE)), RECON_STATUS_DUPLICATE, null);

        //TODO-remove once tested
        matched.show(false);
        unmatched.show(false);
        duplicate.show(false);

        // Add RFS_ID column
        matched = matched.withColumn(RFS_ID, lit(String.valueOf(reconFileId)));
        unmatched = unmatched.withColumn(RFS_ID, lit(String.valueOf(reconFileId)));
        duplicate = duplicate.withColumn(RFS_ID, lit(String.valueOf(reconFileId)));

        //TODO-remove once tested
        matched.show(false);
        unmatched.show(false);
        duplicate.show(false);

        // Return both datasets or store them in a Map
        Map<String, Dataset<Row>> result = new HashMap<>();
        result.put(RECON_STATUS_MATCHED, matched.select(RFD_ID, RECON_STATUS, SETTLEMENT_STATUS_FIELD, RFS_ID));
        result.put(OTHERS, unmatched.union(duplicate).select(RFD_ID, RECON_STATUS, RFS_ID));
        return result;
    }

    /**
     * This method is used to map the reconStatus to the respective dataset.
     *
     * @param dataset dataset.
     * @param reconStatus  recon reconStatus.
     * @return dataset.
     */
    private Dataset<Row> addStatus(Dataset<Row> dataset, String reconStatus, String settlementStatus) {
        dataset.withColumn(RECON_STATUS, lit(reconStatus));
        dataset.withColumn(SETTLEMENT_STATUS_FIELD, lit(settlementStatus));
        return dataset;
    }
