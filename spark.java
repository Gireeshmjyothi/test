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



public File saveTempFile(InputStream inputStream, String fileName) throws IOException {
    File tempFile = File.createTempFile("upload-", "-" + fileName);
    try (OutputStream out = new FileOutputStream(tempFile)) {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
    }
    return tempFile;
}

File tempFile = saveTempFile(fileInputStream, "transactions.csv");
Dataset<Row> df = loadTransactionDataset(spark, tempFile.getAbsolutePath(), fileType, hasHeader, columnMapping);



public Dataset<Row> loadTransactionDataset(SparkSession spark, String filePath, String fileType, boolean hasHeader, Map<String, String> columnMapping) {
    return switch (fileType.toLowerCase()) {
        case "csv" -> loadCsv(spark, filePath, hasHeader, columnMapping);
        case "txt" -> loadTxt(spark, filePath, hasHeader, columnMapping);
        case "xls", "xlsx" -> loadExcel(spark, filePath, hasHeader, columnMapping);
        default -> throw new IllegalArgumentException("Unsupported file type: " + fileType);
    };
}

private Dataset<Row> loadCsv(SparkSession spark, String path, boolean hasHeader, Map<String, String> columnMapping) {
    Dataset<Row> df = spark.read()
            .format("csv")
            .option("header", String.valueOf(hasHeader))
            .option("inferSchema", "true")
            .load(path);
    return cleanAndRename(df, columnMapping);
}

private Dataset<Row> loadTxt(SparkSession spark, String path, boolean hasHeader, Map<String, String> columnMapping) {
    Dataset<Row> df = spark.read()
            .format("csv") // txt treated as CSV if comma/tab delimited
            .option("header", String.valueOf(hasHeader))
            .option("delimiter", ",")  // Change if needed
            .option("inferSchema", "true")
            .load(path);
    return cleanAndRename(df, columnMapping);
}

private Dataset<Row> loadExcel(SparkSession spark, String path, boolean hasHeader, Map<String, String> columnMapping) {
    Dataset<Row> df = spark.read()
            .format("com.crealytics.spark.excel")
            .option("dataAddress", "'Sheet1'!A1")
            .option("header", String.valueOf(hasHeader))
            .option("inferSchema", "true")
            .option("treatEmptyValuesAsNulls", "true")
            .option("addColorColumns", "false")
            .load(path);
    return cleanAndRename(df, columnMapping);
}


private Dataset<Row> cleanAndRename(Dataset<Row> df, Map<String, String> columnMapping) {
    for (Map.Entry<String, String> entry : columnMapping.entrySet()) {
        if (df.columns() != null && Arrays.asList(df.columns()).contains(entry.getKey())) {
            df = df.withColumnRenamed(entry.getKey(), entry.getValue());
        }
    }

    // Remove empty or null rows
    return df.filter(col(columnMapping.get("ATRN_NUM")).isNotNull()
            .and(col(columnMapping.get("DEBIT_AMT")).isNotNull()));
}

Map<String, String> columnMap = Map.of(
    "col1", "ATRN_NUM",
    "col2", "DEBIT_AMT"
);
