public void reconProcess(long startTime, long endTime) {
    logger.info("Recon process started.");
    long processStartTime = currentTimeMillis();
    logger.info("🚀 Recon process started at : {} ", sparkService.formatMillis(processStartTime));

    // Load datasets
    logger.info("Reading merchant order payments from DB.");
    Dataset<Row> merchantOrderPayments = readAndNormalize("MERCHANT_ORDER_PAYMENTS", "CREATED_DATE", startTime, endTime)
            .alias("src");
    merchantOrderPayments.printSchema();

    logger.info("Reading recon file details from DB.");
    Dataset<Row> reconFileDtls = readAndNormalize("RECON_FILE_DTLS", "PAYMENT_DATE", startTime, endTime)
            .alias("tgt");
    reconFileDtls.printSchema();

    logger.info("Renaming recon file columns to match source schema.");
    reconFileDtls = renameColumns(reconFileDtls).alias("tgt");

    // Deduplicate both datasets
    logger.info("Deduplicating merchant order payments.");
    Dataset<Row> merchantDeduped = merchantOrderPayments.dropDuplicates().alias("src");

    logger.info("Deduplicating recon file details.");
    Dataset<Row> reconDeduped = reconFileDtls.dropDuplicates().alias("tgt");

    // Join on ATRN_NUM
    logger.info("Joining on ATRN_NUM.");
    Dataset<Row> joined = merchantDeduped.alias("src")
        .join(reconDeduped.alias("tgt"),
              functions.col("src.ATRN_NUM").equalTo(functions.col("tgt.ATRN_NUM")), "inner");

    // Match on value columns
    Column valueMatch = lit(true);
    for (String col : columnMapping().keySet()) {
        valueMatch = valueMatch.and(
            functions.col("src." + col).equalTo(functions.col("tgt." + col))
        );
    }

    // Matched records
    logger.info("Filtering matched records.");
    Dataset<Row> matched = joined.filter(valueMatch).select("src.*");

    // Unmatched records (source not in target)
    logger.info("Finding unmatched records (source not in target).");
    Dataset<Row> unmatched = merchantDeduped.alias("src")
        .join(reconDeduped.alias("tgt"),
              functions.col("src.ATRN_NUM").equalTo(functions.col("tgt.ATRN_NUM")), "left_anti");

    // Duplicate Detection
    logger.info("Finding duplicate records in source.");
    Dataset<Row> sourceDuplicates = getDuplicates(merchantOrderPayments, "ATRN_NUM")
        .join(merchantOrderPayments, "ATRN_NUM")
        .alias("src_dup");

    logger.info("Finding duplicate records in target.");
    Dataset<Row> targetDuplicates = getDuplicates(reconFileDtls, "ATRN_NUM")
        .join(reconFileDtls, "ATRN_NUM")
        .alias("tgt_dup");

    // Log summaries
    logDataset("Matched Rows", matched);
    logDataset("Unmatched Rows", unmatched);
    logDataset("Target Duplicates", targetDuplicates);

    // Save results
    logger.info("Inserting reconciliation results into DB.");
    saveToReconciliationTable(matched, "MATCHED", "matched");
    saveToReconciliationTable(unmatched, "UNMATCHED", "atrn not matched");
    saveToReconciliationTable(targetDuplicates, "TARGET_DUPLICATE", "duplicate atrn in recon file details");

    logger.info("Recon process completed in: {}", sparkService.formatMillis(currentTimeMillis() - processStartTime));
}
