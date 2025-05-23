rogram or batch file.
'#' is not recognized as an internal or external command,
operable program or batch file.
'#' is not recognized as an internal or external command,
operable program or batch file.
'#' is not recognized as an internal or external command,
operable program or batch file.
'#' is not recognized as an internal or external command,
operable program or batch file.
Setting default log level to "WARN".
To adjust logging level use sc.setLogLevel(newLevel). For SparkR, use setLogLevel(newLevel).
25/05/23 15:30:26 WARN Utils: The configured local directories are not expected to be URIs; however, got suspicious values [F:/spark/tmp]. Please check your configured local directories.
25/05/23 15:30:32 WARN NativeCodeLoader: Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
25/05/23 15:30:32 WARN SparkConf: Note that spark.local.dir will be overridden by the value set by the cluster manager (via SPARK_LOCAL_DIRS in mesos/standalone/kubernetes and LOCAL_DIRS in YARN).
25/05/23 15:30:35 WARN StandaloneAppClient$ClientEndpoint: Failed to connect to master 10.30.64.27:7077
org.apache.spark.SparkException: Exception thrown in awaitResult:
        at org.apache.spark.util.SparkThreadUtils$.awaitResult(SparkThreadUtils.scala:56)
        at org.apache.spark.util.ThreadUtils$.awaitResult(ThreadUtils.scala:310)
        at org.apache.spark.rpc.RpcTimeout.awaitResult(RpcTimeout.scala:75)
        at org.apache.spark.rpc.RpcEnv.setupEndpointRefByURI(RpcEnv.scala:102)
        at org.apache.spark.rpc.RpcEnv.setupEndpointRef(RpcEnv.scala:110)
        at org.apache.spark.deploy.client.StandaloneAppClient$ClientEndpoint$$anon$1.run(StandaloneAppClient.scala:108)
        at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:572)
        at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:317)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642)
        at java.base/java.lang.Thread.run(Thread.java:1583)
Caused by: java.io.IOException: Failed to connect to /10.30.64.27:7077
        at org.apache.spark.network.client.TransportClientFactory.createClient(TransportClientFactory.java:294)
        at org.apache.spark.network.client.TransportClientFactory.createClient(TransportClientFactory.java:214)
        at org.apache.spark.network.client.TransportClientFactory.createClient(TransportClientFactory.java:226)
        at org.apache.spark.rpc.netty.NettyRpcEnv.createClient(NettyRpcEnv.scala:204)
        at org.apache.spark.rpc.netty.Outbox$$anon$1.call(Outbox.scala:202)
        at org.apache
