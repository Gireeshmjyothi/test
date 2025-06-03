public class ReconProcessor {

    private static final String JDBC_URL = "jdbc:postgresql://<host>:<port>/<db>";
    private static final String JDBC_USER = "<username>";
    private static final String JDBC_PASS = "<password>";
    private static final String JDBC_DRIVER = "org.postgresql.Driver";

    private final SparkSession spark;

    public ReconProcessor(SparkSession spark) {
        this.spark = spark;
    }

    // Method to write rfdId + status to staging table
    public void stageReconStatus(Dataset<Row> dataset, String status) {
        Dataset<Row> staged = dataset
                .selectExpr("rfdId", "'" + status + "' as reconStatus")
                .dropDuplicates("rfdId");

        staged.write()
                .mode(SaveMode.Append)
                .format("jdbc")
                .option("url", JDBC_URL)
                .option("dbtable", "recon_status_stage")
                .option("user", JDBC_USER)
                .option("password", JDBC_PASS)
                .option("driver", JDBC_DRIVER)
                .save();
    }

    // Method to bulk update RECON_FILE_DTLS from staging
    public void updateReconFromStage() {
        String updateSql =
                "UPDATE RECON_FILE_DTLS r " +
                "SET match_status = s.reconStatus " +
                "FROM recon_status_stage s " +
                "WHERE r.rfdId = s.rfdId";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(updateSql);
        } catch (SQLException e) {
            e.printStackTrace(); // Preferably use proper logging
        }
    }

    // Optional cleanup
    public void clearStageTable() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
             Statement stmt = conn.createStatement()) {
            stmt.execute("TRUNCATE TABLE recon_status_stage");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
