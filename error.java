public Dataset<Row> readAndNormalizeTransaction(UUID rfId) {
        String query = "MERCHANT_TXN"; // buildQueryForTransaction(rfId);
        logger.info("Executing query for : {}", query);
        return normalize(readFromDBWithFilter(query));
    }

    /**
     * This method is used to normalise the dataset columns.
     *
     * @param dataset required dataset.
     * @return normalised dataset.
     */
    public Dataset<Row> normalize(Dataset<Row> dataset) {
        logger.info("Normalising required data.");
        var columns = Arrays.asList(dataset.columns());
        for (String col : MAPPING_COLUMNS.keySet()) {
            if (columns.contains(col)) {
                dataset = dataset.withColumn(col, functions.trim(dataset.col(col)));
            }
        }
        return dataset;
    }
