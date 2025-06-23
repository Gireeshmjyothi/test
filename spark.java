import org.apache.spark.sql.*;
import org.apache.spark.sql.expressions.Window;
import static org.apache.spark.sql.functions.*;

public class ReconProcessor {

    public static void process(SparkSession spark, Dataset<Row> reconFileDtls, Dataset<Row> merchantDtls) {
        // STEP 1: Add row_number for MATCHED prioritization
        Dataset<Row> joinedMatched = reconFileDtls
            .join(merchantDtls, reconFileDtls.col("atrnNum").equalTo(merchantDtls.col("atrnNum"))
                    .and(reconFileDtls.col("debitAmt").equalTo(merchantDtls.col("debitAmt"))),
                "left_outer"
            )
            .withColumn("matchFlag", when(merchantDtls.col("atrnNum").isNotNull(), lit(1)).otherwise(lit(0)));

        // STEP 2: Rank matched rows so only one is MATCHED, rest go to DUPLICATE
        WindowSpec matchRankWindow = Window.partitionBy("atrnNum", "debitAmt").orderBy("rfdId");

        Dataset<Row> ranked = joinedMatched
            .withColumn("match_rank", when(col("matchFlag").equalTo(1), row_number().over(matchRankWindow)));

        // STEP 3: Classify
        Dataset<Row> classified = ranked.withColumn("status",
            when(col("matchFlag").equalTo(1).and(col("match_rank").equalTo(1)), lit("MATCHED"))
            .when(col("matchFlag").equalTo(1).and(col("match_rank").gt(1)), lit("DUPLICATE"))
            .when(col("matchFlag").equalTo(0), lit("UNMATCHED"))
        );

        // STEP 4: Select final columns
        Dataset<Row> result = classified.select(
            col("rfdId"),
            col("atrnNum"),
            col("debitAmt"),
            col("status")
        );

        // Output categorized data
        System.out.println("== ✅ MATCHED ==");
        result.filter(col("status").equalTo("MATCHED")).show();

        System.out.println("== 🔁 DUPLICATES ==");
        result.filter(col("status").equalTo("DUPLICATE")).show();

        System.out.println("== ❌ UNMATCHED ==");
        result.filter(col("status").equalTo("UNMATCHED")).show();
    }
}
