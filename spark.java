public Dataset<Row> loadTransactionFile(SparkSession spark, String filePath, FileConfig config) {
    Dataset<Row> df;

    DataFrameReader reader = spark.read()
            .option("header", String.valueOf(config.hasHeader))
            .option("inferSchema", "true")
            .option("delimiter", ",");

    // Choose format
    switch (config.fileFormat.toLowerCase()) {
        case "csv":
        case "txt":
            df = reader.csv(filePath);
            break;
        case "xls":
        case "xlsx":
            df = reader.format("com.crealytics.spark.excel")
                       .option("dataAddress", "'Sheet1'!A1")
                       .load(filePath);
            break;
        default:
            throw new IllegalArgumentException("Unsupported file format");
    }

    // If header present: Rename based on header names
    if (config.hasHeader) {
        df = df
            .withColumnRenamed(config.atrnColumnName, "ATRN_NUM")
            .withColumnRenamed(config.debitAmtColumnName, "DEBIT_AMT")
            .select("ATRN_NUM", "DEBIT_AMT");
    } else {
        // If no header: map by index
        String atrnCol = "_c" + config.atrnColumnIndex;
        String amtCol = "_c" + config.debitAmtColumnIndex;

        df = df.select(col(atrnCol).as("ATRN_NUM"), col(amtCol).as("DEBIT_AMT"));
    }

    return df;
}

df = df.filter(col("ATRN_NUM").isNotNull().and(col("DEBIT_AMT").isNotNull()));
recommended
df = df.filter(col("ATRN_NUM").isNotNull().and(col("DEBIT_AMT").isNotNull()));

df = df.na().drop(new String[]{"ATRN_NUM", "DEBIT_AMT"});
