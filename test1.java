
package com.example.sparkcompare;

import org.apache.spark.sql.*; import org.apache.spark.sql.types.StructType; import org.springframework.beans.factory.annotation.Value; import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct; import java.util.HashMap; import java.util.Map;

@Service public class TableComparisonService {

@Value("${oracle.url}")
private String dbUrl;

@Value("${oracle.user}")
private String dbUser;

@Value("${oracle.password}")
private String dbPassword;

private final String dbDriver = "oracle.jdbc.OracleDriver";
private SparkSession sparkSession;

@PostConstruct
public void init() {
    sparkSession = SparkSession.builder()
            .appName("TableComparisonService")
            .master("local[*]")
            .getOrCreate();
}

public Dataset<Row> readFromDBWithFilter(String tableName, String whereClause) {
    String query = String.format("(SELECT * FROM %s WHERE %s) AS filtered_data", tableName, whereClause);

    return sparkSession.read()
            .format("jdbc")
            .option("url", dbUrl)
            .option("driver", dbDriver)
            .option("dbtable", query)
            .option("user", dbUser)
            .option("password", dbPassword)
            .load();
}

public void compareTables(String sourceTable, String targetTable, String sourceWhere, String targetWhere, Map<String, String> columnMappings) {
    Dataset<Row> sourceDF = readFromDBWithFilter(sourceTable, sourceWhere);
    Dataset<Row> targetDF = readFromDBWithFilter(targetTable, targetWhere);

    // Rename columns in source and target to common names
    for (Map.Entry<String, String> entry : columnMappings.entrySet()) {
        sourceDF = sourceDF.withColumnRenamed(entry.getKey(), entry.getValue());
        targetDF = targetDF.withColumnRenamed(entry.getValue(), entry.getValue());
    }

    // Deduplicate
    Dataset<Row> dedupSource = sourceDF.dropDuplicates();
    Dataset<Row> dedupTarget = targetDF.dropDuplicates();

    // Join on key
    Column joinCond = dedupSource.col("id").equalTo(dedupTarget.col("id"));

    // Matched rows (key + all mapped column values equal)
    Column valueMatch = joinCond;
    for (String col : columnMappings.values()) {
        valueMatch = valueMatch.and(dedupSource.col(col).equalTo(dedupTarget.col(col)));
    }

    Dataset<Row> matched = dedupSource.join(dedupTarget, valueMatch, "inner");

    // Unmatched (left anti join to get rows in source not in target)
    Dataset<Row> unmatched = dedupSource.join(dedupTarget, joinCond, "left_anti");

    // Duplicates (if any)
    Dataset<Row> sourceDuplicates = sourceDF.except(dedupSource);
    Dataset<Row> targetDuplicates = targetDF.except(dedupTarget);

    System.out.println("--- Matched Rows ---");
    matched.show();

    System.out.println("--- Unmatched Rows ---");
    unmatched.show();

    System.out.println("--- Source Duplicates ---");
    sourceDuplicates.show();

    System.out.println("--- Target Duplicates ---");
    targetDuplicates.show();
}

// Example usage
public void runComparison() {
    Map<String, String> columnMappings = new HashMap<>();
    columnMappings.put("source_id", "id");
    columnMappings.put("source_name", "name");
    columnMappings.put("source_salary", "salary");

    String sourceWhere = "create_date >= TO_DATE('2024-01-01', 'YYYY-MM-DD')";
    String targetWhere = "created_on >= TO_DATE('2024-01-01', 'YYYY-MM-DD')";

    compareTables("SOURCE_TABLE", "TARGET_TABLE", sourceWhere, targetWhere, columnMappings);
}

}

