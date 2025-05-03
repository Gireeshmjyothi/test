org.apache.spark.SparkException: Exception thrown in awaitResult: 
	at org.apache.spark.util.SparkThreadUtils$.awaitResult(SparkThreadUtils.scala:56) ~[spark-common-utils_2.13-3.5.5.jar:3.5.5]
	at org.apache.spark.util.ThreadUtils$.awaitResult(ThreadUtils.scala:310) ~[spark-core_2.13-3.5.5.jar:3.5.5]
	at org.apache.spark.rpc.RpcTimeout.awaitResult(RpcTimeout.scala:75) ~[spark-core_2.13-3.5.5.jar:3.5.5]
	at org.apache.spark.rpc.RpcEnv.setupEndpointRefByURI(RpcEnv.scala:102) ~[spark-core_2.13-3.5.5.jar:3.5.5]
	at org.apache.spark.rpc.RpcEnv.setupEndpointRef(RpcEnv.scala:110) ~[spark-core_2.13-3.5.5.jar:3.5.5]
	at org.apache.spark.deploy.client.StandaloneAppClient$ClientEndpoint$$anon$1.run(StandaloneAppClient.scala:108) ~[spark-core_2.13-3.5.5.jar:3.5.5]
	at java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:572) ~[?:?]
	at java.util.concurrent.FutureTask.run(FutureTask.java:317) ~[?:?]
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144) ~[?:?]
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642) ~[?:?]
	at java.lang.Thread.run(Thread.java:1583) [?:?]
Caused by: java.lang.RuntimeException: java.io.InvalidClassException: org.apache.spark.rpc.netty.RpcEndpointVerifier$CheckExistence; local class incompatible: stream classdesc serialVersionUID = 5378738997755484868, local class serialVersionUID = 7789290765573734431
	at java.base/java.io.ObjectStreamClass.initNonProxy(ObjectStreamClass.java:598)
