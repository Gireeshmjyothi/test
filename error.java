17:58:35.283 [http-nio-8080-exec-1] ERROR org.apache.catalina.core.ContainerBase.[Tomcat].[localhost].[/].[dispatcherServlet] - Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed: org.apache.spark.sql.AnalysisException: [UNRESOLVED_COLUMN.WITH_SUGGESTION] A column or function parameter with name `sbiOrderRefNumber` cannot be resolved. Did you mean one of the following? [`ATRN_NUM`, `DEBIT_AMT`, `MID`, `ORDER_REF_NUMBER`].] with root cause
org.apache.spark.sql.AnalysisException: [UNRESOLVED_COLUMN.WITH_SUGGESTION] A column or function parameter with name `sbiOrderRefNumber` cannot be resolved. Did you mean one of the following? [`ATRN_NUM`, `DEBIT_AMT`, `MID`, `ORDER_REF_NUMBER`].
	at org.apache.spark.sql.errors.QueryCompilationErrors$.unresolvedAttributeError(QueryCompilationErrors.scala:307) ~[spark-catalyst_2.13-3.5.1.jar:3.5.1]
	at org.apache.spark.sql.catalyst.analysis.CheckAnalysis.org$apache$spark$sql$catalyst$analysis$CheckAnalysis$$failUnresolvedAttribute(CheckAnalysis.scala:147) ~[spark-catalyst_2.13-3.5.1.jar:3.5.1]
	at org.apache.spark.sql.catalyst.analysis.CheckAnalysis.$anonfun$checkAnalysis0$6(CheckAnalysis.scala:266) ~[spark-catalyst_2.13-3.5.1.jar:3.5.1]
	at org.apache.spark.sql.catalyst.analysis.CheckAnalysis.$anonfun$checkAnalysis0$6$adapted(CheckAnalysis.scala:264) ~[spark-catalyst_2.13-3.5.1.jar:3.5.1]
	at org.apache.spark.sql.catalyst.trees.TreeNode.foreachUp(TreeNode.scala:244) ~[spark-catalyst_2.13-3.5.1.jar:3.5.1]
	at org.apache.spark.sql.catalyst.trees.TreeNode.$anonfun$foreachUp$1(TreeNode.scala:243) ~[spark-catalyst_2.13-3.5.1.jar:3.5.1]
	at org.apache.spark.sql.catalyst.trees.TreeNode.$anonfun$foreachUp$1$adapted(TreeNode.scala:243) ~[spark-catalyst_2.13-3.5.1.jar:3.5.1]
	at scala.collection.immutable.Vector.foreach(Vector.scala:2124) ~[scala-library-2.13.13.jar:?]
	at org.apache.spark.sql.catalyst.trees.TreeNode.foreachUp(TreeNode.scala:243) ~[spark-catalyst_2.13-3.5.1.jar:3.5.1]
	at org.apache.spark.sql.catalyst.trees.TreeNode.$anonfun$foreachUp$1(TreeNode.scala:243) ~[spark-catalyst_2.13-3.5.1.jar:3.5.1]
	at org.apache.spark.sql.catalyst.trees.TreeNode.$anonfun$foreachUp$1$adapted(TreeNode.scala:243) ~[spark-catalyst_2.13-3.5.1.jar:3.5.1]
	at scala.collection.immutable.Vector.foreach(Vector.scala:2124) ~[scala-library-2.13.13.jar:?]
	at org.apache.spark.sql.catalyst.trees.TreeNode.foreachUp(TreeNode.scala:243) ~[spark-catalyst_2.13-3.5.1.jar:3.5.1]
	at org.apache.spark.sql.catalyst.trees.TreeNode.$anonfun$foreachUp$1(TreeNode.scala:243) ~[spark-catalyst_2.13-3.5.1.jar:3.5.1]
	at org.apache.spark.sql.catalyst.trees.TreeNode.$anonfun$foreachUp$1$adapted(TreeNode.scala:243) ~[spark-catalyst_2.13-3.5.1.jar:3.5.1]
	at scala.collection.immutable.List.foreach(List.scala:334) ~[scala-library-2.13.13.jar:?]
	at org.apache.spark.sql.catalyst.trees.TreeNode.foreachUp(TreeNode.scala:243) ~[spark-catalyst_2.13-3.5.1.jar:3.5.1]
	at org.apache.spark.sql.catalyst.analysis.CheckAnalysis.$anonfun$checkAnalysis0$5(CheckAnalysis.scala:264) ~[spark-catalyst_2.13-3.5.1.jar:3.5.1]
	at org.apache.spark.sql.catalyst.analysis.CheckAnalysis.$anonfun$checkAnalysis0$5$adapted(CheckAnalysis.scala:264) ~[spark-catalyst_2.13-3.5.1.jar:3.5.1]
	at scala.collection.immutable.List.foreach(List.scala:334) ~[scala-library-2.13.13.jar:?]
	at org.apache.spark.sql.catalyst.analysis.CheckAnalysis.$anonfun$checkAnalysis0$2(CheckAnalysis.scala:264) ~[spark-catalyst_2.13-3.5.1.jar:3.5.1]
	at org.apache.spark.sql.catalyst.analysis.CheckAnalysis.$anonfun$checkAnalysis0$2$adapted(CheckAnalysis.scala:182) ~[spark-catalyst_2.13-3.5.1.jar:3.5.1]
	at org.apache.spark.sql.catalyst.trees.TreeNode.foreachUp(TreeNode.scala:244) ~[spark-catalyst_2.13-3.5.1.jar:3.5.1]
	at org.apache.spark.sql.catalyst.analysis.CheckAnalysis.checkAnalysis0(CheckAnalysis.scala:182) ~[spark-catalyst_2.13-3.5.1.jar:3.5.1]
	at org.apache.spark.sql.catalyst.analysis.CheckAnalysis.checkAnalysis0$(CheckAnalysis.scala:164) ~[spark-catalyst_2.13-3.5.1.jar:3.5.1]
	at org.apache.spark.sql.catalyst.analysis.Analyzer.checkAnalysis0(Analyzer.scala:188) ~[spark-catalyst_2.13-3.5.1.jar:3.5.1]
	at org.apache.spark.sql.catalyst.analysis.CheckAnalysis.checkAnalysis(CheckAnalysis.scala:160) ~[spark-catalyst_2.13-3.5.1.jar:3.5.1]
	at org.apache.spark.sql.catalyst.analysis.CheckAnalysis.checkAnalysis$(CheckAnalysis.scala:150) ~[spark-catalyst_2.13-3.5.1.jar:3.5.1]
	at org.apache.spark.sql.catalyst.analysis.Analyzer.checkAnalysis(Analyzer.scala:188) ~[spark-catalyst_2.13-3.5.1.jar:3.5.1]
	at org.apache.spark.sql.catalyst.encoders.ExpressionEncoder.resolveAndBind(ExpressionEncoder.scala:347) ~[spark-catalyst_2.13-3.5.1.jar:3.5.1]
	at org.apache.spark.sql.Dataset.resolvedEnc$lzycompute(Dataset.scala:240) ~[spark-sql_2.13-3.5.1.jar:3.5.1]
	at org.apache.spark.sql.Dataset.org$apache$spark$sql$Dataset$$resolvedEnc(Dataset.scala:239) ~[spark-sql_2.13-3.5.1.jar:3.5.1]
	at org.apache.spark.sql.Dataset$.apply(Dataset.scala:83) ~[spark-sql_2.13-3.5.1.jar:3.5.1]
	at org.apache.spark.sql.Dataset.as(Dataset.scala:490) ~[spark-sql_2.13-3.5.1.jar:3.5.1]
	at com.rajput.service.SparkService.getFileDataset(SparkService.java:112) ~[main/:?]
	at com.rajput.service.SparkService.process(SparkService.java:39) ~[main/:?]
	at com.rajput.controller.SparkController.home(SparkController.java:19) ~[main/:?]
	at jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103) ~[?:?]
	at java.lang.reflect.Method.invoke(Method.java:580) ~[?:?]
	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:255) ~[spring-web-6.1.18.jar:6.1.18]
	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:188) ~[spring-web-6.1.18.jar:6.1.18]
	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:118) ~[spring-webmvc-6.1.18.jar:6.1.18]
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:926) ~[spring-webmvc-6.1.18.jar:6.1.18]
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:831) ~[spring-webmvc-6.1.18.jar:6.1.18]
	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87) ~[spring-webmvc-6.1.18.jar:6.1.18]
	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1089) ~[spring-webmvc-6.1.18.jar:6.1.18]
	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:979) ~[spring-webmvc-6.1.18.jar:6.1.18]
	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1014) ~[spring-webmvc-6.1.18.jar:6.1.18]
	at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:903) ~[spring-webmvc-6.1.18.jar:6.1.18]
	at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:564) ~[tomcat-embed-core-10.1.39.jar:6.0]
	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:885) ~[spring-webmvc-6.1.18.jar:6.1.18]
	at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658) ~[tomcat-embed-core-10.1.39.jar:6.0]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:195) ~[tomcat-embed-core-10.1.39.jar:10.1.39]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140) ~[tomcat-embed-core-10.1.39.jar:10.1.39]
	at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:51) ~[tomcat-embed-websocket-10.1.39.jar:10.1.39]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:164) ~[tomcat-embed-core-10.1.39.jar:10.1.39]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140) ~[tomcat-embed-core-10.1.39.jar:10.1.39]
	at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100) ~[spring-web-6.1.18.jar:6.1.18]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116) ~[spring-web-6.1.18.jar:6.1.18]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:164) ~[tomcat-embed-core-10.1.39.jar:10.1.39]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140) ~[tomcat-embed-core-10.1.39.jar:10.1.39]
	at org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93) ~[spring-web-6.1.18.jar:6.1.18]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116) ~[spring-web-6.1.18.jar:6.1.18]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:164) ~[tomcat-embed-core-10.1.39.jar:10.1.39]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140) ~[tomcat-embed-core-10.1.39.jar:10.1.39]
	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201) ~[spring-web-6.1.18.jar:6.1.18]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116) ~[spring-web-6.1.18.jar:6.1.18]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:164) ~[tomcat-embed-core-10.1.39.jar:10.1.39]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140) ~[tomcat-embed-core-10.1.39.jar:10.1.39]
	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:167) ~[tomcat-embed-core-10.1.39.jar:10.1.39]
	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:90) ~[tomcat-embed-core-10.1.39.jar:10.1.39]
	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:483) ~[tomcat-embed-core-10.1.39.jar:10.1.39]
	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:115) ~[tomcat-embed-core-10.1.39.jar:10.1.39]
	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:93) ~[tomcat-embed-core-10.1.39.jar:10.1.39]
	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:74) ~[tomcat-embed-core-10.1.39.jar:10.1.39]
	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:344) ~[tomcat-embed-core-10.1.39.jar:10.1.39]
	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:397) ~[tomcat-embed-core-10.1.39.jar:10.1.39]
	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:63) ~[tomcat-embed-core-10.1.39.jar:10.1.39]
	at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:905) ~[tomcat-embed-core-10.1.39.jar:10.1.39]
	at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1743) ~[tomcat-embed-core-10.1.39.jar:10.1.39]
	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:52) ~[tomcat-embed-core-10.1.39.jar:10.1.39]
	at org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1190) ~[tomcat-embed-core-10.1.39.jar:10.1.39]
	at org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659) ~[tomcat-embed-core-10.1.39.jar:10.1.39]
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:63) ~[tomcat-embed-core-10.1.39.jar:10.1.39]
	at java.lang.Thread.run(Thread.java:1583) [?:?]


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MerchantOrderPayment implements Serializable {
    @JsonProperty("MERCHANT_ID")
    private String mid;
    @JsonProperty("ORDER_REF_NUMBER")
    private String orderRefNumber;
    @JsonProperty("SBI_ORDER_REF_NUMBER")
    private String sbiOrderRefNumber;
    @JsonProperty("ATRN_NUMBER")
    private String atrnNum;
    @JsonProperty("DEBIT_AMT")
    private double debitAmt;
}

CREATE TABLE "MERCHANT_ORDER_PAYMENTS" (	
    "MERCHANT_ID" VARCHAR2(20 BYTE),
	"ORDER_REF_NUMBER" VARCHAR2(50 BYTE), 
	"SBI_ORDER_REF_NUMBER" VARCHAR2(50 BYTE), 
	"ATRN_NUM" VARCHAR2(50 BYTE),
	"BANK_REFERENCE_NUMBER" VARCHAR2(255 BYTE), 
	"CURRENCY_CODE" VARCHAR2(50 BYTE),
	"CHANNEL_BANK" VARCHAR2(100 BYTE),
	"PAY_MODE" VARCHAR2(50 BYTE),
	"GTW_MAP_ID" VARCHAR2(20 BYTE), 
	"PAY_PROC_ID" VARCHAR2(20 BYTE))

  
    private Dataset<MerchantOrderPayment> getFileDataset() {
        return sparkSession.read().option("header", true).option("inferSchema", true)
                .csv("data/merchantOrderPayment.csv").cache().as(merchantOrderPayment);
    }
}
