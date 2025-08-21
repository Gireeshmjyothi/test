import static org.apache.spark.sql.functions.*;
import org.apache.spark.sql.Row;

Dataset<Row> df = dataset; // your incoming DF

// 1) Verify schema
df.printSchema();

// 2) Ensure numeric column for partitioning
String partCol = "RFD_ID";
if (!df.schema().apply(partCol).dataType().typeName().matches("(?i)byte|short|int|long|float|double|decimal")) {
  df = df.withColumn("RFD_ID_NUM", col(partCol).cast("long"));
  partCol = "RFD_ID_NUM";
}

// 3) Handle empty DF
long cnt = df.count();
if (cnt == 0L) {
  logger.warn("No rows to write."); 
  return;
}

// 4) Compute bounds safely
Row b = df.agg(min(col(partCol)).alias("mn"), max(col(partCol)).alias("mx")).first();
long lower = b.getAs("mn");
long upper = b.getAs("mx");

// 5) Repartition on the actual column used
int partitions = 96; // set to executors*cores
df = df.repartition(partitions, col(partCol));

// 6) Write
df.write()
  .format("jdbc")
  .option("url", System.getProperty("dbUrl"))
  .option("user", System.getProperty("dbUser"))
  .option("password", System.getProperty("dbPassword"))
  .option("driver", "oracle.jdbc.OracleDriver")
  .option("dbtable", tableName)
  .option("batchsize", "50000")
  .option("isolationLevel", "NONE")
  .option("partitionColumn", partCol)     // must match the numeric column above
  .option("lowerBound", String.valueOf(lower))
  .option("upperBound", String.valueOf(upper))
  .option("numPartitions", String.valueOf(partitions))
  .mode(SaveMode.Append)
  .save();
