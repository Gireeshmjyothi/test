import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.expressions.Window;
import org.apache.spark.sql.expressions.WindowSpec;
import static org.apache.spark.sql.functions.*;

public Dataset<Row>[] classifyReconData(Dataset<Row> reconFileDtls, Dataset<Row> merchantDtls) {

    // 1. Matched: exact (atrnNum, debitAmt) - keep only one match
    Dataset<Row> matched = reconFileDtls
        .join(merchantDtls,
            reconFileDtls.col("atrnNum").equalTo(merchantDtls.col("atrnNum"))
            .and(reconFileDtls.col("debitAmt").equalTo(merchantDtls.col("debitAmt"))),
            "inner"
        )
        .select(reconFileDtls.col("rfdId"), reconFileDtls.col("atrnNum"), reconFileDtls.col("debitAmt"));

    // Use row_number to select one match per (atrnNum, debitAmt)
    WindowSpec windowSpec = Window.partitionBy("atrnNum", "debitAmt").orderBy("rfdId");
    Dataset<Row> matchedFinal = matched
        .withColumn("row_num", row_number().over(windowSpec))
        .filter(col("row_num").equalTo(1))
        .drop("row_num");

    // 2. Remaining = reconFileDtls - matchedFinal
    Dataset<Row> remaining = reconFileDtls.join(matchedFinal, "rfdId", "left_anti");

    // 3. Separate unmatched and duplicates

    // Get all atrnNums from merchant
    Dataset<Row> merchantAtrnNums = merchantDtls.select("atrnNum").distinct();

    // 3.1 Unmatched: atrnNum not present in merchantDtls
    Dataset<Row> unmatched = remaining.join(merchantAtrnNums, "atrnNum", "left_anti");

    // 3.2 Duplicates: atrnNum present in merchant but debitAmt does not match
    Dataset<Row> duplicate = remaining.join(merchantAtrnNums, "atrnNum", "inner");

    // Return all three as array
    return new Dataset[]{matchedFinal, duplicate, unmatched};
}
