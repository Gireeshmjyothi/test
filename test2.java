private Dataset<Row>[] classifyReconData(Dataset<Row> reconFileDataset, Dataset<Row> transactionDataset) {
    logger.info("Starting classification of recon data...");

    // Step 0: Aliasing for clarity
    Dataset<Row> recon = reconFileDataset.alias("recon");
    Dataset<Row> txn = transactionDataset.alias("txn");

    // Step 1: Window spec for match ranking (optional)
    WindowSpec matchWindow = Window
            .partitionBy("recon.ATRN_NUM", "recon.DEBIT_AMT")
            .orderBy(col("recon.RFD_ID"));  // or any stable ordering

    // Step 2: Join on ATRN_NUM and DEBIT_AMT to find exact matches
    Dataset<Row> joined = recon
            .join(txn,
                    col("recon.ATRN_NUM").equalTo(col("txn.ATRN_NUM"))
                            .and(col("recon.DEBIT_AMT").equalTo(col("txn.DEBIT_AMT"))),
                    "left_outer")
            .withColumn("exactMatch", when(col("txn.ATRN_NUM").isNotNull(), lit(1)).otherwise(lit(0)))
            .withColumn("match_rank", when(col("exactMatch").equalTo(1), row_number().over(matchWindow)));

    // Step 3: Collect all matched ATRN_NUMs (not just match_rank = 1)
    Dataset<Row> matchedAtrns = joined
            .filter(col("exactMatch").equalTo(1))
            .select(col("recon.ATRN_NUM").alias("MATCHED_ATRN"))
            .distinct();

    // Step 4: Tagging matched status using ATRN_NUM presence and match_rank
    Dataset<Row> withMatchedFlag = joined
            .join(matchedAtrns, col("recon.ATRN_NUM").equalTo(col("MATCHED_ATRN")), "left_outer")
            .withColumn("isAtrnMatched", col("MATCHED_ATRN").isNotNull());

    // Step 5: Final status assignment
    Dataset<Row> finalStatus = withMatchedFlag.withColumn("RECON_STATUS",
            when(col("exactMatch").equalTo(1).and(col("match_rank").equalTo(1)), lit("MATCHED"))
            .when(col("exactMatch").equalTo(1).and(col("match_rank").gt(1)), lit("DUPLICATE"))
            .when(col("exactMatch").equalTo(0).and(col("isAtrnMatched").equalTo(true)), lit("DUPLICATE"))
            .otherwise(lit("UNMATCHED"))
    );

    // Step 6: Return datasets
    return new Dataset[]{
            finalStatus.filter(col("RECON_STATUS").equalTo("MATCHED")),
            finalStatus.filter(col("RECON_STATUS").equalTo("DUPLICATE")),
            finalStatus.filter(col("RECON_STATUS").equalTo("UNMATCHED"))
    };
}
