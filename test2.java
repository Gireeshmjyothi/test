Dataset<Row> matchedAtrns = reconFileDataset.alias("recon")
    .join(transactionDataset.alias("txn"),
          col("recon.ATRN_NUM").equalTo(col("txn.ATRN_NUM"))
              .and(col("recon.DEBIT_AMT").equalTo(col("txn.DEBIT_AMT"))),
          "inner")
    .select(col("recon.ATRN_NUM").alias("MATCHED_ATRN"),
            col("recon.DEBIT_AMT").alias("MATCHED_DEBIT_AMT"),
            functions.row_number().over(Window.partitionBy("recon.ATRN_NUM").orderBy(/* your logic */)).alias("match_rank"));

Dataset<Row> withMatchedFlag = joined
    .join(matchedAtrns,
          col("recon.ATRN_NUM").equalTo(col("MATCHED_ATRN"))
              .and(col("recon.DEBIT_AMT").equalTo(col("MATCHED_DEBIT_AMT"))),
          "left_outer")
    .withColumn("isAtrnMatched", col("MATCHED_ATRN").isNotNull());
