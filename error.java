public Dataset<Row> loadTxt(String path, FileConfigDto fileConfigDto) {
    SparkSession spark = sparkConfig.sparkSession();

    int headerRowIndex = fileConfigDto.getHeaderRowIndex(); // e.g. 1, 2, etc.
    boolean hasHeader = fileConfigDto.isHasHeader();
    String delimiter = fileConfigDto.getDelimiter(); // e.g. "^"
    Map<String, Integer> columnMap = fileConfigDto.getMapColumn();

    // Step 1: Load full file as text
    Dataset<String> lines = spark.read().textFile(path);

    // Step 2: Skip lines before header/data start
    Dataset<String> actualLines = lines
            .javaRDD()
            .zipWithIndex()
            .filter(tuple -> tuple._2() >= (headerRowIndex - 1)) // 0-based
            .map(Tuple2::_1)
            .toDS();

    // Step 3: Read as CSV (header only if hasHeader is true)
    Dataset<Row> raw = spark.read()
            .format("csv")
            .option("delimiter", delimiter)
            .option("header", hasHeader)
            .load(actualLines);

    // Step 4: Map required columns
    return mapColumn(raw, columnMap, hasHeader);
}
