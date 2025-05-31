// ✅ Step 1: Filter only unique ATRN_NUMs from reconFileDtls
Dataset<Row> reconUnique = reconFileDtls.groupBy("ATRN_NUM")
    .count()
    .filter("count = 1")
    .select("ATRN_NUM");

Dataset<Row> filteredRecon = reconFileDtls.join(reconUnique, "ATRN_NUM");

// ✅ Step 2: Filter only unique ATRN_NUMs from merchantOrderPayments
Dataset<Row> merchantUnique = merchantOrderPayments.groupBy("ATRN_NUM")
    .count()
    .filter("count = 1")
    .select("ATRN_NUM");

Dataset<Row> filteredMerchant = merchantOrderPayments.join(merchantUnique, "ATRN_NUM");

// ✅ Step 3: Join unique-only records to find matched ones
Column matchCondition = filteredRecon.col("ATRN_NUM").equalTo(filteredMerchant.col("ATRN_NUM"))
    .and(filteredRecon.col("DEBIT_AMT").equalTo(filteredMerchant.col("DEBIT_AMT")));

Dataset<Row> matched = filteredRecon
    .join(filteredMerchant, matchCondition, "inner")
    .dropDuplicates("ATRN_NUM");  // this is now optional due to filtering above
