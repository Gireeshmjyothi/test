private Dataset<Row>[] classifyReconData(Dataset<Row> reconFileDataset, Dataset<Row> transactionDataset) {
    logger.info("Starting classification of recon data...");

    // Apply aliases
    Dataset<Row> recon = reconFileDataset.alias("recon");
    Dataset<Row> txn = transactionDataset.alias("txn");

    // Step 1: Find matched records (exact match on ATRN_NUM + DEBIT_AMT)
    Dataset<Row> matched = recon
            .join(txn,
                    col("recon.ATRN_NUM").equalTo(col("txn.ATRN_NUM"))
                            .and(col("recon.DEBIT_AMT").equalTo(col("txn.DEBIT_AMT"))),
                    "inner")
            .select(col("recon.ATRN_NUM"), col("recon.DEBIT_AMT"))  // Avoid carrying txn columns
            .withColumn("RECON_STATUS", lit("MATCHED"))
            .alias("matched");

    // Step 2: Get unmatched + duplicate candidates by excluding matched rows
    Dataset<Row> unmatchedOrDuplicate = recon
            .join(matched,
                    col("recon.ATRN_NUM").equalTo(col("matched.ATRN_NUM"))
                            .and(col("recon.DEBIT_AMT").equalTo(col("matched.DEBIT_AMT"))),
                    "left_anti");

    // Step 3: Use row_number to mark first unmatched, others as duplicates
    WindowSpec windowSpec = Window.partitionBy("ATRN_NUM").orderBy("DEBIT_AMT");

    Dataset<Row> unmatchedAndDuplicateLabeled = unmatchedOrDuplicate
            .withColumn("row_num", row_number().over(windowSpec))
            .withColumn("RECON_STATUS", when(col("row_num").equalTo(1), lit("UNMATCHED"))
                    .otherwise(lit("DUPLICATE")))
            .drop("row_num");

    // Step 4: Combine final results
    Dataset<Row> finalDataset = matched.unionByName(unmatchedAndDuplicateLabeled);

    // Step 5: Return split datasets
    return new Dataset[]{
            finalDataset.filter(col("RECON_STATUS").equalTo("MATCHED")),
            finalDataset.filter(col("RECON_STATUS").equalTo("DUPLICATE")),
            finalDataset.filter(col("RECON_STATUS").equalTo("UNMATCHED"))
    };
}
