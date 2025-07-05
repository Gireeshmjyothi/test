private Dataset<Row> loadTxt(String path,  FileConfigDto fileConfigDto) {
        Dataset<Row> df = sparkConfig.sparkSession().read()
                .format("csv")
                .option("header", false)
                .option("delimiter", fileConfigDto.getDelimiter())  // Change if needed
                .option("inferSchema", "true")
                .load(path);
        return mapColumn(df, fileConfigDto);
    }

Hash:4C62C790BF5D9814F4F661154C7AA8D52B9AEDDCF06F475E7AE50AF30588D456,Count:87,Amount:3954134.93
id^atrnNum^debitAmt^date
1^UAoHUrJQXz^3^10/07/2025
2^YxfbQLXcEZ^3^10/07/2025
3^pUfvqBKcnr^3^10/07/2025
4^RYFeZJzEsQ^3^10/07/2025
