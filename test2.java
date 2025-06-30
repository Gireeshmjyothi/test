Dataset<Row> matchedAtrns = joined
    .filter(col("exactMatch").equalTo(1))
    .select(col("recon.ATRN_NUM"), col("recon.DEBIT_AMT"), col("recon.RFD_ID"))
    .distinct()
    .withColumnRenamed("ATRN_NUM", "MATCHED_ATRN")
    .withColumnRenamed("DEBIT_AMT", "MATCHED_DEBIT_AMT")
    .withColumnRenamed("RFD_ID", "MATCHED_RFD_ID");


Dataset<Row> withMatchedFlag = joined
    .join(matchedAtrns,
        col("recon.ATRN_NUM").equalTo(col("MATCHED_ATRN"))
            .and(col("recon.DEBIT_AMT").equalTo(col("MATCHED_DEBIT_AMT")))
            .and(col("recon.RFD_ID").equalTo(col("MATCHED_RFD_ID"))),
        "left_outer")
    .withColumn("isAtrnMatched", col("MATCHED_ATRN").isNotNull());
