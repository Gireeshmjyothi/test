private Dataset<Row>[] classifyReconData(Dataset<Row> reconFileDataset, Dataset<Row> transactionDataset) {
    logger.info("Starting classification of recon data...");

    // Step 0: Add aliases to both datasets
    Dataset<Row> recon = reconFileDataset.alias("recon");
    Dataset<Row> txn = transactionDataset.alias("txn");

    // Step 1: Join recon and transaction on atrnNum and debitAmt to find exact matches
    Dataset<Row> reconWithTxMatch = recon
            .join(txn,
                    recon.col(ATRN_NUM).equalTo(txn.col(ATRN_NUM))
                            .and(recon.col(DEBIT_AMT).equalTo(txn.col(DEBIT_AMT))),
                    "left"
            )
            .withColumn("isMatched", txn.col(ATRN_NUM).isNotNull());

    // Step 2: Mark matched rows (we only keep recon columns to avoid duplication)
    Dataset<Row> matched = reconWithTxMatch
            .filter(col("isMatched").equalTo(true))
            .drop("isMatched")
            .select("recon.*")
            .withColumn(RECON_STATUS_FIELD, lit(RECON_STATUS_MATCHED));

    // Step 3: Identify unmatched/duplicate (where atrnNum exists in transaction, but debitAmt differs OR not in transaction)
    Dataset<Row> remaining = recon
            .join(
                    matched.select(col(ATRN_NUM), col(DEBIT_AMT)),
                    recon.col(ATRN_NUM).equalTo(matched.col(ATRN_NUM))
                            .and(recon.col(DEBIT_AMT).equalTo(matched.col(DEBIT_AMT))),
                    "leftanti"
            );

    // Step 4: Apply row_number partitioned by atrnNum to tag first occurrence as unmatched, rest as duplicates
    WindowSpec dupWindow = Window.partitionBy(recon.col(ATRN_NUM)).orderBy(recon.col(DEBIT_AMT));

    Dataset<Row> remainingWithFlags = remaining
            .withColumn("row_num", row_number().over(dupWindow))
            .withColumn(RECON_STATUS_FIELD,
                    when(col("row_num").equalTo(1), lit(RECON_STATUS_UNMATCHED))
                            .otherwise(lit(RECON_STATUS_DUPLICATE))
            )
            .drop("row_num");

    // Step 5: Combine matched + remainingWithFlags
    Dataset<Row> finalDataset = matched.unionByName(remainingWithFlags);

    // Return matched, duplicate, unmatched
    return new Dataset[]{
            finalDataset.filter(col(RECON_STATUS_FIELD).equalTo(RECON_STATUS_MATCHED)),
            finalDataset.filter(col(RECON_STATUS_FIELD).equalTo(RECON_STATUS_DUPLICATE)),
            finalDataset.filter(col(RECON_STATUS_FIELD).equalTo(RECON_STATUS_UNMATCHED))
    };
}
