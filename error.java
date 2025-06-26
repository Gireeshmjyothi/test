private Dataset<Row>[] classifyReconData(Dataset<Row> reconFileDataset, Dataset<Row> transactionDataset) {
    logger.info("Starting classification of recon data...");

    // Step 0: Alias
    Dataset<Row> recon = reconFileDataset.alias("recon");
    Dataset<Row> txn = transactionDataset.alias("txn");

    // Step 1: Find matched records
    Dataset<Row> matched = recon
            .join(txn,
                    col("recon.ATRN_NUM").equalTo(col("txn.ATRN_NUM"))
                            .and(col("recon.DEBIT_AMT").equalTo(col("txn.DEBIT_AMT"))),
                    "inner")
            .select(
                    col("recon.rfdId"),
                    col("recon.ATRN_NUM"),
                    col("recon.DEBIT_AMT")
            )
            .withColumn("RECON_STATUS", lit("MATCHED"));

    // Step 2: Exclude matched rows from recon dataset
    Dataset<Row> unmatchedOrDuplicate = recon
            .join(matched,
                    recon.col("ATRN_NUM").equalTo(matched.col("ATRN_NUM"))
                            .and(recon.col("DEBIT_AMT").equalTo(matched.col("DEBIT_AMT")))
                            .and(recon.col("rfdId").equalTo(matched.col("rfdId"))),
                    "left_anti")
            .select(
                    col("recon.rfdId"),
                    col("recon.ATRN_NUM"),
                    col("recon.DEBIT_AMT")
            );

    // Step 3: Classify unmatched and duplicates using row_number
    WindowSpec windowSpec = Window.partitionBy("ATRN_NUM", "DEBIT_AMT").orderBy("DEBIT_AMT");

    Dataset<Row> unmatchedAndDuplicate = unmatchedOrDuplicate
            .withColumn("row_num", row_number().over(windowSpec))
            .withColumn("RECON_STATUS",
                    when(col("row_num").equalTo(1), lit("UNMATCHED"))
                            .otherwise(lit("DUPLICATE")))
            .drop("row_num");

    // Step 4: Combine all
    Dataset<Row> finalDataset = matched.unionByName(unmatchedAndDuplicate);

    // Step 5: Return split datasets
    return new Dataset[]{
            finalDataset.filter(col("RECON_STATUS").equalTo("MATCHED")),
            finalDataset.filter(col("RECON_STATUS").equalTo("DUPLICATE")),
            finalDataset.filter(col("RECON_STATUS").equalTo("UNMATCHED"))
    };
}
