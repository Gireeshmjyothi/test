/**
     * This method is used to find the matched, unmatched and duplicate data.
     *
     * @param reconFileDataset   recon file dtls dataset.
     * @param transactionDataset transaction dataset.
     * @return Array of matched, unmatched and duplicate data set.
     */
    private Dataset<Row>[] classifyReconData(Dataset<Row> reconFileDataset, Dataset<Row> transactionDataset) {
        logger.info("Starting classification of recon data...");

        WindowSpec matchWindow = Window
                .partitionBy("recon." + ATRN_NUM, "recon." + DEBIT_AMT)
                .orderBy(RFD_ID);

        logger.info("Performing join between recon and transaction data.");
        Dataset<Row> joined = reconFileDataset
                .join(transactionDataset,
                        reconFileDataset.col("recon." + ATRN_NUM).equalTo(transactionDataset.col(ATRN_NUM))
                                .and(reconFileDataset.col("recon." + DEBIT_AMT).equalTo(transactionDataset.col(DEBIT_AMT))),
                        "left_outer"
                )
                .withColumn("exactMatch", when(transactionDataset.col(ATRN_NUM).isNotNull(), lit(1)).otherwise(lit(0)))
                .withColumn("match_rank", when(col("exactMatch").equalTo(1), row_number().over(matchWindow)));

        logger.info("Identifying matched atrn numbers.");
        Dataset<Row> matchedAtrns = joined
                .filter(col("match_rank").equalTo(1))
                .select("recon." + ATRN_NUM)
                .distinct()
                .withColumnRenamed(ATRN_NUM, MATCHED_ATRN);

        logger.info("Tagging matched status.");
        Dataset<Row> withMatchedFlag = joined
                .join(matchedAtrns, joined.col("recon." + ATRN_NUM).equalTo(matchedAtrns.col(MATCHED_ATRN)), "left_outer")
                .withColumn("isAtrnMatched", col(MATCHED_ATRN).isNotNull());

        logger.info("Applying classification logic to tag rows with status.");
        Dataset<Row> finalStatus = withMatchedFlag.withColumn(RECON_STATUS_FIELD,
                when(col("exactMatch").equalTo(1).and(col("match_rank").equalTo(1)), lit(RECON_STATUS_MATCHED))
                        .when(col("exactMatch").equalTo(1).and(col("match_rank").gt(1)), lit(RECON_STATUS_DUPLICATE))
                        .when(col("exactMatch").equalTo(0).and(col("isAtrnMatched").equalTo(true)), lit(RECON_STATUS_DUPLICATE))
                        .otherwise(lit(RECON_STATUS_UNMATCHED))
        );

        logger.info("Returning classified datasets: matched, duplicate, unmatched.");
        return new Dataset[]{
                finalStatus.filter(col(RECON_STATUS_FIELD).equalTo(RECON_STATUS_MATCHED)),
                finalStatus.filter(col(RECON_STATUS_FIELD).equalTo(RECON_STATUS_DUPLICATE)),
                finalStatus.filter(col(RECON_STATUS_FIELD).equalTo(RECON_STATUS_UNMATCHED))
        };//matched, duplicate, unmatched
    }
