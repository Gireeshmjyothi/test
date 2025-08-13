private Map<String, Dataset<Row>> findMatchedUnmatchedAndDuplicateReconRecords(
        Dataset<Row> transactionDataset,
        Dataset<Row> reconFileDataset,
        UUID reconFileId) {

    logger.info("Starting classification of recon data...");
    final String reconFieldFormat = "recon.%s";
    final String txnFieldFormat = "txn.%s";

    // Step 1: Normalize matching keys to avoid type/whitespace mismatches
    Dataset<Row> recon = reconFileDataset
            .withColumn(OperationsConstant.ATRN_NUM,
                    trim(col(OperationsConstant.ATRN_NUM).cast("string")))
            .withColumn(OperationsConstant.DEBIT_AMT,
                    col(OperationsConstant.DEBIT_AMT).cast("decimal(18,2)"))
            .withColumn("ROW_NUMBER", monotonically_increasing_id())
            .alias("recon");

    Dataset<Row> txn = transactionDataset
            .withColumn(OperationsConstant.ATRN_NUM,
                    trim(col(OperationsConstant.ATRN_NUM).cast("string")))
            .withColumn(OperationsConstant.DEBIT_AMT,
                    col(OperationsConstant.DEBIT_AMT).cast("decimal(18,2)"))
            .alias("txn");

    // Step 2: Window spec for MATCH_RANK
    WindowSpec matchWindow = Window
            .partitionBy(
                    col(String.format(reconFieldFormat, OperationsConstant.ATRN_NUM)),
                    col(String.format(reconFieldFormat, OperationsConstant.DEBIT_AMT))
            )
            .orderBy(col(String.format(reconFieldFormat, "ROW_NUMBER")));

    // Step 3: Join recon & txn
    Dataset<Row> joined = recon.join(txn,
                    col(String.format(reconFieldFormat, OperationsConstant.ATRN_NUM))
                            .equalTo(col(String.format(txnFieldFormat, OperationsConstant.ATRN_NUM)))
                            .and(col(String.format(reconFieldFormat, OperationsConstant.DEBIT_AMT))
                                    .equalTo(col(String.format(txnFieldFormat, OperationsConstant.DEBIT_AMT)))),
                    "left_outer")
            .withColumn(OperationsConstant.EXACT_MATCH,
                    when(col(String.format(txnFieldFormat, OperationsConstant.ATRN_NUM)).isNotNull(), lit(1)).otherwise(lit(0)))
            .withColumn(OperationsConstant.MATCH_RANK, row_number().over(matchWindow))
            .withColumn(MERCHANT_ID, col(String.format(txnFieldFormat, MERCHANT_ID)));

    // Debugging: Count how many matched vs unmatched
    joined.groupBy(OperationsConstant.EXACT_MATCH).count().show(false);

    // Step 4: Find distinct matched ATRN_NUM
    Dataset<Row> matchedAtrns = joined
            .filter(col(OperationsConstant.EXACT_MATCH).equalTo(1)
                    .and(col(OperationsConstant.MATCH_RANK).equalTo(1)))
            .select(col(String.format(reconFieldFormat, OperationsConstant.ATRN_NUM))
                    .alias(OperationsConstant.MATCHED_ATRN))
            .distinct();

    // Step 5: Mark duplicates
    Dataset<Row> withMatchedFlag = joined.join(matchedAtrns,
                    col(String.format(reconFieldFormat, OperationsConstant.ATRN_NUM))
                            .equalTo(col(OperationsConstant.MATCHED_ATRN)),
                    "left_outer")
            .withColumn(OperationsConstant.ATRN_MATCHED,
                    col(OperationsConstant.MATCHED_ATRN).isNotNull());

    // Step 6: Classification logic
    Dataset<Row> finalStatus = withMatchedFlag.withColumn(OperationsConstant.RECON_STATUS,
            when(col(OperationsConstant.EXACT_MATCH).equalTo(1)
                    .and(col(OperationsConstant.MATCH_RANK).equalTo(1)),
                    lit(OperationsConstant.RECON_STATUS_MATCHED))
                    .when(col(OperationsConstant.EXACT_MATCH).equalTo(1)
                            .and(col(OperationsConstant.MATCH_RANK).gt(1)),
                            lit(OperationsConstant.RECON_STATUS_DUPLICATE))
                    .when(col(OperationsConstant.EXACT_MATCH).equalTo(0)
                            .and(col(OperationsConstant.ATRN_MATCHED).equalTo(true)),
                            lit(OperationsConstant.RECON_STATUS_DUPLICATE))
                    .otherwise(lit(OperationsConstant.RECON_STATUS_UNMATCHED)));

    // Step 7: Prepare matched dataset
    Dataset<Row> matched = finalStatus
            .filter(col(OperationsConstant.RECON_STATUS)
                    .equalTo(OperationsConstant.RECON_STATUS_MATCHED))
            .withColumn(OperationsConstant.RF_ID, lit(reconFileId.toString()))
            .select(
                    col("ROW_NUMBER"),
                    col(OperationsConstant.RECON_STATUS),
                    col(OperationsConstant.RF_ID),
                    col(MERCHANT_ID)
            );

    // Step 8: Prepare unmatched/duplicate dataset
    Dataset<Row> unmatchedOrDuplicate = finalStatus
            .filter(col(OperationsConstant.RECON_STATUS)
                    .isin(OperationsConstant.RECON_STATUS_UNMATCHED, OperationsConstant.RECON_STATUS_DUPLICATE))
            .withColumn(OperationsConstant.RF_ID, lit(reconFileId.toString()))
            .select(
                    col("ROW_NUMBER"),
                    col(OperationsConstant.RECON_STATUS),
                    col(OperationsConstant.RF_ID),
                    col(MERCHANT_ID)
            );

    // Step 9: Return results in a map
    Map<String, Dataset<Row>> result = new HashMap<>();
    result.put(OperationsConstant.RECON_STATUS_MATCHED, matched);
    result.put(OperationsConstant.OTHERS, unmatchedOrDuplicate);

    return result;
}
