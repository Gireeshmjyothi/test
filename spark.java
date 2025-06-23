/**
 * This method is used to process and find the matched, unmatched and duplicate data from recon file
 * with respect to transaction data.
 * @param rfsId recon file summary ID (UUID).
 */
public void reconProcess(UUID rfsId) {
    long processStartTime = currentTimeMillis();
    logger.info("🚀 Recon process started at: {}", sparkService.formatMillis(processStartTime));

    try {
        // Step-1: Fetch transaction data.
        logger.info("📥 Step-1: Fetching transaction data from table: {}", MERCHANT_ORDER_PAYMENTS);
        Dataset<Row> transactionDataset = readAndNormalize();

        // Step-2: Fetch recon file details using rfsId.
        logger.info("📥 Step-2: Fetching recon file details for rfsId: {}", rfsId);
        Dataset<Row> reconFileDataset = readAndNormalize(rfsId);

        // Step-3: Rename recon file details columns for alignment with transaction data.
        logger.info("🔄 Step-3: Renaming recon file columns for standardization.");
        reconFileDataset = renameColumns(reconFileDataset).alias("recon");

        // Step-4: Classify the data into matched, duplicate, and unmatched.
        logger.info("🔍 Step-4: Classifying recon data.");
        Dataset<Row>[] classifiedData = classifyReconData(reconFileDataset, transactionDataset);

        // Step-5: Add status column to each dataset.
        logger.info("🧩 Step-5: Tagging classified datasets with respective statuses.");
        Dataset<Row> matched = addStatus(classifiedData[0], RECON_STATUS_MATCHED);
        Dataset<Row> duplicate = addStatus(classifiedData[1], RECON_STATUS_DUPLICATE);
        Dataset<Row> unmatched = addStatus(classifiedData[2], RECON_STATUS_UNMATCHED);

        // Step-6: Prepare final dataset for staging update.
        logger.info("🗃️ Step-6: Merging all status-tagged datasets for staging.");
        Dataset<Row> finalReconStatusUpdate = matched
                .select(RFD_ID, RECON_STATUS)
                .union(unmatched.select(RFD_ID, RECON_STATUS))
                .union(duplicate.select(RFD_ID, RECON_STATUS));

        // Step-7: Write to staging table.
        logger.info("💾 Step-7: Writing recon status to staging table '{}'.", RECON_STATUS_STAGE);
        jdbcReaderService.writeToStagingTable(finalReconStatusUpdate, RECON_STATUS_STAGE);

        // Step-8: Update final status in recon_file_dtls table.
        logger.info("✅ Step-8: Updating recon status in main recon_file_dtls table.");
        jdbcReaderService.updateReconStatusFromStage();

        // Step-9: Clear staging table.
        logger.info("🧹 Step-9: Clearing staging table after update.");
        jdbcReaderService.clearStageTable();

        // Step-10: Fetch status count.
        logger.info("📊 Step-10: Fetching recon status count summary for rfsId: {}", rfsId);
        List<ReconStatusCountProjection> statusSummary = reconFileDtlsDao.getReconStatusCount(rfsId);

        // Step-11: Update recon file summary.
        logger.info("📝 Step-11: Updating recon file summary table with status summary.");
        reconFileSummaryDao.updateReconFileSummary(statusSummary, rfsId);

        logger.info("✅ Recon process completed in: {}", sparkService.formatMillis(currentTimeMillis() - processStartTime));

    } catch (Exception ex) {
        logger.error("❌ Error during recon process for rfsId {}: {}", rfsId, ex.getMessage(), ex);
    }
}

private Dataset<Row> addStatus(Dataset<Row> dataset, String status) {
    return dataset.withColumn(RECON_STATUS, lit(status));
}

/**
 * This method is used to find the matched, unmatched and duplicate data.
 * @param reconFileDataset recon file dtls dataset.
 * @param transactionDataset transaction dataset.
 * @return Array of matched, unmatched and duplicate data set.
 */
public Dataset<Row>[] classifyReconData(Dataset<Row> reconFileDataset, Dataset<Row> transactionDataset) {
    logger.info("🔍 Starting classification of recon data...");

    WindowSpec matchWindow = Window
            .partitionBy("recon." + ATRN_NUM, "recon." + DEBIT_AMT)
            .orderBy(RFD_ID);

    logger.info("🔗 Performing join between recon and transaction data.");
    Dataset<Row> joined = reconFileDataset
            .join(transactionDataset,
                    reconFileDataset.col("recon." + ATRN_NUM).equalTo(transactionDataset.col(ATRN_NUM))
                            .and(reconFileDataset.col("recon." + DEBIT_AMT).equalTo(transactionDataset.col(DEBIT_AMT))),
                    "left_outer"
            )
            .withColumn("exactMatch", when(transactionDataset.col(ATRN_NUM).isNotNull(), lit(1)).otherwise(lit(0)))
            .withColumn("match_rank", when(col("exactMatch").equalTo(1), row_number().over(matchWindow)));

    logger.info("📌 Identifying matched atrn numbers.");
    Dataset<Row> matchedAtrns = joined
            .filter(col("match_rank").equalTo(1))
            .select("recon." + ATRN_NUM)
            .distinct()
            .withColumnRenamed(ATRN_NUM, MATCHED_ATRN);

    logger.info("🏷️ Tagging matched status.");
    Dataset<Row> withMatchedFlag = joined
            .join(matchedAtrns, joined.col("recon." + ATRN_NUM).equalTo(matchedAtrns.col(MATCHED_ATRN)), "left_outer")
            .withColumn("isAtrnMatched", col(MATCHED_ATRN).isNotNull());

    logger.info("🧠 Applying classification logic to tag rows with status.");
    Dataset<Row> finalStatus = withMatchedFlag.withColumn(RECON_STATUS_FIELD,
            when(col("exactMatch").equalTo(1).and(col("match_rank").equalTo(1)), lit(RECON_STATUS_MATCHED))
                    .when(col("exactMatch").equalTo(1).and(col("match_rank").gt(1)), lit(RECON_STATUS_DUPLICATE))
                    .when(col("exactMatch").equalTo(0).and(col("isAtrnMatched").equalTo(true)), lit(RECON_STATUS_DUPLICATE))
                    .otherwise(lit(RECON_STATUS_UNMATCHED))
    );

    logger.info("📤 Returning classified datasets: matched, duplicate, unmatched.");
    return new Dataset[]{
            finalStatus.filter(col(RECON_STATUS_FIELD).equalTo(RECON_STATUS_MATCHED)),
            finalStatus.filter(col(RECON_STATUS_FIELD).equalTo(RECON_STATUS_DUPLICATE)),
            finalStatus.filter(col(RECON_STATUS_FIELD).equalTo(RECON_STATUS_UNMATCHED))
    };
                                                    }
