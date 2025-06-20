import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.expressions.Window;
import org.apache.spark.sql.expressions.WindowSpec;
import static org.apache.spark.sql.functions.*;

public Dataset<Row> getDuplicateAtrnNums(Dataset<Row> reconFileDtls, Dataset<Row> merchantDtls) {
    // Step 1: Perform inner join on (atrnNum, debitAmt) to find exact matches
    Dataset<Row> matched = reconFileDtls
        .join(merchantDtls,
            reconFileDtls.col("atrnNum").equalTo(merchantDtls.col("atrnNum"))
                .and(reconFileDtls.col("debitAmt").equalTo(merchantDtls.col("debitAmt"))),
            "inner"
        )
        .select(reconFileDtls.col("rfdId"), reconFileDtls.col("atrnNum"), reconFileDtls.col("debitAmt"));

    // Step 2: Use row_number to keep only one matched record per (atrnNum, debitAmt)
    WindowSpec windowSpec = Window.partitionBy("atrnNum", "debitAmt").orderBy("rfdId");
    Dataset<Row> deduplicatedMatched = matched
        .withColumn("row_num", row_number().over(windowSpec))
        .filter(col("row_num").equalTo(1))
        .drop("row_num");

    // Step 3: Remove this single matched record from reconFileDtls
    Dataset<Row> remaining = reconFileDtls.join(deduplicatedMatched, "rfdId", "left_anti");

    // Step 4: Find atrnNum values that occur more than once in the remaining records
    Dataset<Row> duplicateKeys = remaining.groupBy("atrnNum")
                                          .count()
                                          .filter("count > 1")
                                          .select("atrnNum");

    // Step 5: Return all rows from remaining that have duplicate atrnNum
    return remaining.join(duplicateKeys, "atrnNum");
}
