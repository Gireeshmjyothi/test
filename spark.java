private Dataset<Row> addStatus(Dataset<Row> dataset, String reconStatus, String settlementStatus) {
    Dataset<Row> updated = dataset.withColumn(RECON_STATUS, lit(reconStatus));
    if (settlementStatus != null) {
        updated = updated.withColumn(SETTLEMENT_STATUS_FIELD, lit(settlementStatus));
    }
    return updated;
}


private Map<String, Dataset<Row>> findMatchedUnmatchedAndDuplicateReconRecords(Dataset<Row> transactionDataset, Dataset<Row> reconFileDataset, UUID reconFileId) {
    logger.info("Starting classification of recon data...");
    final String reconFieldFormat = "recon.%s";
    final String txnFieldFormat = "txn.%s";

    // Step 1: Aliases
    Dataset<Row> recon = reconFileDataset.alias("recon");
    Dataset<Row> txn = transactionDataset.alias("txn");

    // Step 2: row_number over match window
    WindowSpec matchWindow = Window.partitionBy(
            col(reconFieldFormat.formatted(ATRN_NUM)),
            col(reconFieldFormat.formatted(DEBIT_AMT))
    ).orderBy(col(reconFieldFormat.formatted(RFD_ID)));

    // Step 3: Join + flags
    Dataset<Row> joined = recon.join(txn,
            col(reconFieldFormat.formatted(ATRN_NUM)).equalTo(col(txnFieldFormat.formatted(ATRN_NUM)))
            .and(col(reconFieldFormat.formatted(DEBIT_AMT)).equalTo(col(txnFieldFormat.formatted(DEBIT_AMT)))),
            "left_outer")
        .withColumn(EXACT_MATCH, when(col(txnFieldFormat.formatted(ATRN_NUM)).isNotNull(), lit(1)).otherwise(lit(0)))
        .withColumn(MATCH_RANK, when(col(EXACT_MATCH).equalTo(1), row_number().over(matchWindow)));

    // Step 4: Find matched ATRN_NUMs
    Dataset<Row> matchedAtrns = joined
        .filter(col(MATCH_RANK).equalTo(1))
        .select(col(reconFieldFormat.formatted(ATRN_NUM)).alias(MATCHED_ATRN))
        .distinct();

    // Step 5: Re-join for duplicate detection
    Dataset<Row> withMatchedFlag = joined
        .join(matchedAtrns, col(reconFieldFormat.formatted(ATRN_NUM)).equalTo(col(MATCHED_ATRN)), "left_outer")
        .withColumn(ATRN_MATCHED, col(MATCHED_ATRN).isNotNull());

    // Step 6: Classification
    Dataset<Row> finalStatus = withMatchedFlag.withColumn(RECON_STATUS,
        when(col(EXACT_MATCH).equalTo(1).and(col(MATCH_RANK).equalTo(1)), lit(RECON_STATUS_MATCHED))
        .when(col(EXACT_MATCH).equalTo(1).and(col(MATCH_RANK).gt(1)), lit(RECON_STATUS_DUPLICATE))
        .when(col(EXACT_MATCH).equalTo(0).and(col(ATRN_MATCHED).equalTo(true)), lit(RECON_STATUS_DUPLICATE))
        .otherwise(lit(RECON_STATUS_UNMATCHED)));

    // Step 7: Apply logic per classification

    // ✅ Matched → update both
    Dataset<Row> matched = finalStatus
        .filter(col(RECON_STATUS).equalTo(RECON_STATUS_MATCHED))
        .withColumn(RECON_STATUS, lit(RECON_STATUS_MATCHED))
        .withColumn(SETTLEMENT_STATUS_FIELD,
            when(col(SETTLEMENT_STATUS_FIELD).isNull()
                    .or(col(SETTLEMENT_STATUS_FIELD).equalTo(Status.PENDING.name())),
                 lit(SETTLEMENT_STATUS_SETTLED))
            .otherwise(col(SETTLEMENT_STATUS_FIELD)))
        .withColumn(RFS_ID, lit(String.valueOf(reconFileId)))
        .select(RFD_ID, RECON_STATUS, SETTLEMENT_STATUS_FIELD, RFS_ID);

    // ✅ Unmatched & Duplicate → only update reconStatus
    Dataset<Row> unmatchedOrDuplicate = finalStatus
        .filter(col(RECON_STATUS).isin(RECON_STATUS_UNMATCHED, RECON_STATUS_DUPLICATE))
        .withColumn(RFS_ID, lit(String.valueOf(reconFileId)))
        .select(RFD_ID, RECON_STATUS, RFS_ID);

    // Step 8: Return as Map
    Map<String, Dataset<Row>> result = new HashMap<>();
    result.put(RECON_STATUS_MATCHED, matched);
    result.put(OTHERS, unmatchedOrDuplicate);

    return result;
}
