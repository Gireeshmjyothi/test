java.lang.RuntimeException: java.sql.SQLException: ORA-30926: unable to get a stable set of rows in the source tables

https://docs.oracle.com/error-help/db/ora-30926/
	at com.epay.rns.service.JdbcReaderService.mergeStageToMain(JdbcReaderService.java:132)
	at com.epay.rns.service.ReconService.updateReconFileDetails(ReconService.java:168)
	at com.epay.rns.service.ReconService.reconProcess(ReconService.java:95)
	at com.epay.rns.controller.SparkController.reconProcess(SparkController.java:31)
	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103)
	at java.base/java.lang.reflect.Method.invoke(Method.java:580)
	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:255)
	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:188)
	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:118)
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:926)
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:831)
	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)
	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1089)
	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:979)
	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1014)
	at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:903)
	at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:564)
	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:885)
	at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:195)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140)
	at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:51)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:164)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140)
	at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100)
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:164)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140)
	at org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93)
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:164)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140)
	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201)
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:164)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140)
	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:167)
	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:90)
	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:483)
	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:115)
	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:93)
	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:74)
	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:344)
	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:397)
	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:63)
	at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:905)
	at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1743)
	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:52)
	at org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1190)
	at org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659)
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:63)
	at java.base/java.lang.Thread.run(Thread.java:1583)
Caused by: java.sql.SQLException: ORA-30926: unable to get a stable set of rows in the source tables





private void updateReconFileDetails(Dataset<Row> dataset, String status) {
        Dataset<Row> updated = dataset.select(functions.col("recon.ATRN_NUM"))
                .withColumn("RECON_STATUS", functions.lit(status));

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
                "MERGE INTO %s T " +
                        "USING %s S " +
                        "ON (T.%s = S.%s) " +
                        "WHEN MATCHED THEN UPDATE SET T.RECON_STATUS = S.RECON_STATUS",
                targetTableName, stageTableName, keyColumn, keyColumn
        );

        try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUserName, jdbcPassword);
             Statement stmt = connection.createStatement()) {
            stmt.execute(mergeSql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


   updateReconFileDetails(matched, "MATCHED");
        updateReconFileDetails(unmatched, "UNMATCHED");
        updateReconFileDetails(reconFileDetailDuplicate, "DUPLICATE");
