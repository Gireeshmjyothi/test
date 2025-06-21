import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.expressions.Window;
import static org.apache.spark.sql.functions.*;

public class ReconProcessor {

    public static void process(SparkSession spark, Dataset<Row> reconFileDtls, Dataset<Row> merchantDtls) {

        // Step 1: Get all exact matches (atrnNum + debitAmt)
        Dataset<Row> exactMatch = reconFileDtls.alias("r")
            .join(merchantDtls.alias("m"),
                col("r.atrnNum").equalTo(col("m.atrnNum"))
                    .and(col("r.debitAmt").equalTo(col("m.debitAmt"))),
                "inner")
            .select(col("r.rfdId"), col("r.atrnNum"), col("r.debitAmt"));

        // Apply row_number() to ensure only one (atrnNum, debitAmt) is matched
        WindowSpec windowSpec = Window.partitionBy("atrnNum", "debitAmt").orderBy("rfdId");
        Dataset<Row> rankedMatches = exactMatch.withColumn("rn", row_number().over(windowSpec));

        Dataset<Row> trueMatches = rankedMatches
            .filter(col("rn").equalTo(1))
            .drop("rn");

        // Step 2: Get duplicates (same atrnNum but different debitAmt or extra entries with same match key)
        Dataset<Row> matchedIds = trueMatches.select("rfdId");

        Dataset<Row> duplicates = reconFileDtls.alias("r")
            .join(merchantDtls.alias("m"), col("r.atrnNum").equalTo(col("m.atrnNum")))
            .filter(
                col("r.debitAmt").notEqual(col("m.debitAmt")) // same atrnNum, different amount
                    .or(reconFileDtls.col("rfdId").notEqual(trueMatches.col("rfdId"))) // extra entries
            )
            .filter(col("r.rfdId").notEqual(trueMatches.col("rfdId")))
            .select(col("r.rfdId"), col("r.atrnNum"), col("r.debitAmt"))
            .except(trueMatches); // ensure no overlap

        // Step 3: Unmatched: rfdId not in matched or duplicate
        Dataset<Row> matchedAndDuplicateIds = matchedIds.union(duplicates.select("rfdId")).distinct();

        Dataset<Row> unmatched = reconFileDtls.join(matchedAndDuplicateIds, "rfdId", "left_anti");

        // Print output
        System.out.println("== MATCHED ==");
        trueMatches.orderBy("rfdId").show();

        System.out.println("== DUPLICATE ==");
        duplicates.orderBy("rfdId").show();

        System.out.println("== UNMATCHED ==");
        unmatched.orderBy("rfdId").show();
    }
}
