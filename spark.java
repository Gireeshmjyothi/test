private Dataset<Row> findMatchedUnmatchedAndDuplicateReconRecords(
        Dataset<Row> transactionDataset,
        Dataset<Row> reconFileDataset,
        UUID reconFileId) {

    logger.info("Starting classification of recon data...");
    final String reconFieldFormat = "recon.%s";
    final String txnFieldFormat = "txn.%s";

    // Step 1: Add ROW_NUMBER for reference
    Dataset<Row> recon = reconFileDataset
            .withColumn("ROW_NUMBER", monotonically_increasing_id())
            .alias("recon");

    Dataset<Row> txn = transactionDataset.alias("txn");

    // Step 2: Window spec for ranking matches
    WindowSpec matchWindow = Window.partitionBy(
            reconFieldFormat.formatted(ATRN_NUM),
            reconFieldFormat.formatted(DEBIT_AMT))
        .orderBy(reconFieldFormat.formatted("ROW_NUMBER"));

    // Step 3: Join recon with txn on ATRN + DEBIT_AMT
    Dataset<Row> joined = recon.join(txn,
                    col(reconFieldFormat.formatted(ATRN_NUM))
                            .equalTo(col(txnFieldFormat.formatted(ATRN_NUM)))
                            .and(col(reconFieldFormat.formatted(DEBIT_AMT))
                                    .equalTo(col(txnFieldFormat.formatted(DEBIT_AMT)))),
                    "left_outer")
            .withColumn(EXACT_MATCH,
                    when(col(txnFieldFormat.formatted(ATRN_NUM)).isNotNull(), lit(1))
                            .otherwise(lit(0)))
            .withColumn(MATCH_RANK,
                    when(col(EXACT_MATCH).equalTo(1), row_number().over(matchWindow)))
            .withColumn(MERCHANT_ID,
                    coalesce(col(txnFieldFormat.formatted(MERCHANT_ID)), lit(null)));

    // Step 4: Find matched ATRNs
    Dataset<Row> matchedAtrns = joined.filter(col(MATCH_RANK).equalTo(1))
            .select(col(reconFieldFormat.formatted(ATRN_NUM)).alias(MATCHED_ATRN))
            .distinct();

    // Step 5: Mark ATRN matched flag
    Dataset<Row> withMatchedFlag = joined.join(matchedAtrns,
                    col(reconFieldFormat.formatted(ATRN_NUM)).equalTo(col(MATCHED_ATRN)),
                    "left_outer")
            .withColumn(ATRN_MATCHED, col(MATCHED_ATRN).isNotNull());

    // Step 6: Classification + Remarks for unmatched
    Dataset<Row> finalStatus = withMatchedFlag
            .withColumn(RECON_STATUS,
                    when(col(EXACT_MATCH).equalTo(1).and(col(MATCH_RANK).equalTo(1)),
                            lit(RECON_STATUS_MATCHED))
                            .when(col(EXACT_MATCH).equalTo(1).and(col(MATCH_RANK).gt(1)),
                                    lit(RECON_STATUS_DUPLICATE))
                            .when(col(EXACT_MATCH).equalTo(0).and(col(ATRN_MATCHED).equalTo(true)),
                                    lit(RECON_STATUS_DUPLICATE))
                            .otherwise(lit(RECON_STATUS_UNMATCHED)))
            .withColumn("REMARK",
                    when(col(RECON_STATUS).equalTo(RECON_STATUS_UNMATCHED)
                                    .and(col(txnFieldFormat.formatted(ATRN_NUM)).isNull()),
                            lit("ATRN missing"))
                            .when(col(RECON_STATUS).equalTo(RECON_STATUS_UNMATCHED)
                                    .and(col(txnFieldFormat.formatted(ATRN_NUM)).isNotNull())
                                    .and(col(txnFieldFormat.formatted(DEBIT_AMT)).isNull()),
                            lit("Debit amount mismatch"))
                            .otherwise(lit(null)))
            // Ensure merchantId is populated when ATRN matches but amount mismatches
            .withColumn(MERCHANT_ID,
                    when(col(RECON_STATUS).equalTo(RECON_STATUS_UNMATCHED)
                                    .and(col("REMARK").equalTo("Debit amount mismatch")),
                            col(txnFieldFormat.formatted(MERCHANT_ID)))
                            .otherwise(col(MERCHANT_ID)))
            .withColumn(RF_ID, lit(reconFileId.toString().replace("-", "")))
            .withColumn("CREATED_DATE", unix_timestamp(current_timestamp()).multiply(1000))
            .withColumn("UPDATED_DATE", unix_timestamp(current_timestamp()).multiply(1000));

    // Step 7: Single dataset for all statuses
    Dataset<Row> finalResult = finalStatus.select(
            col(reconFieldFormat.formatted(ATRN_NUM)),
            col(reconFieldFormat.formatted(DEBIT_AMT)),
            col(RECON_STATUS),
            col("REMARK"),
            col(RF_ID),
            col(MERCHANT_ID),
            col("ROW_NUMBER"),
            col("CREATED_DATE"),
            col("UPDATED_DATE")
    );

    return finalResult;
}
