private Dataset<Row> loadTxt(String path, FileConfigDto fileConfigDto) {
    int skipLines = fileConfigDto.getHeaderRowIndex();  // e.g., 1
    boolean hasHeader = fileConfigDto.isHasHeader();    // true or false

    // Step 1: Read full file as Dataset<String>
    Dataset<String> lines = sparkConfig.sparkSession().read().textFile(path);

    // Step 2: Skip header/metadata lines using `zipWithIndex`
    JavaRDD<String> filteredLines = lines.javaRDD()
            .zipWithIndex()
            .filter(tuple -> tuple._2() >= skipLines)
            .map(Tuple2::_1);

    // Step 3: Convert filteredLines back to Dataset<String>
    Dataset<String> dataLines = sparkConfig.sparkSession().createDataset(filteredLines.rdd(), Encoders.STRING());

    // Step 4: Write to temporary path (Spark CSV reader needs path or file input stream)
    String tempPath = "/tmp/cleaned_" + System.currentTimeMillis();
    dataLines.coalesce(1).write().text(tempPath);  // write clean lines as text (one per line)

    // Step 5: Now read it using CSV reader
    Dataset<Row> raw = sparkConfig.sparkSession().read()
            .format("csv")
            .option("delimiter", fileConfigDto.getDelimiter())
            .option("header", hasHeader)
            .load(tempPath);

    // Step 6: Map columns using config (always index-based)
    return mapColumn(raw, fileConfigDto.getMapColumn(), hasHeader);
}
