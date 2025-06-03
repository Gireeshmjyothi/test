public void stageReconStatus(Dataset<Row> dataset, String status) {
    Dataset<Row> staged = dataset
            .selectExpr("RFD_ID", "uuidToBytes(RFD_ID) as RFD_ID", "'" + status + "' as RECON_STATUS") // convert UUID to RAW(16)
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
