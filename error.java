extracted txt file Dataset : raw
+---+----------+--------+-------------+-------------------------+---+---------------+-------------------------+-----------------+
|1  |UAoHUrJQXz|20250303|12316895|55535|3  |535|aggagb  |asglkg|
+---+----------+--------+-------------+-------------------------+---+---------------+-------------------------+-----------------+
|2  |YxfbQLXcEZ|20250303|12316895|55535|3  |535 |aggagb |asglkg|
|3  |pUfvqBKcnr|20250303|12316895|55535|3  |535 |aggagb |asglkg|
|4  |RYFeZJzEsQ|20250303|12316895|55535|3  |535 |aggagb |asglkg|
|5  |RYFeZJzEsQ|20250303|12316895|55535|3  |535 |aggagb |asglkg|
|6  |1AoHUrJQXz|20250303|12316895|55535|3  |535 |aggagb |asglkg|
+---+----------+--------+-------------+-------------------------+---+---------------+-------------------------+-----------------+

need to map the column to the above dataset: 

public Dataset<Row> mapColumn(Dataset<Row> raw, Map<String, Integer> config) {

        raw.show(false);

        // Step 2: Prepare renamed columns based on config
        List<Column> selectedColumns = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : config.entrySet()) {
            String alias = entry.getKey();
            int index = entry.getValue() - 1; // Convert 1-based to 0-based

            String rawColName = "_c" + index;
            selectedColumns.add(raw.col(rawColName).alias(alias));
        }

        // Step 3: Select only the desired columns
        Dataset<Row> finalResult = raw.select(selectedColumns.toArray(new Column[0]));
        finalResult.show(false);
        return finalResult;
    }


2025-07-04 18:51:14.278 ERROR | com.epay.rns.dao.AdminDao:45 | principal=  | scenario= | operation= | correlation= | loadFileConfig | Unexpected error occurred in scheduled task
org.apache.spark.sql.AnalysisException: [UNRESOLVED_COLUMN.WITH_SUGGESTION] A column or function parameter with name `_c1` cannot be resolved. Did you mean one of the following? [`1`, `UAoHUrJQXz`, `20250303`, `1437190306713`, `4172120250303000100440401`, `3`, `018300100043524`, `Savings Bank-Resident`, `25030311490458577`].
	at org.apache.spark.sql.errors.QueryCompilationErrors$.unresolvedColumnWithSuggestionError(QueryCompilationErrors.scala:3109)
	at org.apache.spark.sql.errors.QueryCompilationErrors$.resolveException(QueryCompilationErrors.scala:3117)

Expected out:
+---+----------+
|atrn|paymentAmt|
+---+----------+
|UAoHUrJQXz|3  |
|YxfbQLXcEZ|3  |
|pUfvqBKcnr|3  |
|RYFeZJzEsQ|3  |
|RYFeZJzEsQ|3  |
|1AoHUrJQXz|3  |
+---+----------+

