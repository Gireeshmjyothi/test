private void saveToReconciliationTable(Dataset<Row> dataset, String matchStatus, boolean isMatched) {
        Dataset<Row> resultDataset = dataset.withColumn("match_status", functions.lit(matchStatus))
                .withColumn("mismatch_reason", functions.lit(isMatched ? null : "Data mismatch"))
                .withColumn("source_json", functions.to_json(structFromColumns(dataset.columns())))
                .withColumn("recon_json", functions.to_json(structFromColumns(dataset.columns())))
                .withColumn("reconciled_at", functions.current_timestamp())
                .withColumn("batch_date", functions.current_date());

        resultDataset.printSchema();
        jdbcReaderService.writeToReconFileResult(resultDataset, "RECONCILIATION_RESULT");
    }
