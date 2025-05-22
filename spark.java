public void reconProcess(long startTime, long endTime) {
    logger.info("Recon process started.");

    // Load datasets
    logger.info("Reading merchant order payments from DB.");
    Dataset<Row> merchantOrderPayments = readAndNormalize("MERCHANT_ORDER_PAYMENTS", "CREATED_DATE", startTime, endTime)
            .alias("src");

    logger.info("Reading recon file details from DB.");
    Dataset<Row> reconFileDtls = readAndNormalize("RECON_FILE_DTLS", "PAYMENT_DATE", startTime, endTime)
            .alias("tgt");

    // Rename recon file columns to match source
    logger.info("Renaming recon file columns to match source schema.");
    reconFileDtls = renameColumns(reconFileDtls).alias("tgt");

    // Deduplicate both datasets
    logger.info("Deduplicating merchant order payments.");
    Dataset<Row> merchantDeduped = merchantOrderPayments.dropDuplicates().alias("src");

    logger.info("Deduplicating recon file details.");
    Dataset<Row> reconDeduped = reconFileDtls.dropDuplicates().alias("tgt");

    // Join condition on ATRN_NUM
    Column joinCond = merchantDeduped.col("ATRN_NUM").equalTo(reconDeduped.col("ATRN_NUM"));

    // Value match on all mapped fields
    Column valueMatch = joinCond;
    for (String col : columnMapping().keySet()) {
        valueMatch = valueMatch.and(
            merchantDeduped.col(col).equalTo(reconDeduped.col(col))
        );
    }

    // Matched records
    logger.info("Finding matched records.");
    Dataset<Row> matched = merchantDeduped.join(reconDeduped, valueMatch, "inner")
            .select("src.*"); // Take only source columns to avoid ambiguity

    // Unmatched records from source
    logger.info("Finding unmatched records (source not in target).");
    Dataset<Row> unmatched = merchantDeduped.join(reconDeduped, joinCond, "left_anti");

    // Duplicate Detection by ATRN_NUM
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


 private void saveToReconciliationTable(Dataset<Row> dataset, String matchStatus, boolean isMatched) {
        Dataset<Row> resultDataset = dataset.withColumn("match_status", functions.lit(matchStatus))
                .withColumn("mismatch_reason", functions.lit(isMatched ? null : "Data mismatch"))
                .withColumn("source_json", functions.to_json(structFromColumns(dataset.columns())))
                .withColumn("recon_json", functions.to_json(structFromColumns(dataset.columns())))
                .withColumn("reconciled_at", functions.current_timestamp())
                .withColumn("batch_date", functions.current_date());


        jdbcReaderService.writeToReconFileResult(resultDataset, "RECONCILIATION_RESULT");
    }
public void writeToReconFileResult(Dataset<Row> df, String tableName) {
        df.select(
                        "atrn_num", "match_status", "mismatch_reason",
                        "source_json", "recon_json", "reconciled_at", "batch_date"
                ).write()
                .format("jdbc")
                .option("url", jdbcConfig.getJdbcUrl())
                .option("dbtable", tableName)
                .option("user", jdbcUserName)
                .option("password", jdbcPassword)
                .option("driver", jdbcDriver)
                .mode(SaveMode.Append)
                .save();
    }
