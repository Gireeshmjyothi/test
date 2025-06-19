private Dataset<Row> getDuplicates(Dataset<Row> dataset, String... keyCols) {
        Column[] groupCols = Arrays.stream(keyCols).map(functions::col).toArray(Column[]::new);
        Dataset<Row> duplicates = dataset.groupBy(groupCols).count().filter("count > 1");
        return duplicates.join(dataset, JavaConverters.asScalaBufferConverter(Arrays.asList(keyCols)).asScala().toSeq(), "inner");
    }
