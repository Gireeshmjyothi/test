public Dataset<Row> mapColumn(Dataset<Row> raw, Map<String, Integer> config, boolean hasHeader) {
    List<Column> selectedColumns = new ArrayList<>();
    String[] rawColumns = raw.columns();
    int totalCols = rawColumns.length;

    for (Map.Entry<String, Integer> entry : config.entrySet()) {
        String alias = entry.getKey();
        int index = entry.getValue() - 1; // 1-based to 0-based

        if (index < 0 || index >= totalCols) {
            System.err.printf("⚠️ Skipping '%s': index %d is out of bounds (dataset has %d columns)%n", alias, entry.getValue(), totalCols);
            continue;
        }

        String selectedCol = hasHeader ? rawColumns[index] : "_c" + index;
        selectedColumns.add(col(selectedCol).alias(alias));
    }

    if (selectedColumns.isEmpty()) {
        throw new IllegalArgumentException("❌ No valid columns mapped from configuration.");
    }

    return raw.select(selectedColumns.toArray(new Column[0]));
}
