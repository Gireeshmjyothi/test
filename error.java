// Step 2: Mark matched rows
Dataset<Row> matched = reconWithTxMatch
        .filter(col("isMatched").equalTo(true))
        .drop("isMatched")
        // Select only columns from recon side and rename to avoid ambiguity
        .select(
                reconFileDataset.col(ATRN_NUM).alias(ATRN_NUM),
                reconFileDataset.col(DEBIT_AMT).alias(DEBIT_AMT)
        )
        .withColumn(RECON_STATUS_FIELD, lit(RECON_STATUS_MATCHED));

// Step 3: Identify unmatched/duplicate (where atrnNum exists in transaction, but debitAmt differs OR not in transaction)
Dataset<Row> remaining = reconFileDataset
        .join(matched.select(ATRN_NUM, DEBIT_AMT),
                reconFileDataset.col(ATRN_NUM).equalTo(col(ATRN_NUM))
                        .and(reconFileDataset.col(DEBIT_AMT).equalTo(col(DEBIT_AMT))),
                "leftanti");
