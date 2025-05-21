Column joinCond = merchantDeduped.col("ATRN_NUM").equalTo(reconDeduped.col("ATRN_NUM"));

Column valueMatch = joinCond;
for (String col : columnMapping().keySet()) {
    valueMatch = valueMatch.and(merchantDeduped.col(col).equalTo(reconDeduped.col(col)));
}

Dataset<Row> matched = merchantDeduped.join(reconDeduped, valueMatch, "inner");

Dataset<Row> source = merchantDeduped.alias("src");
Dataset<Row> target = reconDeduped.alias("tgt");

Column joinCond = source.col("ATRN_NUM").equalTo(target.col("ATRN_NUM"));

Column valueMatch = joinCond;
for (String col : columnMapping().keySet()) {
    valueMatch = valueMatch.and(source.col(col).equalTo(target.col(col)));
}

Dataset<Row> matched = source.join(target, valueMatch, "inner");
Dataset<Row> unmatched = source.join(target, source.col("ATRN_NUM").equalTo(target.col("ATRN_NUM")), "left_anti");

Dataset<Row> sourceDuplicates = merchantOrderPayments.except(merchantDeduped);
Dataset<Row> targetDuplicates = reconFileDtls.except(reconDeduped);


.withColumn("atrn_num", functions.col("src.ATRN_NUM"))

