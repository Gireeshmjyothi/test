
  starting org.apache.spark.deploy.master.Master, logging to ...\logs\spark-username-org.apache.spark.deploy.master.Master-1-MACHINE.out
 
 Start a worker process and connect to the master (you’ll get the master URL from step 1 output):
 %SPARK_HOME%\sbin\start-worker.cmd spark://localhost:7077
 
 You’ll see:
 starting org.apache.spark.deploy.worker.Worker, logging to ...\logs\spark-username-org.apache.spark.deploy.worker.Worker-1-MACHINE.out
 
 implementation('org.apache.spark:spark-core_2.13:3.5.5') {
     exclude group: 'org.eclipse.jetty'
     exclude group: 'javax.servlet'
 }
 implementation('org.apache.spark:spark-sql_2.13:3.5.5') {
     exclude group: 'org.eclipse.jetty'
     exclude group: 'javax.servlet'
 }
 
 
 
  Stop Master:
 %SPARK_HOME%\sbin\stop-master.cmd
 
 Stop Worker:
 %SPARK_HOME%\sbin\stop-worker.cmd
 
 
 java -cp "jars/*" org.apache.spark.deploy.worker.Worker spark://10.30.64.27:7077
 java -cp "jars/*" org.apache.spark.deploy.master.Master
