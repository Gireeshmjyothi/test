public void reconProcess(long startTime, long endTime) {
    logger.info("Recon process started.");

    // Load datasets
    Dataset<Row> merchantOrderPayments = readAndNormalize("MERCHANT_ORDER_PAYMENTS", "CREATED_DATE", startTime, endTime)
            .alias("src");

    Dataset<Row> reconFileDtls = readAndNormalize("RECON_FILE_DTLS", "PAYMENT_DATE", startTime, endTime)
            .alias("tgt");

    reconFileDtls = renameColumns(reconFileDtls).alias("tgt");

    // Deduplicate
    Dataset<Row> merchantDeduped = merchantOrderPayments.dropDuplicates().alias("src");
    Dataset<Row> reconDeduped = reconFileDtls.dropDuplicates().alias("tgt");

    // Join condition and value match
    Column joinCond = merchantDeduped.col("ATRN_NUM").equalTo(reconDeduped.col("ATRN_NUM"));
    Column valueMatch = joinCond;
    for (String col : columnMapping().keySet()) {
        valueMatch = valueMatch.and(merchantDeduped.col(col).equalTo(reconDeduped.col(col)));
    }

    // Matched
    Dataset<Row> matched = merchantDeduped.join(reconDeduped, valueMatch, "inner")
            .select(merchantDeduped.col("*"), reconDeduped.col("*"));

    // Unmatched
    Dataset<Row> unmatched = merchantDeduped.join(reconDeduped, joinCond, "left_anti");

    // Target Duplicates
    Dataset<Row> targetDuplicates = getDuplicates(reconFileDtls, "ATRN_NUM")
            .join(reconFileDtls, "ATRN_NUM");
    targetDuplicates = prefixColumns(targetDuplicates, "TGT_");

    // Save results
    saveToReconciliationTable(prefixColumns(matched, "SRC_"), "MATCHED", "ATRN is matched");
    saveToReconciliationTable(prefixColumns(unmatched, "SRC_"), "UNMATCHED", "ATRN is not matched");
    saveToReconciliationTable(targetDuplicates, "TARGET_DUPLICATE", "Duplicate ATRN");

    logger.info("Recon process completed.");
}

private void saveToReconciliationTable(Dataset<Row> dataset, String matchStatus, String reason) {
    Dataset<Row> resultDataset = dataset
            .withColumn("match_status", functions.lit(matchStatus))
            .withColumn("mismatch_reason", functions.lit(reason))
            .withColumn("source_json", functions.to_json(structFromColumnsStartingWith(dataset, "SRC_")))
            .withColumn("recon_json", functions.to_json(structFromColumnsStartingWith(dataset, "TGT_")))
            .withColumn("reconciled_at", functions.current_timestamp())
            .withColumn("batch_date", functions.current_date());

    jdbcReaderService.writeToReconFileResult(resultDataset, "RECONCILIATION_RESULT");
}
private Column[] structFromColumnsStartingWith(Dataset<Row> dataset, String prefix) {
    return Arrays.stream(dataset.columns())
            .filter(col -> col.startsWith(prefix))
            .map(functions::col)
            .toArray(Column[]::new);
}

private Dataset<Row> prefixColumns(Dataset<Row> df, String prefix) {
    return df.select(Arrays.stream(df.columns())
            .map(col -> df.col(col).alias(prefix + col))
            .toArray(Column[]::new));
}
