// Join only on key columns (ATRN_NUM)
Column joinCond = merchantDeduped.col("ATRN_NUM").equalTo(reconDeduped.col("ATRN_NUM"));

// Inner join on keys
Dataset<Row> joined = merchantDeduped.join(reconDeduped, joinCond, "inner")
        .alias("joined");

// Now compare value columns to identify matched vs mismatched
Column valueMatch = lit(true);
for (String col : columnMapping().keySet()) {
    valueMatch = valueMatch.and(
        joined.col("src." + col).equalTo(joined.col("tgt." + col))
    );
}

// Filter matched rows where values match
Dataset<Row> matched = joined.filter(valueMatch)
        .select("src.*");

// Filter rows where keys matched but values differ (partial match)
Dataset<Row> mismatched = joined.filter(valueMatch.not());

// Unmatched = source rows that do not exist in target on key join
Dataset<Row> unmatched = merchantDeduped.join(reconDeduped, joinCond, "left_anti");
