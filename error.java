public Dataset<Row> mapColumn(Dataset<Row> raw, Map<String, Integer> config, boolean hasHeader) {
    List<Column> selectedColumns = new ArrayList<>();
    String[] rawColumns = raw.columns();  // cache column names for validation

    if (hasHeader) {
        for (String alias : config.keySet()) {
            if (Arrays.asList(rawColumns).contains(alias)) {
                selectedColumns.add(col(alias).alias(alias));
            } else {
                System.err.printf("⚠️ Column '%s' not found in header.%n", alias);
            }
        }
    } else {
        int totalCols = rawColumns.length;

        config.forEach((alias, position) -> {
            int index = position - 1;
            if (index >= 0 && index < totalCols) {
                selectedColumns.add(col("_c" + index).alias(alias));
            } else {
                System.err.printf("⚠️ Skipping '%s': index %d out of bounds (total columns: %d)%n",
                        alias, index + 1, totalCols);
            }
        });
    }

    if (selectedColumns.isEmpty()) {
        throw new IllegalArgumentException("❌ No valid columns mapped from configuration.");
    }

    Dataset<Row> result = raw.select(selectedColumns.toArray(new Column[0]));
    result.show(false);  // You can remove or toggle with a flag
    return result;
}
