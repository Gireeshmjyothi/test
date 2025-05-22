org.apache.spark.sql.AnalysisException: [AMBIGUOUS_REFERENCE] Reference `ATRN_NUM` is ambiguous, could be: [`ATRN_NUM`, `ATRN_NUM`].

public void writeToReconFileResult(Dataset<Row> df, String tableName) {
        df.select(
                        "atrn_num", "match_status", "mismatch_reason",
                        "source_json", "recon_json", "reconciled_at", "batch_date"
                ).write()
                .format("jdbc")
                .option("url", jdbcConfig.getJdbcUrl())
                .option("dbtable", tableName)
                .option("user", jdbcUserName)
                .option("password", jdbcPassword)
                .option("driver", jdbcDriver)
                .mode(SaveMode.Append)
                .save();
    }

private void saveToReconciliationTable(Dataset<Row> dataset, String matchStatus, boolean isMatched) {
        Dataset<Row> resultDataset = dataset.withColumn("match_status", functions.lit(matchStatus))
                .withColumn("mismatch_reason", functions.lit(isMatched ? null : "Data mismatch"))
                .withColumn("source_json", functions.to_json(structFromColumns(dataset.columns())))
                .withColumn("recon_json", functions.to_json(structFromColumns(dataset.columns())))
                .withColumn("reconciled_at", functions.current_timestamp())
                .withColumn("batch_date", functions.current_date());


        jdbcReaderService.writeToReconFileResult(resultDataset, "RECONCILIATION_RESULT");
    }

    private Column structFromColumns(String[] columns) {
        Column[] cols = Arrays.stream(columns)
                .map(functions::col)
                .toArray(Column[]::new);
        return functions.struct(cols);
    }
