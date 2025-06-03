public void reconProcess(String rfsId) {
        logger.info("Recon process started.");
        long processStartTime = currentTimeMillis();
        logger.info("🚀 Recon process started at : {} ", sparkService.formatMillis(processStartTime));

        // Load datasets
        logger.info("🚀 fetch merchant order payments starts : {} ", currentTimeMillis());
        long localStartTime = currentTimeMillis();
        Dataset<Row> merchantOrderPayments = readAndNormalize("MERCHANT_ORDER_PAYMENTS", "CREATED_DATE", 1726138792000L, 1726138792000L).alias("src");
        logger.info("🚀 fetch merchant order payments ends : {}", sparkService.formatMillis(currentTimeMillis() - localStartTime));

        logger.info("🚀 fetch recon file details starts : {}", currentTimeMillis());
        localStartTime = currentTimeMillis();
        Dataset<Row> reconFileDtls = readAndNormalize("RECON_FILE_DTLS", "PAYMENT_DATE", 1726138792000L, 1726138792000L).alias("tgt");
        logger.info("🚀 fetch recon file details ends : {}", sparkService.formatMillis(currentTimeMillis() - localStartTime));

        // Rename recon file columns to match source
        reconFileDtls = renameColumns(reconFileDtls).alias("recon");

        // Join and match logic
        Column joinCond = reconFileDtls.col("ATRN_NUM").equalTo(merchantOrderPayments.col("ATRN_NUM"))
                .and(reconFileDtls.col("DEBIT_AMT").equalTo(merchantOrderPayments.col("DEBIT_AMT")));

        Column valueMatch = joinCond;
        for (String col : columnMapping().keySet()) {
            valueMatch = valueMatch.and(reconFileDtls.col(col).equalTo(merchantOrderPayments.col(col)));
        }

        //Filter only unique ATRN_NUMs from reconFileDtls
        Dataset<Row> reconUnique = reconFileDtls.groupBy("ATRN_NUM")
                .count()
                .filter("count = 1")
                .select("ATRN_NUM");

        Dataset<Row> filteredRecon = reconFileDtls.join(reconUnique, "ATRN_NUM");

        //Join unique-only records to find matched ones
        Column matchCondition = filteredRecon.col("ATRN_NUM").equalTo(merchantOrderPayments.col("ATRN_NUM"))
                .and(filteredRecon.col("DEBIT_AMT").equalTo(merchantOrderPayments.col("DEBIT_AMT")));

        // Matched rows
        logger.info("🚀 Fetch matched data starts: {}", currentTimeMillis());
        localStartTime = currentTimeMillis();
        Dataset<Row> matched = filteredRecon
                .join(merchantOrderPayments, matchCondition, "inner")
                .dropDuplicates("ATRN_NUM");
        logger.info("🚀 Fetch matched data ends: {}", sparkService.formatMillis(currentTimeMillis() - localStartTime));

        // Unmatched rows from reconFileDtls not in merchantOrderPayments
        logger.info("🚀 Fetch unmatched data starts: {}", sparkService.formatMillis(currentTimeMillis()));
        localStartTime = currentTimeMillis();
        Dataset<Row> unmatched = reconFileDtls.join(merchantOrderPayments, joinCond, "left_anti").distinct();
        logger.info("🚀 Fetch unmatched data ends: {}", sparkService.formatMillis(currentTimeMillis() - localStartTime));

        // Duplicates in reconFileDtls
        logger.info("🚀 Fetch duplicate data starts: {}", sparkService.formatMillis(currentTimeMillis()));
        localStartTime = currentTimeMillis();
        Dataset<Row> reconFileDetailDuplicate = getDuplicates(reconFileDtls, "ATRN_NUM");
        logger.info("🚀 Fetch duplicate data ends: {}", sparkService.formatMillis(currentTimeMillis() - localStartTime));

        // Update match_status back in RECON_FILE_DTLS
        logger.info("🚀 Update process data starts: {}", sparkService.formatMillis(currentTimeMillis()));
        localStartTime = currentTimeMillis();
        updateReconFileDetails(matched, "MATCHED");
        updateReconFileDetails(unmatched, "UNMATCHED");
        updateReconFileDetails(reconFileDetailDuplicate, "DUPLICATE");
        logger.info("🚀 Update process data ends: {}", sparkService.formatMillis(currentTimeMillis() - localStartTime));

        logger.info("Recon process completed in: {}", sparkService.formatMillis(currentTimeMillis() - processStartTime));
    }
