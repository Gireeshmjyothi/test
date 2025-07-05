public Dataset<Row> mapColumn(Dataset<Row> raw, Map<String, Integer> config, boolean hasHeader) {
    List<Column> selectedColumns = new ArrayList<>();
    String[] rawColumns = raw.columns();
    int totalCols = rawColumns.length;

    for (Map.Entry<String, Integer> entry : config.entrySet()) {
        String alias = entry.getKey();
        Integer index = entry.getValue();

        // Skip if index is null
        if (index == null) {
            System.out.printf("⚠️ Skipping '%s': no index provided (null value).%n", alias);
            continue;
        }

        int zeroBasedIndex = index - 1;

        if (zeroBasedIndex < 0 || zeroBasedIndex >= totalCols) {
            System.err.printf("⚠️ Skipping '%s': index %d is out of bounds (dataset has %d columns).%n",
                    alias, index, totalCols);
            continue;
        }

        String selectedCol = hasHeader ? rawColumns[zeroBasedIndex] : "_c" + zeroBasedIndex;
        selectedColumns.add(functions.col(selectedCol).alias(alias));
    }

    if (selectedColumns.isEmpty()) {
        throw new IllegalArgumentException("❌ No valid column mappings found. Check your config map.");
    }

    return raw.select(selectedColumns.toArray(new Column[0]));
}
