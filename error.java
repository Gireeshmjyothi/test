public void updateMatchStatus(Dataset<Row> dataset, String status) {
    dataset.select("rfdId").foreachPartition(iterator -> {
        String jdbcUrl = "jdbc:postgresql://<host>:<port>/<db>";
        String username = "<your-username>";
        String password = "<your-password>";
        String updateSql = "UPDATE RECON_FILE_DTLS SET match_status = ? WHERE rfd_id = ?";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement ps = conn.prepareStatement(updateSql)) {

            conn.setAutoCommit(false);
            int batchSize = 0;

            while (iterator.hasNext()) {
                Row row = iterator.next();
                String rfdId = row.getString(0);

                ps.setString(1, status);
                ps.setString(2, rfdId);
                ps.addBatch();

                batchSize++;
                if (batchSize % 1000 == 0) {
                    ps.executeBatch();
                    conn.commit();
                    batchSize = 0;
                }
            }

            if (batchSize > 0) {
                ps.executeBatch();
                conn.commit();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Log and handle appropriately
        }
    });
}
