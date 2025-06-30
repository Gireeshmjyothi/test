private Dataset<Row>[] classifyReconData(Dataset<Row> reconFileDataset, Dataset<Row> transactionDataset) {
    logger.info("Starting classification of recon data...");

    // Step 0: Apply aliasing
    Dataset<Row> recon = reconFileDataset.alias("recon");
    Dataset<Row> txn = transactionDataset.alias("txn");

    // Step 1: Perform left_outer join on ATRN_NUM + DEBIT_AMT
    Dataset<Row> joined = recon
        .join(txn,
            col("recon.ATRN_NUM").equalTo(col("txn.ATRN_NUM"))
                .and(col("recon.DEBIT_AMT").equalTo(col("txn.DEBIT_AMT"))),
            "left_outer")
        .withColumn("exactMatch", when(col("txn.ATRN_NUM").isNotNull(), lit(1)).otherwise(lit(0)));

    // Step 2: Apply row_number to tag first match per (ATRN_NUM, DEBIT_AMT, RFD_ID)
    WindowSpec matchWindow = Window
        .partitionBy(col("recon.ATRN_NUM"), col("recon.DEBIT_AMT"), col("recon.RFD_ID"))
        .orderBy(col("recon.RFD_ID")); // can use any ordering

    joined = joined.withColumn("match_rank", when(col("exactMatch").equalTo(1),
            row_number().over(matchWindow)));

    // Step 3: Find distinct matched ATRN_NUM
    Dataset<Row> matchedAtrns = joined
        .filter(col("match_rank").equalTo(1))
        .select(col("recon.ATRN_NUM").alias("MATCHED_ATRN"))
        .distinct();

    // Step 4: Re-join matched ATRN_NUM to mark duplicates (same ATRN_NUM, different DEBIT_AMT)
    Dataset<Row> withMatchedFlag = joined
        .join(matchedAtrns,
            col("recon.ATRN_NUM").equalTo(col("MATCHED_ATRN")),
            "left_outer")
        .withColumn("isAtrnMatched", col("MATCHED_ATRN").isNotNull());

    // Step 5: Apply classification logic
    Dataset<Row> finalStatus = withMatchedFlag.withColumn("RECON_STATUS",
        when(col("exactMatch").equalTo(1).and(col("match_rank").equalTo(1)), lit("MATCHED"))
        .when(col("exactMatch").equalTo(1).and(col("match_rank").gt(1)), lit("DUPLICATE"))
        .when(col("exactMatch").equalTo(0).and(col("isAtrnMatched").equalTo(true)), lit("DUPLICATE"))
        .otherwise(lit("UNMATCHED"))
    );

    // Optional: filter and return 3 datasets
    return new Dataset[]{
        finalStatus.filter(col("RECON_STATUS").equalTo("MATCHED")),
        finalStatus.filter(col("RECON_STATUS").equalTo("DUPLICATE")),
        finalStatus.filter(col("RECON_STATUS").equalTo("UNMATCHED"))
    };
}
