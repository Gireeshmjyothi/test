import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.expressions.Window;
import org.apache.spark.sql.expressions.WindowSpec;
import static org.apache.spark.sql.functions.*;

public Dataset<Row> getOneMatchedPerGroup(Dataset<Row> reconFileDtls, Dataset<Row> merchantDtls) {
    // Step 1: Join on atrnNum and debitAmt
    Dataset<Row> matched = reconFileDtls
        .join(merchantDtls,
            reconFileDtls.col("atrnNum").equalTo(merchantDtls.col("atrnNum"))
                .and(reconFileDtls.col("debitAmt").equalTo(merchantDtls.col("debitAmt"))),
            "inner"
        )
        .select(reconFileDtls.col("rfdId"), reconFileDtls.col("atrnNum"), reconFileDtls.col("debitAmt"));

    // Step 2: Keep only one record per (atrnNum, debitAmt)
    WindowSpec windowSpec = Window.partitionBy("atrnNum", "debitAmt").orderBy("rfdId");
    Dataset<Row> onePerGroup = matched
        .withColumn("row_num", row_number().over(windowSpec))
        .filter(col("row_num").equalTo(1))
        .drop("row_num");

    return onePerGroup;
}
