/**
     * This method is used to process and find the matched, unmatched and duplicate data from recon file
     * with respect to transaction data.
     * @param rfsId recon file summary ID (UUID).
     */
    public void reconProcess(UUID rfsId) {
        try {

            logger.info("Recon process started.");
            long processStartTime = currentTimeMillis();
            logger.info("🚀 Recon process started at : {} ", sparkService.formatMillis(processStartTime));

            // Step-1 Fetching transaction data.
            logger.info("Fetch transaction data.");
            Dataset<Row> transactionDataset = readAndNormalize();

            // Step-2 Fetching recon file details.
            logger.info("Fetch recon file details");
            Dataset<Row> reconFileDataset = readAndNormalize(rfsId);

            // Step-3 Rename recon file details columns with respect to transaction columns.
            reconFileDataset = renameColumns(reconFileDataset).alias("recon");

            // Step-4 Classifying recon data.
            Dataset<Row>[] result = classifyReconData(reconFileDataset, transactionDataset);

            // Step-5 Mapping recon status for classified recon data.
            Dataset<Row> matchedWithStatus = result[0].withColumn(RECON_STATUS, lit(RECON_STATUS_MATCHED));
            Dataset<Row> duplicateWithStatus = result[1].withColumn(RECON_STATUS, lit(RECON_STATUS_DUPLICATE));
            Dataset<Row> unmatchedWithStatus = result[2].withColumn(RECON_STATUS, lit(RECON_STATUS_UNMATCHED));

            // Step-6 Preparing final recon status update for staging table.
            Dataset<Row> finalReconStatusUpdate = matchedWithStatus
                    .select(RFD_ID, RECON_STATUS)
                    .union(unmatchedWithStatus.select(RFD_ID, RECON_STATUS))
                    .union(duplicateWithStatus.select(RFD_ID, RECON_STATUS));

            // Step-7 Writing recon status to staging table.
            jdbcReaderService.writeToStagingTable(finalReconStatusUpdate, RECON_STATUS_STAGE);

            // Step-8 Updating recon status of recon_file_dtls
            jdbcReaderService.updateReconStatusFromStage();

            // Step-9 Clear stage table.
            jdbcReaderService.clearStageTable();

            // Step-10 Fetching recon file summary with help of rfd_id.
            List<ReconStatusCountProjection> reconStatusCountProjectionList = reconFileDtlsDao.getReconStatusCount(rfsId);

            // Step-11 Updating final result of recon file summary.
            reconFileSummaryDao.updateReconFileSummary(reconStatusCountProjectionList, rfsId);
            logger.info("🚀 Recon process completed in: {}", sparkService.formatMillis(currentTimeMillis() - processStartTime));
        } catch (Exception ex) {
            logger.error("Error while processing recon : {}", ex.getMessage());
        }

    }

    /**
     * This method is used to find the matched, unmatched and duplicate data.
     * @param reconFileDataset recon file dtls dataset.
     * @param transactionDataset transaction dataset.
     * @return Array of matched, unmatched and duplicate data set.
     */
    public Dataset<Row>[] classifyReconData(Dataset<Row> reconFileDataset, Dataset<Row> transactionDataset) {

        // Step-1 Create match window by atrnNum + debitAmt.
        WindowSpec matchWindow = Window
                .partitionBy("recon." + ATRN_NUM, "recon." + DEBIT_AMT)
                .orderBy(RFD_ID);

        // Step-2 Join recon with merchant on exact match of atrnNum + debitAmt.
        Dataset<Row> joined = reconFileDataset
                .join(transactionDataset,
                        reconFileDataset.col("recon." + ATRN_NUM).equalTo(transactionDataset.col(ATRN_NUM))
                                .and(reconFileDataset.col("recon." + DEBIT_AMT).equalTo(transactionDataset.col(DEBIT_AMT))),
                        "left_outer"
                )
                .withColumn("exactMatch", when(transactionDataset.col(ATRN_NUM).isNotNull(), lit(1)).otherwise(lit(0)))
                .withColumn("match_rank", when(col("exactMatch").equalTo(1), row_number().over(matchWindow)));

        // Step-3 Find atrnNums that are matched at least once.
        Dataset<Row> matchedAtrns = joined
                .filter(col("match_rank").equalTo(1))
                .select("recon." + ATRN_NUM)
                .distinct()
                .withColumnRenamed(ATRN_NUM, MATCHED_ATRN);

        // Step-4 Join back to tag isAtrnMatched.
        Dataset<Row> withMatchedFlag = joined
                .join(matchedAtrns, joined.col("recon." + ATRN_NUM).equalTo(matchedAtrns.col(MATCHED_ATRN)), "left_outer")
                .withColumn("isAtrnMatched", col(MATCHED_ATRN).isNotNull());

        // Step-5. Apply logic for status assignment.
        Dataset<Row> finalStatus = withMatchedFlag.withColumn(RECON_STATUS_FIELD,
                when(col("exactMatch").equalTo(1).and(col("match_rank").equalTo(1)), lit(RECON_STATUS_MATCHED))
                        .when(col("exactMatch").equalTo(1).and(col("match_rank").gt(1)), lit(RECON_STATUS_DUPLICATE))
                        .when(col("exactMatch").equalTo(0).and(col("isAtrnMatched").equalTo(true)), lit(RECON_STATUS_DUPLICATE))
                        .otherwise(lit(RECON_STATUS_UNMATCHED))
        );

        // Step-6 Return all three dataset as array.
        return new Dataset[]{finalStatus.filter(col(RECON_STATUS_FIELD).equalTo(RECON_STATUS_MATCHED)),
                finalStatus.filter(col(RECON_STATUS_FIELD).equalTo(RECON_STATUS_DUPLICATE)),
                finalStatus.filter(col(RECON_STATUS_FIELD).equalTo(RECON_STATUS_UNMATCHED))}; //matchedFinal, duplicate, unmatched
    }

    private Dataset<Row> readAndNormalize() {
        logger.info("Executing table : {}", MERCHANT_ORDER_PAYMENTS);
        return normalize(jdbcReaderService.readFromDBWithFilter(MERCHANT_ORDER_PAYMENTS));
    }


    private Dataset<Row> readAndNormalize(UUID value) {
        String query = buildQuery(RECON_FILE_DTLS, RFS_ID, value);
        logger.info("Executing query for table '{}': {}", RECON_FILE_DTLS, query);
        return normalize(jdbcReaderService.readFromDBWithFilter(RECON_FILE_DTLS));
    }

    private Dataset<Row> normalize(Dataset<Row> dataset) {
        for (String col : columnMapping().keySet()) {
            if (Arrays.asList(dataset.columns()).contains(col)) {
                dataset = dataset.withColumn(col, functions.trim(dataset.col(col)));
            }
        }
        return dataset;
    }

    private Dataset<Row> renameColumns(Dataset<Row> dataset) {
        for (Map.Entry<String, String> entry : columnMapping().entrySet()) {
            if (Arrays.asList(dataset.columns()).contains(entry.getValue())) {
                dataset = dataset.withColumnRenamed(entry.getValue(), entry.getKey());
            }
        }
        return dataset;
    }

    private Map<String, String> columnMapping() {
        Map<String, String> columnMappings = new HashMap<>();
        columnMappings.put(DEBIT_AMT, PAYMENT_AMOUNT);
        return columnMappings;
    }
