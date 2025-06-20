private Dataset<Row> getDuplicateAtrnNums(Dataset<Row> reconFileDtls, Dataset<Row> merchantDtls) {
    // Step 1: Join to find exact matches between reconFileDtls and merchantDtls
    Dataset<Row> matched = reconFileDtls.join(merchantDtls,
        reconFileDtls.col("atrnNum").equalTo(merchantDtls.col("atrnNum"))
            .and(reconFileDtls.col("debitAmt").equalTo(merchantDtls.col("debitAmt"))),
        "inner"
    ).select("rfdId", "atrnNum", "debitAmt");

    // Step 2: Keep only one matching row to remove
    Dataset<Row> oneMatched = matched.limit(1);

    // Step 3: Remove that single matched row from reconFileDtls using rfdId
    Dataset<Row> remaining = reconFileDtls.join(oneMatched, "rfdId", "left_anti");

    // Step 4: Find atrnNum values that occur more than once in remaining rows
    Dataset<Row> duplicateKeys = remaining.groupBy("atrnNum")
                                          .count()
                                          .filter("count > 1")
                                          .select("atrnNum");

    // Step 5: Return rows that have duplicate atrnNums
    return remaining.join(duplicateKeys, "atrnNum");
}
