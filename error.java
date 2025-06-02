 Request: http://localhost:9097/api/rns/v1/process raised {}
java.lang.RuntimeException: java.sql.SQLSyntaxErrorException: ORA-00928: missing SELECT keyword

https://docs.oracle.com/error-help/db/ora-00928/
	at com.epay.rns.service.JdbcReaderService.mergeStageToMain(JdbcReaderService.java:132)
	at com.epay.rns.service.ReconService.updateReconFileDetails(ReconService.java:168)
	at com.epay.rns.service.ReconService.reconProcess(ReconService.java:93)
	at com.epay.rns.controller.SparkController.reconProcess(SparkController.java:31)
	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103)
	at java.base/java.lang.reflect.Method.invoke(Method.java:580)
	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:255)
	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:188)
	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:118)
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:926)


private void updateReconFileDetails(Dataset<Row> dataset, String status) {
        Dataset<Row> updated = dataset.select(functions.col("recon.ATRN_NUM"))
                .withColumn("RECON_STATUS", functions.lit(status));
        updated.printSchema();
        updated.first();
        String stageTableName = "RECON_FILE_DTLS_STAGE";
        jdbcReaderService.writeToStageTable(updated, stageTableName);
        jdbcReaderService.mergeStageToMain(stageTableName, "RECON_FILE_DTLS", "ATRN_NUM");
    }

public void writeToStageTable(Dataset<Row> updatedData, String stageTableName) {
        updatedData.write()
                .mode(SaveMode.Overwrite)
                .format("jdbc")
                .option("url", jdbcUrl)
                .option("dbtable", stageTableName)
                .option("user", jdbcUserName)
                .option("password", jdbcPassword)
                .save();
    }

    public void mergeStageToMain(String stageTableName, String targetTableName, String keyColumn) {
        String mergeSql = String.format(
                "(MERGE INTO %s T " +
                        "USING %s S " +
                        "ON T.%s = S.%s " +
                        "WHEN MATCHED THEN UPDATE SET T.RECON_STATUS = S.RECON_STATUS)",
                targetTableName, stageTableName, keyColumn, keyColumn
        );

        try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUserName, jdbcPassword);
             Statement stmt = connection.createStatement()) {
            stmt.execute(mergeSql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


