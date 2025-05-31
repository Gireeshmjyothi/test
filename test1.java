public void updateReconFileDetails(Dataset<Row> updateDataset, String tableName, String keyColumn) {
        updateDataset.createOrReplaceTempView("updates");

        String updateSQL = String.format(
                """ 
                        MERGE INTO %s target
                        USING updates source
                        ON target.%s = source.%s
                        WHEN MATCHED THEN UPDATE SET
                        target.match_status = source.match_status
                        """,
                tableName, keyColumn, keyColumn
        );

        updateDataset.sparkSession().sql(updateSQL);
    }
