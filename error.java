private Dataset<Row> loadExcel(String path,  FileConfigDto fileConfigDto) {
        Dataset<Row> df = sparkConfig.sparkSession().read()
                .format("com.crealytics.spark.excel")
                .option("dataAddress", "'Sheet1'!A1")
                .option("header", fileConfigDto.getHeaderPresent())
                .option("inferSchema", "true")
                .option("treatEmptyValuesAsNulls", "true")
                .option("addColorColumns", "false")
                .load(path);
        return mapColumn(df, fileConfigDto);
    }
