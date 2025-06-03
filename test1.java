// Method to write rfdId + status to staging table
    public void stageReconStatus(Dataset<Row> dataset, String status) {
        Dataset<Row> staged = dataset
                .selectExpr("RFD_ID", "'" + status + "' as RECON_STATUS")
                .dropDuplicates("RFD_ID");

        staged.write()
                .mode(SaveMode.Append)
                .format("jdbc")
                .option("url", jdbcUrl)
                .option("dbtable", "RECON_FILE_DTLS_STAGE")
                .option("user", jdbcUserName)
                .option("password", jdbcPassword)
                .option("driver", jdbcDriver)
                .save();
    }

    // Method to bulk update RECON_FILE_DTLS from staging
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

    // Optional cleanup
    public void clearStageTable() {
        try (Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUserName, jdbcPassword);
             Statement stmt = conn.createStatement()) {
            stmt.execute("TRUNCATE TABLE RECON_FILE_DTLS_STAGE");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
   jdbcReaderService.stageReconStatus(matched, "MATCHED");
        jdbcReaderService.stageReconStatus(unmatched, "UNMATCHED");
        jdbcReaderService.stageReconStatus(reconFileDetailDuplicate, "DUPLICATE");
        jdbcReaderService.updateReconFromStage();
        jdbcReaderService.clearStageTable();
        logger.info("🚀 Update process data ends: {}", sparkService.formatMillis(currentTimeMillis() - localStartTime))
