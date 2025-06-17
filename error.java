public void reconProcess(UUID rfsId) {
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
        Dataset<Row> reconFileDtls = readAndNormalize("RECON_FILE_DTLS", "RFS_ID", rfsId).alias("tgt");
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
        logger.info("🚀 Fetch matched data ends at: {}", sparkService.formatMillis(currentTimeMillis() - localStartTime));

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
        
        //TODO-update logic function
        logger.info("🚀 Update process data ends: {}", sparkService.formatMillis(currentTimeMillis() - localStartTime));

        logger.info("Recon process completed in: {}", sparkService.formatMillis(currentTimeMillis() - processStartTime));
        List<ReconStatusCountProjection> reconStatusCountProjectionList = reconFileDtlsDao.getReconStatusCount(rfsId);
        reconFileSummaryDao.updateReconFileSummary(reconStatusCountProjectionList);

    }

CREATE TABLE RECON_FILE_DTLS(
    RFD_ID                RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
    RFS_ID                RAW(16),
    ROW_NUMBER            NUMBER,
    RECORD_TYPE           VARCHAR2(100),
    ATRN_NUM              VARCHAR2(30),
    PAYMENT_AMOUNT        NUMBER(20, 2),
    PAYMENT_DATE          DATE,
    BANK_REF_NUMBER       VARCHAR2(30),
    PAYMENT_STATUS        VARCHAR2(20),
    RECON_STATUS          VARCHAR2(20) DEFAULT 'PENDING' CHECK (RECON_STATUS IN ('PENDING','MATCHED','UNMATCHED','DUPLICATE')) ENABLE,
    SETTLEMENT_STATUS     VARCHAR2(20) DEFAULT 'PENDING' CHECK (SETTLEMENT_STATUS IN ('PENDING','SUCCESS','FAIL')) ENABLE,
    REMARK	              VARCHAR(500)
    );


CREATE INDEX RFD_RFS_ID_IDX ON RECON_FILE_DTLS (RFS_ID) ;
