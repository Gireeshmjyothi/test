// JdbcReaderService.java

public void updateReconFileDetails(Dataset<Row> updateDataset, String tableName, String keyColumn) {
    updateDataset.foreachPartition(iterator -> {
        try (Connection connection = DriverManager.getConnection("jdbc:your-db-url", "username", "password")) {
            connection.setAutoCommit(false);
            String sql = "UPDATE " + tableName + " SET RECON_STATUS = ? WHERE " + keyColumn + " = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                while (iterator.hasNext()) {
                    Row row = iterator.next();
                    statement.setString(1, row.getAs("RECON_STATUS"));
                    statement.setString(2, row.getAs(keyColumn));
                    statement.addBatch();
                }
                statement.executeBatch();
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                throw e;
            }
        }
    });
}


private void updateReconFileDetails(Dataset<Row> dataset, String status) {
    Dataset<Row> updated = dataset.select("recon.ATRN_NUM")
            .withColumn("RECON_STATUS", functions.lit(status));
    jdbcReaderService.updateReconFileDetails(updated, "RECON_FILE_DTLS", "ATRN_NUM");
}
