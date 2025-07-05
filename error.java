public Dataset<Row> loadTxt(String path, FileConfigDto fileConfigDto) {
    SparkSession spark = sparkConfig.sparkSession();
    int skipLines = fileConfigDto.getHeaderRowIndex(); // e.g., 1
    boolean hasHeader = fileConfigDto.isHasHeader();   // e.g., true or false
    String delimiter = fileConfigDto.getDelimiter();   // e.g., "^"

    // Step 1: Read full text file as lines
    Dataset<String> lines = spark.read().textFile(path);

    // Step 2: If header exists in a certain line (e.g., line 1), extract it
    String headerLine = hasHeader ? lines.take(skipLines + 1)[skipLines] : null;

    // Step 3: Skip the header/metadata lines
    Dataset<String> dataLines = lines.rdd()
        .zipWithIndex()
        .filter(t -> t._2() >= skipLines + (hasHeader ? 1 : 0))
        .map(Tuple2::_1, Encoders.STRING());

    // Step 4: Convert raw lines to single-column Dataset for Spark CSV reader
    Dataset<Row> raw = spark.read()
        .format("csv")
        .option("delimiter", delimiter)
        .option("header", false)
        .load(dataLines);

    // Step 5: If header exists, assign actual column names for better readability
    if (hasHeader && headerLine != null) {
        String[] columnNames = headerLine.split(Pattern.quote(delimiter));
        for (int i = 0; i < columnNames.length; i++) {
            raw = raw.withColumnRenamed("_c" + i, columnNames[i].trim());
        }
    }

    // Step 6: Map columns using the provided config
    return mapColumn(raw, fileConfigDto.getMapColumn(), hasHeader);
}
