public void reconProcess(long startTime, long endTime) {
        logger.info("Recon process started.");
        long processStartTime = currentTimeMillis();
        logger.info("🚀 Recon process started at : {} ", sparkService.formatMillis(processStartTime));

        // Load datasets
        Dataset<Row> merchantOrderPayments = readAndNormalize("MERCHANT_ORDER_PAYMENTS", "CREATED_DATE", startTime, endTime).alias("src");
        Dataset<Row> reconFileDtls = readAndNormalize("RECON_FILE_DTLS", "PAYMENT_DATE", startTime, endTime).alias("tgt");

        // Rename recon file columns to match source
        reconFileDtls = renameColumns(reconFileDtls).alias("tgt");

        // Deduplicate datasets
        Dataset<Row> merchantDeduped = merchantOrderPayments.dropDuplicates().alias("src");
        Dataset<Row> reconDeduped = reconFileDtls.dropDuplicates().alias("tgt");

        // Join and match logic
        Column joinCond = merchantDeduped.col("ATRN_NUM").equalTo(reconDeduped.col("ATRN_NUM"));
        Column valueMatch = joinCond;
        for (String col : columnMapping().keySet()) {
            valueMatch = valueMatch.and(merchantDeduped.col(col).equalTo(reconDeduped.col(col)));
        }

        Dataset<Row> matched = merchantDeduped.join(reconDeduped, valueMatch, "inner").select("src.*").distinct();
        Dataset<Row> unmatched = merchantDeduped.join(reconDeduped, joinCond, "left_anti").distinct();

        // Duplicates
        Dataset<Row> targetDuplicates = getDuplicates(reconFileDtls, "ATRN_NUM").join(reconFileDtls, "ATRN_NUM").distinct();

        // Logging
        logDataset("Matched Rows", matched);
        logDataset("Unmatched Rows", unmatched);
        logDataset("Target Duplicates", targetDuplicates);

        // Save results
        saveToReconciliationTable(matched, "MATCHED", "matched");
        saveToReconciliationTable(unmatched, "UNMATCHED", "atrn not matched");
        saveToReconciliationTable(targetDuplicates, "TARGET_DUPLICATE", "duplicate atrn in recon file details");

        logger.info("Recon process completed in: {}", sparkService.formatMillis(currentTimeMillis() - processStartTime));
    }


org.apache.spark.sql.AnalysisException: [AMBIGUOUS_REFERENCE] Reference `ID` is ambiguous, could be: [`tgt`.`ID`, `tgt`.`ID`].
	at org.apache.spark.sql.errors.QueryCompilationErrors$.ambiguousReferenceError(QueryCompilationErrors.scala:1937)
	at org.apache.spark.sql.catalyst.expressions.package$AttributeSeq.resolve(package.scala:377)
	at org.apache.spark.sql.catalyst.plans.logical.LogicalPlan.resolveChildren(LogicalPlan.scala:146)
	at org.apache.spark.sql.catalyst.analysis.ColumnResolutionHelper.$anonfun$resolveExpressionByPlanChildren$1(ColumnResolutionHelper.scala:364)
	at org.apache.spark.sql.catalyst.analysis.ColumnResolutionHelper.$anonfun$resolveExpression$3(ColumnResolutionHelper.scala:157)
	at org.apache.spark.sql.catalyst.analysis.package$.withPosition(package.scala:100)
	at org.apache.spark.sql.catalyst.analysis.ColumnResolutionHelper.$anonfun$resolveExpression$1(ColumnResolutionHelper.scala:164)
	at org.apache.spark.sql.catalyst.trees.CurrentOrigin$.withOrigin(origin.scala:76)
