private void saveToReconciliationTable(Dataset<Row> dataset, String matchStatus, String reason) {
    Dataset<Row> resultDataset = dataset.withColumn("match_status", functions.lit(matchStatus))
            .withColumn("mismatch_reason", functions.lit(reason))
            .withColumn("source_json", functions.to_json(structFromColumns(dataset.columns())))
            .withColumn("recon_json", functions.to_json(structFromColumns(dataset.columns())))
            // Convert timestamp and date to string to avoid java.lang.IllegalAccessError
            .withColumn("reconciled_at", functions.date_format(functions.current_timestamp(), "yyyy-MM-dd HH:mm:ss"))
            .withColumn("batch_date", functions.date_format(functions.current_date(), "yyyy-MM-dd"));

    resultDataset.printSchema();
    jdbcReaderService.writeToReconFileResult(resultDataset, "RECONCILIATION_RESULT");
}
