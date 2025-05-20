// 1. Normalize both datasets (trim, uppercase)
merchantOrderPayments = normalize(merchantOrderPayments);
reconFileDtls = normalize(reconFileDtls);

// 2. Rename columns in reconFileDtls
for (Map.Entry<String, String> entry : columnMapping().entrySet()) {
    reconFileDtls = reconFileDtls.withColumnRenamed(entry.getValue(), entry.getKey());
}

// 3. Join condition
Column joinCond = merchantOrderPayments.col("ATRN_NUM").equalTo(reconFileDtls.col("ATRN_NUM"));

// 4. Full match on key + value
Column valueMatch = joinCond;
for (String col : columnMapping().keySet()) {
    valueMatch = valueMatch.and(
        merchantOrderPayments.col(col).equalTo(reconFileDtls.col(col))
    );
}

// 5. Matched rows
Dataset<Row> matched = merchantOrderPayments.join(reconFileDtls, valueMatch, "inner");

// 6. Unmatched (based on key)
Dataset<Row> unmatched = merchantOrderPayments.join(reconFileDtls, joinCond, "left_anti");

// 7. Duplicates
Dataset<Row> sourceDuplicates = merchantOrderPayments.groupBy("ATRN_NUM").count()
        .filter("count > 1").join(merchantOrderPayments, "ATRN_NUM");

Dataset<Row> targetDuplicates = reconFileDtls.groupBy("ATRN_NUM").count()
        .filter("count > 1").join(reconFileDtls, "ATRN_NUM");





private Dataset<Row> normalize(Dataset<Row> dataset) {
    for (String col : columnMapping().keySet()) {
        if (dataset.columns() != null && java.util.Arrays.asList(dataset.columns()).contains(col)) {
            dataset = dataset.withColumn(col, functions.upper(functions.trim(dataset.col(col))));
        }
    }

    if (java.util.Arrays.asList(dataset.columns()).contains("ATRN_NUM")) {
        dataset = dataset.withColumn("ATRN_NUM", functions.upper(functions.trim(dataset.col("ATRN_NUM"))));
    }

    return dataset;
}
