private void saveToReconciliationTable(Dataset<Row> dataset, String matchStatus, String reason) {
        Dataset<Row> resultDataset = dataset.withColumn("match_status", functions.lit(matchStatus))
                .withColumn("mismatch_reason", functions.lit(reason))
                .withColumn("source_json", functions.to_json(structFromColumns(dataset.columns())))
                .withColumn("recon_json", functions.to_json(structFromColumns(dataset.columns())))
                .withColumn("reconciled_at", functions.current_timestamp())
                .withColumn("batch_date", functions.current_date());

        resultDataset.printSchema();
        jdbcReaderService.writeToReconFileResult(resultDataset, "RECONCILIATION_RESULT");
    }

    private Column structFromColumns(String[] columns) {
        Column[] cols = Arrays.stream(columns)
                .map(functions::col)
                .toArray(Column[]::new);
        return functions.struct(cols);
    }
