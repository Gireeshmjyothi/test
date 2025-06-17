CREATE TABLE RECON_STATUS_STAGE (
    RFD_ID RAW(16),
    RECON_STATUS VARCHAR2(20)
);

CREATE INDEX RSS_RFD_ID_IDX ON RECON_STATUS_STAGE(RFD_ID);

Dataset<Row> matchedWithStatus = matched.withColumn("RECON_STATUS", lit("MATCHED"));
Dataset<Row> unmatchedWithStatus = unmatched.withColumn("RECON_STATUS", lit("UNMATCHED"));
Dataset<Row> duplicateWithStatus = reconFileDetailDuplicate.withColumn("RECON_STATUS", lit("DUPLICATE"));

Dataset<Row> finalReconStatusUpdate = matchedWithStatus
    .select("RFD_ID", "RECON_STATUS")
    .union(unmatchedWithStatus.select("RFD_ID", "RECON_STATUS"))
    .union(duplicateWithStatus.select("RFD_ID", "RECON_STATUS"));


public void writeToStagingTable(Dataset<Row> dataset, String tableName) {
    Properties connectionProperties = new Properties();
    connectionProperties.put("user", "YOUR_DB_USER");
    connectionProperties.put("password", "YOUR_DB_PASSWORD");
    connectionProperties.put("driver", "oracle.jdbc.OracleDriver");

    String jdbcUrl = "jdbc:oracle:thin:@//YOUR_HOST:YOUR_PORT/YOUR_SID";

    dataset.write()
        .mode(SaveMode.Overwrite)
        .jdbc(jdbcUrl, tableName, connectionProperties);
}



@Autowired
private DataSource dataSource;

public void mergeReconStatusUpdates() {
    String mergeSQL = """
        MERGE INTO RECON_FILE_DTLS tgt
        USING RECON_STATUS_STAGE stage
        ON (tgt.RFD_ID = stage.RFD_ID)
        WHEN MATCHED THEN
          UPDATE SET tgt.RECON_STATUS = stage.RECON_STATUS
    """;

    try (Connection conn = dataSource.getConnection();
         Statement stmt = conn.createStatement()) {
        stmt.executeUpdate(mergeSQL);
    } catch (SQLException e) {
        log.error("Failed to merge RECON status updates", e);
        throw new RuntimeException(e);
    }
}
