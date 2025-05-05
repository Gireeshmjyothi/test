25/05/05 18:50:13 ERROR ExecutorRunner: Error running executor
java.lang.IllegalStateException: Cannot find any build directories.
        at org.apache.spark.launcher.CommandBuilderUtils.checkState(CommandBuilderUtils.java:236)
        at org.apache.spark.launcher.AbstractCommandBuilder.getScalaVersion(AbstractCommandBuilder.java:241)
        at org.apache.spark.launcher.AbstractCommandBuilder.buildClassPath(AbstractCommandBuilder.java:195)
        at org.apache.spark.launcher.AbstractCommandBuilder.buildJavaCommand(AbstractCommandBuilder.java:118)
        at org.apache.spark.launcher.WorkerCommandBuilder.buildCommand(WorkerCommandBuilder.scala:39)
        at org.apache.spark.launcher.WorkerCommandBuilder.buildCommand(WorkerCommandBuilder.scala:45)
        at org.apache.spark.deploy.worker.CommandUtils$.buildCommandSeq(CommandUtils.scala:63)
        at org.apache.spark.deploy.worker.CommandUtils$.buildProcessBuilder(CommandUtils.scala:51)
        at org.apache.spark.deploy.worker.ExecutorRunner.org$apache$spark$deploy$worker$ExecutorRunner$$fetchAndRunExecutor(ExecutorRunner.scala:160)
        at org.apache.spark.deploy.worker.ExecutorRunner$$anon$1.run(ExecutorRunner.scala:80)
25/05/05 18:50:13 INFO Worker: Executor app-20250505185012-0000/9 finished with state FAILED message java.lang.IllegalStateException: Cannot find any build directories.
25/05/05 18:50:13 INFO ExternalShuffleBlockResolver: Clean up non-shuffle and non-RDD files associated with the finished executor 9
25/05/05 18:50:13 INFO ExternalShuffleBlockResolver: Executor is not registered (appId=app-20250505185012-0000, execId=9)
25/05/05 18:50:13 INFO ExternalShuffleBlockResolver: Application app-20250505185012-0000 removed, cleanupLocalDirs = true
25/05/05 18:50:13 INFO Worker: Cleaning up local directories for application app-20250505185012-0000
