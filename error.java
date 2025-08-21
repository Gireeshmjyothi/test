public void writeDatasetIntoReconFileDetails(Dataset<Row> dataset, String tableName) {
    Dataset<Row> df = dataset;

    // 1) Verify schema
    df.printSchema();

    // 2) Pick a valid partition column from the DataFrame (not the DB auto-id RFD_ID)
    String partCol = "ROW_NUMBER"; // Or "RF_ID" if that's better for your case

    // Ensure partition column is numeric
    if (!df.schema().apply(partCol).dataType().typeName().matches("(?i)byte|short|int|long|float|double|decimal")) {
        String numericCol = partCol + "_NUM";
        df = df.withColumn(numericCol, col(partCol).cast("long"));
        partCol = numericCol;
    }

    // 3) Handle empty DF
    long cnt = df.count();
    if (cnt == 0L) {
        logger.warn("No rows to write.");
        return;
    }

    // 4) Compute bounds for partitioning
    Row b = df.agg(min(col(partCol)).alias("mn"), max(col(partCol)).alias("mx")).first();
    long lower = b.getAs("mn");
    long upper = b.getAs("mx");

    // 5) Repartition the DataFrame
    int partitions = 96; // tune based on executors * cores
    df = df.repartition(partitions, col(partCol));

    // 6) Write to DB (excluding RFD_ID since Oracle generates it automatically)
    df.write()
            .format("jdbc")
            .option("url", System.getProperty("dbUrl"))
            .option("user", System.getProperty("dbUser"))
            .option("password", System.getProperty("dbPassword"))
            .option("driver", "oracle.jdbc.OracleDriver")
            .option("dbtable", tableName)
            .option("batchsize", "50000")
            .option("isolationLevel", "NONE")
            .option("partitionColumn", partCol)       // from DataFrame
            .option("lowerBound", String.valueOf(lower))
            .option("upperBound", String.valueOf(upper))
            .option("numPartitions", String.valueOf(partitions))
            .mode(SaveMode.Append)
            .save();
}




--------

String partCol = "RF_ID"; // choose an existing column
if (!df.schema().apply(partCol).dataType().typeName()
      .matches("(?i)byte|short|int|long|float|double|decimal")) {
  df = df.withColumn(partCol + "_NUM", col(partCol).cast("long"));
  partCol = partCol + "_NUM";
}

Row r = df.agg(
    functions.count(lit(1)).alias("cnt"),
    functions.min(col(partCol)).alias("mn"),
    functions.max(col(partCol)).alias("mx")
).first();

long cnt = r.getAs("cnt");
if (cnt == 0L) return;

long lower = r.getAs("mn");
long upper = r.getAs("mx");

int partitions = 96; // align to cluster cores
df = df.repartition(partitions, col(partCol));

df.write()
  .format("jdbc")
  .option("url", System.getProperty("dbUrl"))
  .option("user", System.getProperty("dbUser"))
  .option("password", System.getProperty("dbPassword"))
  .option("driver", "oracle.jdbc.OracleDriver")
  .option("dbtable", tableName)
  .option("batchsize", "50000")
  .option("isolationLevel", "NONE")
  .option("partitionColumn", partCol)
  .option("lowerBound", String.valueOf(lower))
  .option("upperBound", String.valueOf(upper))
  .option("numPartitions", String.valueOf(partitions))
  .mode(SaveMode.Append)
  .save();
