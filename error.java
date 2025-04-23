Dataset<Row> rowDataset = sparkSession.read()
                   .format("jdbc")
                   .option("url", url)
                   .option("user", user)
                   .option("password", password)
                   .option("dbtable", table)
                   .option("driver", "oracle.jdbc.OracleDriver")
                   .load();
       /* Dataset<Row> datasetRow = sparkSession.read().option("header", true).option("inferSchema", true)
                .csv("data/employeeDb.csv").cache();*/
           return rowDataset.as(merchantOrderPayment);
