public void writeToStagingTable(Dataset<Row> dataset, String tableName) {
        logger.info("Inserting into recon status stage.");
        Properties connectionProperties = new Properties();
        connectionProperties.put("user", jdbcUserName);
        connectionProperties.put("password", jdbcPassword);
        connectionProperties.put("driver", jdbcDriver);

        dataset.write()
                .mode(SaveMode.Append)
                .jdbc(jdbcUrl, tableName, connectionProperties);
        logger.info("Insertion completed.");
    }


    public void updateReconStatusFromStage() {
        String mergeSql = """
                    MERGE INTO RECON_FILE_DTLS tgt
                    USING RECON_STATUS_STAGE src
                    ON (tgt.RFD_ID = src.RFD_ID)
                    WHEN MATCHED THEN
                      UPDATE SET tgt.RECON_STATUS = src.RECON_STATUS
                """;

        jdbcTemplate.update(mergeSql);
    }
