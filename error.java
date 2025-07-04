public static Dataset<Row> renameColumnsUsingConfig(Dataset<Row> rawDataset, Map<String, Integer> fieldConfig) {
    // Build a mapping from index -> logical field name
    Map<Integer, String> indexToName = new HashMap<>();
    for (Map.Entry<String, Integer> entry : fieldConfig.entrySet()) {
        int zeroBasedIndex = entry.getValue() - 1;
        indexToName.put(zeroBasedIndex, entry.getKey());
    }

    // Get existing column names (like _c0, _c1...)
    String[] existingCols = rawDataset.columns();

    // Build new column list with renaming where applicable
    List<Column> renamedCols = new ArrayList<>();
    for (int i = 0; i < existingCols.length; i++) {
        String originalName = existingCols[i]; // e.g., _c0, _c1...
        if (indexToName.containsKey(i)) {
            renamedCols.add(rawDataset.col(originalName).alias(indexToName.get(i)));
        } else {
            // Keep original if not mapped
            renamedCols.add(rawDataset.col(originalName));
        }
    }

    // Apply renaming using select
    return rawDataset.select(renamedCols.toArray(new Column[0]));
}
