private Dataset<Row>[] classifyReconData(Dataset<Row> reconFileDataset, Dataset<Row> transactionDataset) {
    logger.info("Starting classification of recon data...");

    // Step 0: Alias for readability
    Dataset<Row> recon = reconFileDataset.alias("recon");
    Dataset<Row> txn = transactionDataset.alias("txn");

    // Step 1: Find exact matches
    Dataset<Row> matched = recon
            .join(txn,
                    col("recon.ATRN_NUM").equalTo(col("txn.ATRN_NUM"))
                            .and(col("recon.DEBIT_AMT").equalTo(col("txn.DEBIT_AMT"))),
                    "inner")
            .select(col("recon.ATRN_NUM"), col("recon.DEBIT_AMT"))
            .withColumn("RECON_STATUS", lit("MATCHED"));

    // Step 2: Exclude matched rows from recon dataset
    Dataset<Row> unmatchedOrDuplicate = recon
            .join(matched,
                    recon.col("ATRN_NUM").equalTo(matched.col("ATRN_NUM"))
                            .and(recon.col("DEBIT_AMT").equalTo(matched.col("DEBIT_AMT"))),
                    "left_anti")
            .select(recon.col("ATRN_NUM"), recon.col("DEBIT_AMT"));

    // Step 3: Use row_number to classify unmatched and duplicates
    WindowSpec windowSpec = Window.partitionBy("ATRN_NUM", "DEBIT_AMT").orderBy("DEBIT_AMT");

    Dataset<Row> unmatchedAndDuplicate = unmatchedOrDuplicate
            .withColumn("row_num", row_number().over(windowSpec))
            .withColumn("RECON_STATUS",
                    when(col("row_num").equalTo(1), lit("UNMATCHED"))
                            .otherwise(lit("DUPLICATE")))
            .drop("row_num");

    // Step 4: Union all
    Dataset<Row> finalDataset = matched.unionByName(unmatchedAndDuplicate);

    // Step 5: Return split datasets
    return new Dataset[]{
            finalDataset.filter(col("RECON_STATUS").equalTo("MATCHED")),
            finalDataset.filter(col("RECON_STATUS").equalTo("DUPLICATE")),
            finalDataset.filter(col("RECON_STATUS").equalTo("UNMATCHED"))
    };
}
