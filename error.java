private Dataset<Row> normalize(Dataset<Row> dataset) {
    for (String col : columnMapping().keySet()) {
        if (Arrays.asList(dataset.columns()).contains(col)) {
            dataset = dataset.withColumn(col + "_NORMALIZED", functions.upper(functions.trim(dataset.col(col))));
        }
    }

    if (Arrays.asList(dataset.columns()).contains("ATRN_NUM")) {
        dataset = dataset.withColumn("ATRN_NUM_NORMALIZED", functions.upper(functions.trim(dataset.col("ATRN_NUM"))));
    }

    return dataset;
}

Column joinCond = reconFileDtls.col("ATRN_NUM_NORMALIZED").equalTo(merchantOrderPayments.col("ATRN_NUM_NORMALIZED"))
    .and(reconFileDtls.col("DEBIT_AMT").equalTo(merchantOrderPayments.col("DEBIT_AMT")));

for (String col : columnMapping().keySet()) {
    valueMatch = valueMatch.and(
        reconFileDtls.col(col + "_NORMALIZED").equalTo(merchantOrderPayments.col(col + "_NORMALIZED"))
    );
}
