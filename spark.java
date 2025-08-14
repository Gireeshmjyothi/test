private Dataset<Row> classifyReconRecords(Dataset<Row> transactionDataset,
                                          Dataset<Row> reconFileDataset,
                                          UUID reconFileId) {

    final String reconFieldFormat = "recon.%s";
    final String txnFieldFormat = "txn.%s";

    Dataset<Row> recon = reconFileDataset
            .withColumn("ROW_NUMBER", monotonically_increasing_id())
            .alias("recon");

    Dataset<Row> txn = transactionDataset.alias("txn");

    // Window to tag first match per ATRN_NUM + DEBIT_AMT
    WindowSpec matchWindow = Window
            .partitionBy(col(String.format(reconFieldFormat, ATRN_NUM)))
            .orderBy(col("ROW_NUMBER"));

    // Join recon and txn on ATRN_NUM
    Dataset<Row> joined = recon.join(txn,
            col(String.format(reconFieldFormat, ATRN_NUM))
                    .equalTo(col(String.format(txnFieldFormat, ATRN_NUM))),
            "left_outer")
            .withColumn("EXACT_MATCH",
                    when(col(String.format(txnFieldFormat, ATRN_NUM)).isNotNull()
                            .and(col(String.format(reconFieldFormat, DEBIT_AMT))
                                    .equalTo(col(String.format(txnFieldFormat, TXN_AMOUNT)))), lit(1))
                    .otherwise(lit(0)))
            .withColumn("MATCH_RANK",
                    when(col("EXACT_MATCH").equalTo(1), row_number().over(matchWindow)))
            // Always take merchantId from txn if ATRN exists
            .withColumn(MERCHANT_ID, col(String.format(txnFieldFormat, MERCHANT_ID)));

    // Identify matched ATRN_NUM
    Dataset<Row> matchedAtrns = joined.filter(col("MATCH_RANK").equalTo(1))
            .select(col(String.format(reconFieldFormat, ATRN_NUM)).alias("MATCHED_ATRN"))
            .distinct();

    // Flag ATRN matched but possibly debit mismatch
    Dataset<Row> withMatchedFlag = joined.join(matchedAtrns,
                    col(String.format(reconFieldFormat, ATRN_NUM)).equalTo(col("MATCHED_ATRN")),
                    "left_outer")
            .withColumn("IS_ATRN_MATCHED", col("MATCHED_ATRN").isNotNull());

    // Apply classification logic
    Dataset<Row> finalStatus = withMatchedFlag.withColumn(RECON_STATUS,
            when(col("EXACT_MATCH").equalTo(1).and(col("MATCH_RANK").equalTo(1)), lit(RECON_STATUS_MATCHED))
            .when(col("EXACT_MATCH").equalTo(1).and(col("MATCH_RANK").gt(1)), lit(RECON_STATUS_DUPLICATE))
            .when(col("EXACT_MATCH").equalTo(0).and(col("IS_ATRN_MATCHED").equalTo(true)), lit(RECON_STATUS_DUPLICATE))
            .otherwise(lit(RECON_STATUS_UNMATCHED))
    );

    // Add remark column
    finalStatus = finalStatus.withColumn("REMARK",
            when(col(RECON_STATUS).equalTo(RECON_STATUS_UNMATCHED)
                    .and(col(String.format(txnFieldFormat, ATRN_NUM)).isNull()), "ATRN missing")
            .when(col(RECON_STATUS).equalTo(RECON_STATUS_UNMATCHED)
                    .and(col(String.format(txnFieldFormat, ATRN_NUM)).isNotNull()
                            .and(col(String.format(reconFieldFormat, DEBIT_AMT))
                                    .notEqual(col(String.format(txnFieldFormat, TXN_AMOUNT))))), "Debit amount mismatch")
            .otherwise(lit(null))
    );

    // Add RF_ID, CREATED_DATE, UPDATED_DATE
    finalStatus = finalStatus
            .withColumn(RF_ID, lit(reconFileId.toString().replace("-", "")))
            .withColumn("CREATED_DATE", unix_timestamp(current_timestamp()).cast(DataTypes.LongType)
                    .multiply(1000).plus(monotonically_increasing_id().mod(1000)))
            .withColumn("UPDATED_DATE", unix_timestamp(current_timestamp()).cast(DataTypes.LongType)
                    .multiply(1000).plus(monotonically_increasing_id().mod(1000)));

    // Select final columns
    Dataset<Row> result = finalStatus.select(
            col(String.format(reconFieldFormat, ATRN_NUM)),
            col(String.format(reconFieldFormat, DEBIT_AMT)),
            col(RECON_STATUS),
            col(RF_ID),
            col(MERCHANT_ID),
            col("ROW_NUMBER"),
            col("REMARK"),
            col("CREATED_DATE"),
            col("UPDATED_DATE")
    );

    return result;
}
