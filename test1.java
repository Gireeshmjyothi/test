public void writeToStageTable(Dataset<Row> updatedData, String stageTableName) {
    updatedData.write()
        .mode(SaveMode.Overwrite)
        .format("jdbc")
        .option("url", "jdbc:your-db-url")
        .option("dbtable", stageTableName)
        .option("user", "your-username")
        .option("password", "your-password")
        .save();
}

public void mergeStageToMain(String stageTableName, String targetTableName, String keyColumn) {
    String mergeSql = String.format(
        "MERGE INTO %s T " +
        "USING %s S " +
        "ON T.%s = S.%s " +
        "WHEN MATCHED THEN UPDATE SET T.RECON_STATUS = S.RECON_STATUS",
        targetTableName, stageTableName, keyColumn, keyColumn
    );

    try (Connection connection = DriverManager.getConnection("jdbc:your-db-url", "your-username", "your-password");
         Statement stmt = connection.createStatement()) {
        stmt.execute(mergeSql);
    }
}
private void updateReconFileDetails(Dataset<Row> dataset, String status) {
    Dataset<Row> updated = dataset.select("recon.ATRN_NUM")
                                  .withColumn("RECON_STATUS", functions.lit(status));

    String stageTableName = "RECON_FILE_DTLS_STAGE";
    jdbcReaderService.writeToStageTable(updated, stageTableName);
    jdbcReaderService.mergeStageToMain(stageTableName, "RECON_FILE_DTLS", "ATRN_NUM");
}
