private Dataset<Row> loadTxt(String path, FileConfigDto fileConfigDto) {
    int skipLines = fileConfigDto.getHeaderRowIndex(); // = 1 in your case
    boolean hasHeader = fileConfigDto.isHasHeader();   // = false here

    Dataset<String> lines = sparkConfig.sparkSession().read().textFile(path);

    // Step 1: Skip metadata lines
    Dataset<String> dataLines = lines.rdd()
            .zipWithIndex()
            .filter(t -> t._2() >= skipLines)
            .map(Tuple2::_1, Encoders.STRING())
            .toDS();

    // Step 2: Load as CSV
    Dataset<Row> raw = sparkConfig.sparkSession().read()
            .format("csv")
            .option("delimiter", fileConfigDto.getDelimiter())
            .option("header", false) // no header in this file
            .load(dataLines);

    // Step 3: Map columns
    return mapColumn(raw, fileConfigDto.getMapColumn(), false);
}
