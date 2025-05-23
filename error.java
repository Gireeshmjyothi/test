 "C:\Software\java-21-openjdk-21.0.3.0.9-1.win.jdk.x86_64\java-21-openjdk-21.0.3.0.9-1.win.jdk.x86_64\bin\java" "-cp" "F:\software\spark-3.5.5-bin-hadoop3\bin\..\conf\;F:\software\spark-3.5.5-bin-hadoop3\jars\*" "-Xmx1024M" "-Dspark.driver.port=64970" "-Djava.net.preferIPv6Addresses=false" "-XX:+IgnoreUnrecognizedVMOptions" "--add-opens=java.base/java.lang=ALL-UNNAMED" "--add-opens=java.base/java.lang.invoke=ALL-UNNAMED" "--add-opens=java.base/java.lang.reflect=ALL-UNNAMED" "--add-opens=java.base/java.io=ALL-UNNAMED" "--add-opens=java.base/java.net=ALL-UNNAMED" "--add-opens=java.base/java.nio=ALL-UNNAMED" "--add-opens=java.base/java.util=ALL-UNNAMED" "--add-opens=java.base/java.util.concurrent=ALL-UNNAMED" "--add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED" "--add-opens=java.base/jdk.internal.ref=ALL-UNNAMED" "--add-opens=java.base/sun.nio.ch=ALL-UNNAMED" "--add-opens=java.base/sun.nio.cs=ALL-UNNAMED" "--add-opens=java.base/sun.security.action=ALL-UNNAMED" "--add-opens=java.base/sun.util.calendar=ALL-UNNAMED" "--add-opens=java.security.jgss/sun.security.krb5=ALL-UNNAMED" "-Djdk.reflect.useDirectMethodHandle=false" "org.apache.spark.executor.CoarseGrainedExecutorBackend" "--driver-url" "spark://CoarseGrainedScheduler@DESKTOP-8ED0TAP.CORP.AD.SBI:64970" "--executor-id" "0" "--hostname" "10.30.64.27" "--cores" "12" "--app-id" "app-20250523122639-0000" "--worker-url" "spark://Worker@10.30.64.27:64556" "--resourceProfileId" "0"
25/05/23 12:27:51 INFO Worker: Asked to kill executor app-20250523122639-0000/0
25/05/23 12:27:51 INFO ExecutorRunner: Runner thread for executor app-20250523122639-0000/0 interrupted
25/05/23 12:27:51 INFO ExecutorRunner: Killing process!
25/05/23 12:27:51 INFO Worker: Executor app-20250523122639-0000/0 finished with state KILLED exitStatus 1
25/05/23 12:27:51 INFO ExternalShuffleBlockResolver: Clean up non-shuffle and non-RDD files associated with the finished executor 0
25/05/23 12:27:51 INFO ExternalShuffleBlockResolver: Executor is not registered (appId=app-20250523122639-0000, execId=0)
25/05/23 12:27:51 INFO ExternalShuffleBlockResolver: Application app-20250523122639-0000 removed, cleanupLocalDirs = true
25/05/23 12:27:51 INFO Worker: Cleaning up local directories for application app-20250523122639-0000
25/05/23 12:27:51 WARN TransportChannelHandler: Exception in connection from /10.30.64.27:64975
java.net.SocketException: Connection reset
        at java.base/sun.nio.ch.SocketChannelImpl.throwConnectionReset(SocketChannelImpl.java:401)
        at java.base/sun.nio.ch.SocketChannelImpl.read(SocketChannelImpl.java:434)
        at io.netty.buffer.PooledByteBuf.setBytes(PooledByteBuf.java:254)
        at io.netty.buffer.AbstractByteBuf.writeBytes(AbstractByteBuf.java:1132)
        at io.netty.channel.socket.nio.NioSocketChannel.doReadBytes(NioSocketChannel.java:357)
        at io.netty.channel.nio.AbstractNioByteChannel$NioByteUnsafe.read(AbstractNioByteChannel.java:151)
        at io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:788)
        at io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:724)
        at io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:650)
        at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:562)
        at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:997)
        at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)
        at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
        at java.base/java.lang.Thread.run(Thread.java:1583)
