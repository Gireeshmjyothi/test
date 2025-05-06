15:51:53.403 [main] INFO  org.apache.spark.sql.internal.SharedState - Warehouse path is 'file:/E:/Gireesh_M/springboot-jsp-spark-demo%202/springboot-jsp-spark-demo/spark-warehouse'.
15:51:53.659 [stop-spark-context] INFO  org.apache.spark.SparkContext - SparkContext is stopping with exitCode 0.
15:51:53.666 [stop-spark-context] INFO  org.apache.spark.scheduler.cluster.StandaloneSchedulerBackend - Shutting down all executors
15:51:53.659 [dispatcher-event-loop-1] ERROR org.apache.spark.rpc.netty.Inbox - Ignoring error
org.apache.spark.SparkException: Exiting due to error from cluster scheduler: Master removed our application: FAILED
	at org.apache.spark.errors.SparkCoreErrors$.clusterSchedulerError(SparkCoreErrors.scala:291) ~[spark-core_2.12-3.5.5.jar:3.5.5]
