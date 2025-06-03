Caused by: Error : 933, Position : 59, SQL = UPDATE RECON_FILE_DTLS r SET RECON_STATUS = s.RECON_STATUS FROM RECON_FILE_DTLS_STAGE s WHERE r.RFD_ID = s.RFD_ID, Original SQL = UPDATE RECON_FILE_DTLS r SET RECON_STATUS = s.RECON_STATUS FROM RECON_FILE_DTLS_STAGE s WHERE r.RFD_ID = s.RFD_ID, Error Message = ORA-00933: SQL command not properly ended

	at oracle.jdbc.driver.T4CTTIoer11.processError(T4CTTIoer11.java:717)



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
                        "SET RECON_STATUS = s.RECON_STATUS " +
                        "FROM RECON_FILE_DTLS_STAGE s " +
                        "WHERE r.RFD_ID = s.RFD_ID";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUserName, jdbcPassword);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(updateSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Optional cleanup
    public void clearStageTable() {
        try (Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUserName, jdbcPassword);
             Statement stmt = conn.createStatement()) {
            stmt.execute("TRUNCATE TABLE recon_status_stage");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
