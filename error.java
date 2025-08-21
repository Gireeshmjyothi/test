public void writeDatasetIntoReconFileDetails(Dataset<Row> dataset, String tableName) {
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
    }


2025-08-21 19:08:33.534 INFO | com.epay.operations.recon.spark.service.SparkReconProcessingService:95 | principal=  | scenario=ReconSparkAppMain | operation=main | correlation=19666db8-a144-46aa-a52e-378675f5abcb | reconProcessing | Successfully stopped SparkContext
2025-08-21 19:08:33.535 ERROR | com.epay.operations.recon.ReconSparkAppMain:72 | principal=  | scenario=ReconSparkAppMain | operation=main | correlation=19666db8-a144-46aa-a52e-378675f5abcb | main | Exception while the recon process, error message: [FIELD_NOT_FOUND] No such struct field `RFD_ID` in `ATRN_NUM`, `TXN_AMOUNT`, `BANK_REF_NUMBER`, `RECON_STATUS`, `RF_ID`, `MERCHANT_ID`, `ROW_NUMBER`, `REMARK`, `CREATED_DATE`, `UPDATED_DATE`. SQLSTATE: 42704
  
