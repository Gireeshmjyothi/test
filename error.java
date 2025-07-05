public Dataset<Row> mapColumn(Dataset<Row> raw, Map<String, String> config, boolean hasHeader) {
    List<Column> selectedColumns = new ArrayList<>();
    String[] rawColumns = raw.columns();
    int totalCols = rawColumns.length;

    for (Map.Entry<String, String> entry : config.entrySet()) {
        String alias = entry.getKey();
        String value = entry.getValue();

        // Skip if value is empty or null
        if (value == null || value.trim().isEmpty()) {
            System.out.printf("⚠️ Skipping '%s': no column index provided.%n", alias);
            continue;
        }

        try {
            int index = Integer.parseInt(value.trim()) - 1; // 1-based to 0-based

            if (index < 0 || index >= totalCols) {
                System.err.printf("⚠️ Skipping '%s': index %d out of bounds (dataset has %d columns)%n",
                        alias, index + 1, totalCols);
                continue;
            }

            String selectedCol = hasHeader ? rawColumns[index] : "_c" + index;
            selectedColumns.add(col(selectedCol).alias(alias));

        } catch (NumberFormatException e) {
            System.err.printf("❌ Invalid index for '%s': '%s' is not a valid number.%n", alias, value);
        }
    }

    if (selectedColumns.isEmpty()) {
        throw new IllegalArgumentException("❌ No valid column mappings found. Check your config.");
    }

    return raw.select(selectedColumns.toArray(new Column[0]));
}
