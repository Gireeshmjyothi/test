public void updateReconFromStage() {
    String updateSql =
            "UPDATE RECON_FILE_DTLS r " +
            "SET r.RECON_STATUS = ( " +
            "  SELECT s.RECON_STATUS " +
            "  FROM RECON_FILE_DTLS_STAGE s " +
            "  WHERE s.RFD_ID = r.RFD_ID " +
            ") " +
            "WHERE EXISTS ( " +
            "  SELECT 1 FROM RECON_FILE_DTLS_STAGE s " +
            "  WHERE s.RFD_ID = r.RFD_ID " +
            ")";

    try (Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUserName, jdbcPassword);
         Statement stmt = conn.createStatement()) {
        stmt.executeUpdate(updateSql);
    } catch (SQLException e) {
        e.printStackTrace();
        // Consider proper logging and retry logic here
    }
}
