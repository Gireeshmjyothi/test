public void reconProcess(long startTime, long endTime) {
        logger.info("Recon process started.");
        long processStartTime = currentTimeMillis();
        logger.info("🚀 Recon process started at : {} ", sparkService.formatMillis(processStartTime));

        // Load datasets
        logger.info("🚀 fetch merchant order payments starts : {} ", currentTimeMillis());
        long localStartTime = currentTimeMillis();
        Dataset<Row> merchantOrderPayments = readAndNormalize("MERCHANT_ORDER_PAYMENTS", "CREATED_DATE", startTime, endTime).alias("src");
        logger.info("🚀 fetch merchant order payments ends : {}", sparkService.formatMillis(currentTimeMillis() - localStartTime));

        logger.info("🚀 fetch recon file details starts : {}", currentTimeMillis());
        localStartTime = currentTimeMillis();
        Dataset<Row> reconFileDtls = readAndNormalize("RECON_FILE_DTLS", "PAYMENT_DATE", startTime, endTime).alias("tgt");
        logger.info("🚀 fetch recon file details ends : {}", sparkService.formatMillis(currentTimeMillis() - localStartTime));

        // Rename recon file columns to match source
        reconFileDtls = renameColumns(reconFileDtls).alias("tgt");

        // Deduplicate datasets
        Dataset<Row> merchantDeduped = merchantOrderPayments.dropDuplicates().alias("src");

        Dataset<Row> reconDeduped = reconFileDtls.dropDuplicates().alias("tgt");

        // Join and match logic
        Column joinCond = merchantDeduped.col("ATRN_NUM").equalTo(reconDeduped.col("ATRN_NUM"))
                .and(merchantDeduped.col("DEBIT_AMT").equalTo(reconDeduped.col("DEBIT_AMT")));

        Column valueMatch = joinCond;
        for (String col : columnMapping().keySet()) {
            valueMatch = valueMatch.and(merchantDeduped.col(col).equalTo(reconDeduped.col(col)));
        }

        logger.info("🚀 fetch matched data starts : {} ", currentTimeMillis());
        localStartTime = currentTimeMillis();
        Dataset<Row> matched = merchantDeduped.join(reconDeduped, valueMatch, "inner").select("src.*").dropDuplicates("ATRN_NUM");
        logger.info("🚀 fetch matched data ends : {}", sparkService.formatMillis(currentTimeMillis() - localStartTime));

        logger.info("🚀 fetch unmatched data starts : {} ", sparkService.formatMillis(currentTimeMillis()));
        localStartTime = currentTimeMillis();
        Dataset<Row> unmatched = merchantDeduped.join(reconDeduped, joinCond, "left_anti").distinct();
        logger.info("🚀 fetch unmatched data ends : {}", sparkService.formatMillis(currentTimeMillis() - localStartTime));

        // Duplicates
        Dataset<Row> tgt1 = reconFileDtls.alias("tgt1");
        Dataset<Row> tgt2 = getDuplicates(reconFileDtls, "ATRN_NUM").alias("tgt2");

        logger.info("🚀 fetch duplicate data starts : {} ", sparkService.formatMillis(currentTimeMillis()));
        localStartTime = currentTimeMillis();
        Dataset<Row> reconFileDetailDuplicate = tgt2
                .join(tgt1, tgt2.col("ATRN_NUM").equalTo(tgt1.col("ATRN_NUM")))
                .select("tgt1.*");
        logger.info("🚀 fetch duplicate data ends: {}", sparkService.formatMillis(currentTimeMillis() - localStartTime));

        // Save results
        logger.info("🚀 insert process data starts : {} ", sparkService.formatMillis(currentTimeMillis()));
        localStartTime = currentTimeMillis();
        saveToReconciliationTable(matched, "MATCHED", "matched");
        saveToReconciliationTable(unmatched, "UNMATCHED", "atrn not matched");
        saveToReconciliationTable(reconFileDetailDuplicate, "TARGET_DUPLICATE", "duplicate atrn in recon file details");
        logger.info("🚀 insert process data ends : {} ", sparkService.formatMillis(currentTimeMillis() - localStartTime));

        logger.info("Recon process completed in: {}", sparkService.formatMillis(currentTimeMillis() - processStartTime));
    }

private void saveToReconciliationTable(Dataset<Row> dataset, String matchStatus, String reason) {
        Date currentTimeStamp = Date.valueOf(LocalDateTime.now().toLocalDate());
        Timestamp batchTime = Timestamp.valueOf(LocalDateTime.now());
        Dataset<Row> resultDataset = dataset
                .withColumn("match_status", lit(matchStatus))
                .withColumn("mismatch_reason", lit(reason))
                .withColumn("source_json", functions.to_json(functions.struct(Arrays.stream(dataset.columns()).map(functions::col).toArray(Column[]::new))))
                .withColumn("recon_json", functions.to_json(functions.struct(Arrays.stream(dataset.columns()).map(functions::col).toArray(Column[]::new))))
                .withColumn("reconciled_at", lit(currentTimeStamp))
                .withColumn("batch_date", lit(batchTime));
        jdbcReaderService.writeToReconFileResult(resultDataset, "RECONCILIATION_RESULT");
    }

public void writeToReconFileResult(Dataset<Row> df, String tableName) {
        df.select("atrn_num", "match_status", "mismatch_reason",
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
