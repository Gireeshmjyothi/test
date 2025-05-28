Dataset<Row> tgt1 = reconFileDtls.alias("tgt1");
Dataset<Row> tgt2 = getDuplicates(reconFileDtls, "ATRN_NUM").alias("tgt2");

Dataset<Row> targetDuplicates = tgt2
    .join(tgt1, tgt2.col("ATRN_NUM").equalTo(tgt1.col("ATRN_NUM")))
    .select("tgt1.*") // choose one side's columns to avoid ambiguity
    .distinct();



public void reconProcess(long startTime, long endTime) {
        logger.info("Recon process started.");
        long processStartTime = currentTimeMillis();
        logger.info("🚀 Recon process started at : {} ", sparkService.formatMillis(processStartTime));

        // Load datasets
        Dataset<Row> merchantOrderPayments = readAndNormalize("MERCHANT_ORDER_PAYMENTS", "CREATED_DATE", startTime, endTime).alias("src");
        Dataset<Row> reconFileDtls = readAndNormalize("RECON_FILE_DTLS", "PAYMENT_DATE", startTime, endTime).alias("tgt");

        // Rename recon file columns to match source
        reconFileDtls = renameColumns(reconFileDtls).alias("tgt");

        // Deduplicate datasets
        Dataset<Row> merchantDeduped = merchantOrderPayments.dropDuplicates().alias("src");
        Dataset<Row> reconDeduped = reconFileDtls.dropDuplicates().alias("tgt");

        // Join and match logic
        Column joinCond = merchantDeduped.col("ATRN_NUM").equalTo(reconDeduped.col("ATRN_NUM"));
        Column valueMatch = joinCond;
        for (String col : columnMapping().keySet()) {
            valueMatch = valueMatch.and(merchantDeduped.col(col).equalTo(reconDeduped.col(col)));
        }

        Dataset<Row> matched = merchantDeduped.join(reconDeduped, valueMatch, "inner").select("src.*").distinct();
        Dataset<Row> unmatched = merchantDeduped.join(reconDeduped, joinCond, "left_anti").distinct();

        // Duplicates
        Dataset<Row> targetDuplicates = getDuplicates(reconFileDtls, "ATRN_NUM").join(reconFileDtls, "ATRN_NUM").distinct();

        // Logging
        logDataset("Matched Rows", matched);
        logDataset("Unmatched Rows", unmatched);
        logDataset("Target Duplicates", targetDuplicates);

        // Save results
        saveToReconciliationTable(matched, "MATCHED", "matched");
        saveToReconciliationTable(unmatched, "UNMATCHED", "atrn not matched");
        saveToReconciliationTable(targetDuplicates, "TARGET_DUPLICATE", "duplicate atrn in recon file details");

        logger.info("Recon process completed in: {}", sparkService.formatMillis(currentTimeMillis() - processStartTime));
    }



private void saveToReconciliationTable(Dataset<Row> dataset, String matchStatus, String reason) {
        if (dataset.isEmpty()) {
            logger.warn("No data to save for matchStatus: {}", matchStatus);
            return;
        }
        Date currentTimeStamp = Date.valueOf(LocalDateTime.now().toLocalDate());
        Timestamp batchTime = Timestamp.valueOf(LocalDateTime.now());
        Dataset<Row> resultDataset = dataset
                .withColumn("match_status", lit(matchStatus))
                .withColumn("mismatch_reason", lit(reason))
                .withColumn("source_json", functions.to_json(functions.struct(Arrays.stream(dataset.columns()).map(functions::col).toArray(Column[]::new))))
                .withColumn("recon_json", functions.to_json(functions.struct(Arrays.stream(dataset.columns()).map(functions::col).toArray(Column[]::new))))
                .withColumn("reconciled_at", lit(currentTimeStamp))
                .withColumn("batch_date", lit(batchTime));

        resultDataset.printSchema();
        jdbcReaderService.writeToReconFileResult(resultDataset, "RECONCILIATION_RESULT");
    }
