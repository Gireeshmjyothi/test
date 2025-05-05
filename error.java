18:27:21.254 [dispatcher-event-loop-2] ERROR org.apache.spark.rpc.netty.Inbox - Ignoring error
org.apache.spark.SparkException: Exiting due to error from cluster scheduler: Master removed our application: FAILED
	at org.apache.spark.errors.SparkCoreErrors$.clusterSchedulerError(SparkCoreErrors.scala:291) ~[spark-core_2.12-3.5.5.jar:3.5.5]
	at org.apache.spark.scheduler.TaskSchedulerImpl.error(TaskSchedulerImpl.scala:981) ~[spark-core_2.12-3.5.5.jar:3.5.5]
	at org.apache.spark.scheduler.cluster.StandaloneSchedulerBackend.dead(StandaloneSchedulerBackend.scala:165) ~[spark-core_2.12-3.5.5.jar:3.5.5]
	at org.apache.spark.deploy.client.StandaloneAppClient$ClientEndpoint.markDead(StandaloneAppClient.scala:263) ~[spark-core_2.12-3.5.5.jar:3.5.5]
	at org.apache.spark.deploy.client.StandaloneAppClient$ClientEndpoint$$anonfun$receive$1.applyOrElse(StandaloneAppClient.scala:170) ~[spark-core_2.12-3.5.5.jar:3.5.5]
	at org.apache.spark.rpc.netty.Inbox.$anonfun$process$1(Inbox.scala:115) ~[spark-core_2.12-3.5.5.jar:3.5.5]
	at org.apache.spark.rpc.netty.Inbox.safelyCall(Inbox.scala:213) ~[spark-core_2.12-3.5.5.jar:3.5.5]
	at org.apache.spark.rpc.netty.Inbox.process(Inbox.scala:100) ~[spark-core_2.12-3.5.5.jar:3.5.5]
	at org.apache.spark.rpc.netty.MessageLoop.org$apache$spark$rpc$netty$MessageLoop$$receiveLoop(MessageLoop.scala:75) ~[spark-core_2.12-3.5.5.jar:3.5.5]
	at org.apache.spark.rpc.netty.MessageLoop$$anon$1.run(MessageLoop.scala:41) ~[spark-core_2.12-3.5.5.jar:3.5.5]
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144) ~[?:?]
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642) ~[?:?]
	at java.lang.Thread.run(Thread.java:1583) [?:?]
18:27:21.287 [dispatcher-event-loop-4] INFO  org.apache.spark.MapOutputTrackerMasterEndpoint - MapOutputTrackerMasterEndpoint stopped!
18:27:21.308 [stop-spark-context] INFO  org.apache.spark.storage.memory.MemoryStore - MemoryStore cleared
18:27:21.309 [stop-spark-context] INFO  org.apache.spark.storage.BlockManager - BlockManager stopped
18:27:21.313 [stop-spark-context] INFO  org.apache.spark.storage.BlockManagerMaster - BlockManagerMaster stopped
18:27:21.317 [dispatcher-event-loop-9] INFO  org.apache.spark.scheduler.OutputCommitCoordinator$OutputCommitCoordinatorEndpoint - OutputCommitCoordinator stopped!
18:27:21.340 [stop-spark-context] INFO  org.apache.spark.SparkContext - Successfully stopped SparkContext
18:27:21.570 [main] WARN  org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration$JpaWebConfiguration - spring.jpa.open-in-view is enabled by default. Therefore, database queries may be performed during view rendering. Explicitly configure spring.jpa.open-in-view to disable this warning
18:27:21.647 [main] INFO  org.springframework.validation.beanvalidation.OptionalValidatorFactoryBean - Failed to set up a Bean Validation provider: jakarta.validation.NoProviderFoundException: Unable to create a Configuration, because no Jakarta Bean Validation provider could be found. Add a provider like Hibernate Validator (RI) to your classpath.
18:27:22.061 [main] INFO  org.apache.coyote.http11.Http11NioProtocol - Starting ProtocolHandler ["http-nio-8089"]
18:27:22.096 [main] INFO  org.springframework.boot.web.embedded.tomcat.TomcatWebServer - Tomcat started on port 8089 (http) with context path '/'
18:27:22.104 [main] INFO  com.rajput.SpringbootJspSparkDemoApplication - Started SpringbootJspSparkDemoApplication in 7.356 seconds (process running for 8.727)
