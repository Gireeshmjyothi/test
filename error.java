public void reconProcess(long startTime, long endTime) {
        logger.info("Recon process started.");
        long processStartTime = currentTimeMillis();
        logger.info("🚀 Recon process started at : {} ", sparkService.formatMillis(processStartTime));

        // Load datasets
        logger.info("🚀 fetch merchant order payments starts : {} ", sparkService.formatMillis(currentTimeMillis()));
        Dataset<Row> merchantOrderPayments = readAndNormalize("MERCHANT_ORDER_PAYMENTS", "CREATED_DATE", startTime, endTime).alias("src");
        logger.info("🚀 fetch merchant order payments ends : {} ", sparkService.formatMillis(currentTimeMillis()));

        logger.info("🚀 fetch recon file details starts : {} ", sparkService.formatMillis(currentTimeMillis()));
        Dataset<Row> reconFileDtls = readAndNormalize("RECON_FILE_DTLS", "PAYMENT_DATE", startTime, endTime).alias("tgt");
        logger.info("🚀 fetch recon file details ends : {} ", sparkService.formatMillis(currentTimeMillis()));

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

        logger.info("🚀 fetch matched data starts : {} ", sparkService.formatMillis(currentTimeMillis()));
        Dataset<Row> matched = merchantDeduped.join(reconDeduped, valueMatch, "inner").select("src.*").distinct();
        logger.info("🚀 fetch matched data ends : {}", sparkService.formatMillis(currentTimeMillis()));

        logger.info("🚀 fetch unmatched data starts : {} ", sparkService.formatMillis(currentTimeMillis()));
        Dataset<Row> unmatched = merchantDeduped.join(reconDeduped, joinCond, "left_anti").distinct();
        logger.info("🚀 fetch unmatched data ends : {}", sparkService.formatMillis(currentTimeMillis()));

        // Duplicates
//        Dataset<Row> targetDuplicates = getDuplicates(reconFileDtls, "ATRN_NUM").join(reconFileDtls, "ATRN_NUM").distinct();
        Dataset<Row> tgt1 = reconFileDtls.alias("tgt1");
        Dataset<Row> tgt2 = getDuplicates(reconFileDtls, "ATRN_NUM").alias("tgt2");

        logger.info("🚀 fetch duplicate data starts : {} ", sparkService.formatMillis(currentTimeMillis()));
        Dataset<Row> targetDuplicates = tgt2
                .join(tgt1, tgt2.col("ATRN_NUM").equalTo(tgt1.col("ATRN_NUM")))
                .select("tgt1.*") // choose one side's columns to avoid ambiguity
                .distinct();
        logger.info("🚀 fetch duplicate data ends : {}", sparkService.formatMillis(currentTimeMillis()));
       /* // Logging
        logDataset("Matched Rows", matched);
        logDataset("Unmatched Rows", unmatched);
        logDataset("Target Duplicates", targetDuplicates);
*/
        // Save results
        logger.info("🚀 insert process data starts : {} ", sparkService.formatMillis(currentTimeMillis()));
        saveToReconciliationTable(matched, "MATCHED", "matched");
        saveToReconciliationTable(unmatched, "UNMATCHED", "atrn not matched");
        saveToReconciliationTable(targetDuplicates, "TARGET_DUPLICATE", "duplicate atrn in recon file details");
        logger.info("🚀 insert process data ends : {} ", sparkService.formatMillis(currentTimeMillis()));

        logger.info("Recon process completed in: {}", sparkService.formatMillis(currentTimeMillis() - processStartTime));
    }
