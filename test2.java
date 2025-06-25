private Dataset<Row>[] classifyReconData(Dataset<Row> reconFileDataset, Dataset<Row> transactionDataset) {
    logger.info("Starting classification of recon data...");

    // Step 1: Join recon and transaction on atrnNum and debitAmt to find exact matches
    Dataset<Row> reconWithTxMatch = reconFileDataset
            .join(transactionDataset,
                    reconFileDataset.col(ATRN_NUM).equalTo(transactionDataset.col(ATRN_NUM))
                            .and(reconFileDataset.col(DEBIT_AMT).equalTo(transactionDataset.col(DEBIT_AMT))),
                    "left"
            )
            .withColumn("isMatched", transactionDataset.col(ATRN_NUM).isNotNull());

    // Step 2: Mark matched rows
    Dataset<Row> matched = reconWithTxMatch
            .filter(col("isMatched").equalTo(true))
            .drop("isMatched")
            .withColumn(RECON_STATUS_FIELD, lit(RECON_STATUS_MATCHED));

    // Step 3: Identify unmatched/duplicate (where atrnNum exists in transaction, but debitAmt differs OR not in transaction)
    Dataset<Row> remaining = reconFileDataset
            .join(matched.select(ATRN_NUM, DEBIT_AMT), 
                  reconFileDataset.col(ATRN_NUM).equalTo(matched.col(ATRN_NUM))
                  .and(reconFileDataset.col(DEBIT_AMT).equalTo(matched.col(DEBIT_AMT))),
                  "leftanti");

    // Step 4: Apply row_number partitioned by atrnNum to tag first occurrence as unmatched, rest as duplicates
    WindowSpec dupWindow = Window.partitionBy(ATRN_NUM).orderBy(DEBIT_AMT); // can use any column to order

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
