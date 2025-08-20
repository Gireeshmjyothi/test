public void writeDatasetIntoReconFileDetails(Dataset<Row> dataset, String tableName) {
        dataset.repartition(40)
                .write()
                .format("jdbc")
                .option("url", System.getProperty("dbUrl"))
                .option("user", System.getProperty("dbUser"))
                .option("password", System.getProperty("dbPassword"))
                .option("driver", "oracle.jdbc.OracleDriver")
                .option("dbtable", tableName)
                .option("batchsize", "1000")
                .option("numPartitions", "40")
                .option("isolationLevel", "NONE")
                .mode(SaveMode.Append)
                .save();
    }
