// Step 1: Join on atrnNum + debitAmt
Dataset<Row> potentialMatches = reconFileDtls.alias("r")
        .join(merchantDtls.alias("m"),
                col("r.atrnNum").equalTo(col("m.atrnNum"))
                .and(col("r.debitAmt").equalTo(col("m.debitAmt"))),
                "inner")
        .select(col("r.rfdId"), col("r.atrnNum"), col("r.debitAmt"));

// Step 2: Only keep first usage of each (atrnNum, debitAmt)
WindowSpec matchWindow = Window.partitionBy("atrnNum", "debitAmt").orderBy("rfdId");

Dataset<Row> rankedMatches = potentialMatches.withColumn("rank", row_number().over(matchWindow));

Dataset<Row> matched = rankedMatches.filter(col("rank").equalTo(1)).drop("rank");

// Step 3: All other entries with same atrnNum but not matched → duplicate
Dataset<Row> sameAtrnJoin = reconFileDtls.alias("r")
        .join(merchantDtls.alias("m"),
                col("r.atrnNum").equalTo(col("m.atrnNum")),
                "inner")
        .select(col("r.rfdId"), col("r.atrnNum"), col("r.debitAmt"));

// Step 4: Duplicates = same atrnNum join - matched
Dataset<Row> duplicates = sameAtrnJoin
        .join(matched.select("rfdId"), "rfdId", "left_anti");

// Step 5: Unmatched = reconFileDtls - (matched + duplicates)
Dataset<Row> matchedAndDuplicates = matched.union(duplicates).select("rfdId");

Dataset<Row> unmatched = reconFileDtls
        .join(matchedAndDuplicates, "rfdId", "left_anti");
