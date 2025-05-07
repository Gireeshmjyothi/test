ERROR org.apache.spark.rpc.netty.Inbox - Ignoring error
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
10:59:04.302 [dispatcher-CoarseGrainedScheduler] INFO  org.apache.spark.scheduler.cluster.StandaloneSchedulerBackend$StandaloneDriverEndpoint - Asking each executor to shut down
10:59:04.334 [dispatcher-event-loop-10] INFO  org.apache.spark.MapOutputTrackerMasterEndpoint - MapOutputTrackerMasterEndpoint stopped!
10:59:04.350 [stop-spark-context] INFO  org.apache.spark.storage.memory.MemoryStore - MemoryStore cleared
10:59:04.350 [stop-spark-context] INFO  org.apache.spark.storage.BlockManager - BlockManager stopped
10:59:04.365 [stop-spark-context] INFO  org.apache.spark.storage.BlockManagerMaster - BlockManagerMaster stopped
10:59:04.365 [dispatcher-event-loop-6] INFO  org.apache.spark.scheduler.OutputCommitCoordinator$OutputCommitCoordinatorEndpoint - OutputCommitCoordinator stopped!
10:59:04.384 [stop-spark-context] INFO  org.apache.spark.SparkContext - Successfully stopped SparkContext
10:59:04.637 [main] WARN  org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration$JpaWebConfiguration - spring.jpa.open-in-view is enabled by default. Therefore, database queries may
