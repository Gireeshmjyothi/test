org.apache.spark.SparkException: Task not serializable
	at org.apache.spark.util.ClosureCleaner$.ensureSerializable(ClosureCleaner.scala:444)
	at org.apache.spark.util.ClosureCleaner$.clean(ClosureCleaner.scala:416)
	at org.apache.spark.util.ClosureCleaner$.clean(ClosureCleaner.scala:163)
	at org.apache.spark.SparkContext.clean(SparkContext.scala:2669)
	at org.apache.spark.rdd.RDD.$anonfun$foreachPartition$1(RDD.scala:1038)
	at org.apache.spark.rdd.RDDOperationScope$.withScope(RDDOperationScope.scala:151)
	at org.apache.spark.rdd.RDDOperationScope$.withScope(RDDOperationScope.scala:112)
	at org.apache.spark.rdd.RDD.withScope(RDD.scala:410)
	at org.apache.spark.rdd.RDD.foreachPartition(RDD.scala:1037)
	at org.apache.spark.sql.Dataset.$anonfun$foreachPartition$1(Dataset.scala:3516)
	at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.java:23)
	at org.apache.spark.sql.Dataset.$anonfun$withNewRDDExecutionId$1(Dataset.scala:4310)
	at org.apache.spark.sql.execution.SQLExecution$.$anonfun$withNewExecutionId$6(SQLExecution.scala:125)
	at org.apache.spark.sql.execution.SQLExecution$.withSQLConfPropagated(SQLExecution.scala:201)
	at org.apache.spark.sql.execution.SQLExecution$.$anonfun$withNewExecutionId$1(SQLExecution.scala:108)
	at org.apache.spark.sql.SparkSession.withActive(SparkSession.scala:900)
	at org.apache.spark.sql.execution.SQLExecution$.withNewExecutionId(SQLExecution.scala:66)
	at org.apache.spark.sql.Dataset.withNewRDDExecutionId(Dataset.scala:4308)
	at org.apache.spark.sql.Dataset.foreachPartition(Dataset.scala:3516)
	at org.apache.spark.sql.Dataset.foreachPartition(Dataset.scala:3527)
	at com.epay.rns.service.ReconService.publishToKafka(ReconService.java:286)
	at com.epay.rns.service.ReconService.reconProcess(ReconService.java:105)
	at com.epay.rns.controller.SparkController.reconProcess(SparkController.java:36)
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
Caused by: java.io.NotSerializableException: com.epay.rns.service.ReconService
	at java.base/java.io.ObjectOutputStream.writeObject0(ObjectOutputStream.java:1200)
	at java.base/java.io.ObjectOutputStream.writeArray(ObjectOutputStream.java:1394)
	at java.base/java.io.ObjectOutputStream.writeObject0(ObjectOutputStream.java:1190)
	at java.base/java.io.ObjectOutputStream.defaultWriteFields(ObjectOutputStream.java:1585)
	at java.base/java.io.ObjectOutputStream.writeSerialData(ObjectOutputStream.java:1542)
	at java.base/java.io.ObjectOutputStream.writeOrdinaryObject(ObjectOutputStream.java:1451)
	at java.base/java.io.ObjectOutputStream.writeObject0(ObjectOutputStream.java:1194)
	at java.base/java.io.ObjectOutputStream.writeArray(ObjectOutputStream.java:1394)
	at java.base/java.io.ObjectOutputStream.writeObject0(ObjectOutputStream.java:1190)
	at java.base/java.io.ObjectOutputStream.defaultWriteFields(ObjectOutputStream.java:1585)
	at java.base/java.io.ObjectOutputStream.writeSerialData(ObjectOutputStream.java:1542)
	at java.base/java.io.ObjectOutputStream.writeOrdinaryObject(ObjectOutputStream.java:1451)
	at java.base/java.io.ObjectOutputStream.writeObject0(ObjectOutputStream.java:1194)
	at java.base/java.io.ObjectOutputStream.writeObject(ObjectOutputStream.java:358)
	at org.apache.spark.serializer.JavaSerializationStream.writeObject(JavaSerializer.scala:46)
	at org.apache.spark.serializer.JavaSerializerInstance.serialize(JavaSerializer.scala:115)
	at org.apache.spark.util.ClosureCleaner$.ensureSerializable(ClosureCleaner.scala:441)
	... 70 common frames omitted



private void publishToKafka(Dataset<Row> dataset, String topic, String status) {
        // Add status and repartition to control load
        Dataset<Row> enrichedDf = dataset
                .withColumn("status", lit(status))
                .repartition(10);

        enrichedDf.foreachPartition(partition -> {
            Properties props = new Properties();

            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProducerSettings.getBootstrapServers());
            props.put(ProducerConfig.ACKS_CONFIG, kafkaProducerSettings.getAcks());
            props.put(ProducerConfig.RETRIES_CONFIG, kafkaProducerSettings.getRetries());
            props.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaProducerSettings.getBatchSize());
            props.put(ProducerConfig.LINGER_MS_CONFIG, kafkaProducerSettings.getLingerMs());
            props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, kafkaProducerSettings.getBufferMemory());
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            if(kafkaProducerSettings.isSslConfigProvided()) {
                props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, kafkaProducerSettings.getSecurityProtocol());
                props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, CommonUtil.getAbsolutePath(kafkaProducerSettings.getTrustLocation()));
                props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, kafkaProducerSettings.getTrustPassword());
                props.put(SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG, kafkaProducerSettings.getTrustType());
                props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, CommonUtil.getAbsolutePath(kafkaProducerSettings.getKeyLocation()));
                props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, kafkaProducerSettings.getKeyPassword());
                props.put(SslConfigs.SSL_KEYSTORE_TYPE_CONFIG, kafkaProducerSettings.getKeyType());
            }
            props.put(ProducerConfig.CLIENT_ID_CONFIG, "recon-producer-" + UUID.randomUUID());

            KafkaProducer<String, String> producer = new KafkaProducer<>(props);

            List<String> batch = new ArrayList<>();
            final int BATCH_SIZE = 500;

            while (partition.hasNext()) {
                Row row = partition.next();

                byte[] rfdBytes = row.getAs("RFD_ID");
                String rfdHex = bytesToHex(rfdBytes).toUpperCase();

                String atrnNum = row.getAs("ATRN_NUM").toString();

                String json = String.format(
                        "{\"rfdId\":\"%s\", \"atrnNum\":\"%s\", \"status\":\"%s\"}",
                        rfdHex, atrnNum, status
                );

                batch.add(json);
                if (batch.size() >= BATCH_SIZE) {
                    sendBatch(producer, topic, batch, status);
                    batch.clear();
                }
            }
            if (!batch.isEmpty()) {
                sendBatch(producer, topic, batch, status);
            }
            producer.close();
        });
    }


private void sendBatch(KafkaProducer<String, String> producer, String topic, List<String> batch, String status) {
        String uuidKey = UUID.randomUUID() + "_" + status;
        String payload = "[" + String.join(",", batch) + "]";
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, uuidKey, payload);
        producer.send(record);
    }

private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
