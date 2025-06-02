public void truncateStageTable(String stageTableName) {
    try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUserName, jdbcPassword);
         Statement stmt = connection.createStatement()) {
        stmt.execute("TRUNCATE TABLE " + stageTableName);
    } catch (SQLException e) {
        throw new RuntimeException("Failed to truncate stage table: " + stageTableName, e);
    }
}

