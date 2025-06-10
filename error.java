// Subscribe to multiple topics
Dataset<Row> df = spark
  .readStream()
  .format("kafka")
  .option("kafka.bootstrap.servers", "host1:port1,host2:port2")
  .option("subscribe", "topic1,topic2")
  .load();
df.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)");
