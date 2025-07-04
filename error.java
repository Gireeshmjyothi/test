public Dataset<Row> extractColumnsFromPipeFile(SparkSession spark, File file, Map<String, Integer> config) {
    // Step 1: Read file without header
    Dataset<Row> raw = spark.read()
            .option("delimiter", "|")
            .option("inferSchema", "false")
            .option("header", "false")
            .csv(file.getAbsolutePath());

    // Step 2: Prepare renamed columns based on config
    List<Column> selectedColumns = new ArrayList<>();

    for (Map.Entry<String, Integer> entry : config.entrySet()) {
        String alias = entry.getKey();
        int index = entry.getValue() - 1; // Convert 1-based to 0-based

        String rawColName = "_c" + index;
        selectedColumns.add(raw.col(rawColName).alias(alias));
    }

    // Step 3: Select only the desired columns
    return raw.select(selectedColumns.toArray(new Column[0]));
}
