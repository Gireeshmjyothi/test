public void reconProcess(long startTime, long endTime) {
    logger.info("Recon process started.");

    // Load datasets
    logger.info("Reading merchant order payments from DB.");
    Dataset<Row> merchantOrderPayments = readAndNormalize("MERCHANT_ORDER_PAYMENTS", "CREATED_DATE", startTime, endTime);

    logger.info("Reading recon file details from DB.");
    Dataset<Row> reconFileDtls = readAndNormalize("RECON_FILE_DTLS", "PAYMENT_DATE", startTime, endTime);

    // Rename recon file columns to match source
    logger.info("Renaming recon file columns to match source schema.");
    reconFileDtls = renameColumns(reconFileDtls);

    // Deduplicate both datasets
    logger.info("Deduplicating merchant order payments.");
    Dataset<Row> merchantDeduped = merchantOrderPayments.dropDuplicates();

    logger.info("Deduplicating recon file details.");
    Dataset<Row> reconDeduped = reconFileDtls.dropDuplicates();

    // Join condition on key
    Column joinCond = merchantDeduped.col("ATRN_NUM").equalTo(reconDeduped.col("ATRN_NUM"));

    // Match on all mapped values
    Column valueMatch = joinCond;
    for (String col : columnMapping().keySet()) {
        valueMatch = valueMatch.and(merchantDeduped.col(col).equalTo(reconDeduped.col(col)));
    }

    // Results
    logger.info("Finding matched records.");
    Dataset<Row> matched = merchantDeduped.join(reconDeduped, valueMatch, "inner");

    logger.info("Finding unmatched records (source not in target).");
    Dataset<Row> unmatched = merchantDeduped.join(reconDeduped, joinCond, "left_anti");

    // Duplicate Detection by ATRN_NUM
    logger.info("Finding duplicate records in source.");
    Dataset<Row> sourceDuplicates = getDuplicates(merchantOrderPayments, new String[]{"ATRN_NUM"})
            .join(merchantOrderPayments, "ATRN_NUM");

    logger.info("Finding duplicate records in target.");
    Dataset<Row> targetDuplicates = getDuplicates(reconFileDtls, new String[]{"ATRN_NUM"})
            .join(reconFileDtls, "ATRN_NUM");

    // Log summaries
    logDataset("Matched Rows", matched);
    logDataset("Unmatched Rows", unmatched);
    logDataset("Source Duplicates", sourceDuplicates);
    logDataset("Target Duplicates", targetDuplicates);

    // Save results
    logger.info("Inserting reconciliation results into DB.");
    saveToReconciliationTable(matched, "MATCHED", true);
    saveToReconciliationTable(unmatched, "UNMATCHED", false);
    saveToReconciliationTable(sourceDuplicates, "SOURCE_DUPLICATE", false);
    saveToReconciliationTable(targetDuplicates, "TARGET_DUPLICATE", false);

    logger.info("Recon process completed.");
}

private Dataset<Row> getDuplicates(Dataset<Row> dataset, String[] keyCols) {
    return dataset.groupBy(keyCols)
                  .count()
                  .filter("count > 1")
                  .selectExpr(keyCols);
}
