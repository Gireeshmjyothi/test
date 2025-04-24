 private Dataset<MerchantOrderPayment> getDbDataSet() {

        try {
            String url = "jdbc:oracle:thin:@10.177.134.124:1590:epaydbdev1";
            String user = "GIREESH_M";
            String password = "GIREESH_M";
            String table = "MERCHANT_ORDER_PAYMENTS";
            Properties connectionProperties = new Properties();
            connectionProperties.put("user", user);
            connectionProperties.put("password", password);
            connectionProperties.put("driver", "oracle.jdbc.OracleDriver");

            Dataset<Row> datasetRow = sparkSession.read().option("header", true).option("inferSchema", true)
                    .jdbc(url, table, connectionProperties);

            /* Dataset<Row> datasetRow = sparkSession.read().option("header", true).option("inferSchema", true)
                .csv("data/employeeDb.csv").cache();*/
            datasetRow.printSchema();
            Dataset<Row> dataset = datasetRow.withColumnRenamed("MERCHANT_ID", "mid")
                    .withColumnRenamed("ORDER_REF_NUMBER", "orderRefNumber")
                    .withColumnRenamed("SBI_ORDER_REF_NUMBER", "sbiOrderRefNumber")
                    .withColumnRenamed("ATRN_NUM", "atrnNum")
                    .withColumnRenamed("DEBIT_AMT", "debitAmt");
            dataset.printSchema();
            return dataset.as(merchantOrderPayment);
        } catch (Exception ex) {
            log.info("Error while connect to SQL : {} ", ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }
