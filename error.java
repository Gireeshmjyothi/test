dataset.select("rfdId").foreachPartition(iterator -> {
    try (Connection conn = ...; PreparedStatement ps = conn.prepareStatement("UPDATE table SET status = ? WHERE rfd_id = ?")) {
        while (iterator.hasNext()) {
            Row row = iterator.next();
            String rfdId = row.getString(0);
            ps.setString(1, "MATCHED");
            ps.setString(2, rfdId);
            ps.addBatch();
        }
        ps.executeBatch();
    }
});
