Dataset<Row> df = dataset.persist(); // if reused

long[] bounds = df.agg(expr("min(RFD_ID) AS mn"), expr("max(RFD_ID) AS mx"))
                  .as(Encoders.tuple(Encoders.LONG(), Encoders.LONG()))
                  .first();

df = df.repartition(96, functions.col("RFD_ID")); // match cluster parallelism

df.write()
  .format("jdbc")
  .option("url", System.getProperty("dbUrl"))
  .option("user", System.getProperty("dbUser"))
  .option("password", System.getProperty("dbPassword"))
  .option("driver", "oracle.jdbc.OracleDriver")
  .option("dbtable", tableName)
  .option("batchsize", "50000")
  .option("isolationLevel", "NONE")
  .option("partitionColumn", "RFD_ID")
  .option("lowerBound", String.valueOf(bounds[0]))
  .option("upperBound", String.valueOf(bounds[1]))
  .option("numPartitions", "96")
  .option("sessionInitStatement", "ALTER SESSION ENABLE PARALLEL DML")
  .mode(SaveMode.Append)
  .save();
