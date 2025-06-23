import static org.apache.spark.sql.functions.*;

import org.apache.spark.sql.*;
import org.apache.spark.sql.expressions.Window;
import org.apache.spark.sql.expressions.WindowSpec;

public class ReconciliationStatus {

    public static Dataset<Row> markMatchedStatus(Dataset<Row> reconFileDtls, Dataset<Row> merchantDtls) {
        SparkSession spark = reconFileDtls.sparkSession();

        // 1. Create match window by atrnNum + debitAmt
        WindowSpec matchWindow = Window
                .partitionBy("atrnNum", "debitAmt")
                .orderBy("rfdId");

        // 2. Join recon with merchant on exact match of atrnNum + debitAmt
        Dataset<Row> joined = reconFileDtls
            .join(merchantDtls,
                reconFileDtls.col("atrnNum").equalTo(merchantDtls.col("atrnNum"))
                .and(reconFileDtls.col("debitAmt").equalTo(merchantDtls.col("debitAmt"))),
                "left_outer"
            )
            .withColumn("exactMatch", when(merchantDtls.col("atrnNum").isNotNull(), lit(1)).otherwise(lit(0)))
            .withColumn("match_rank", when(col("exactMatch").equalTo(1), row_number().over(matchWindow)));

        // 3. Find atrnNums that are matched at least once
        Dataset<Row> matchedAtrns = joined
            .filter(col("match_rank").equalTo(1))
            .select("atrnNum")
            .distinct()
            .withColumnRenamed("atrnNum", "matchedAtrn");

        // 4. Join back to tag isAtrnMatched
        Dataset<Row> withMatchedFlag = joined
            .join(matchedAtrns, joined.col("atrnNum").equalTo(matchedAtrns.col("matchedAtrn")), "left_outer")
            .withColumn("isAtrnMatched", col("matchedAtrn").isNotNull());

        // 5. Apply logic for status assignment
        Dataset<Row> finalStatus = withMatchedFlag.withColumn("status",
            when(col("exactMatch").equalTo(1).and(col("match_rank").equalTo(1)), lit("MATCHED"))
            .when(col("exactMatch").equalTo(1).and(col("match_rank").gt(1)), lit("DUPLICATE"))
            .when(col("exactMatch").equalTo(0).and(col("isAtrnMatched").equalTo(true)), lit("DUPLICATE"))
            .otherwise(lit("UNMATCHED"))
        );

        // 6. Return final result
        return finalStatus.select("rfdId", "atrnNum", "debitAmt", "status");
    }
}
