import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import static org.apache.spark.sql.functions.*;
import org.apache.spark.sql.expressions.Window;
import org.apache.spark.sql.expressions.WindowSpec;

public class ReconProcessor {

    public static void process(SparkSession spark, Dataset<Row> reconFileDtls, Dataset<Row> merchantDtls) {
        // Step 1: Join recon with merchant on atrnNum and debitAmt (exact match)
        Dataset<Row> exactMatch = reconFileDtls
            .join(merchantDtls,
                reconFileDtls.col("atrnNum").equalTo(merchantDtls.col("atrnNum"))
                    .and(reconFileDtls.col("debitAmt").equalTo(merchantDtls.col("debitAmt"))),
                "inner"
            )
            .select(reconFileDtls.col("rfdId"), reconFileDtls.col("atrnNum"), reconFileDtls.col("debitAmt"));

        // Step 2: Deduplicate exact matches — keep only first match per (atrnNum, debitAmt)
        WindowSpec matchWindow = Window.partitionBy("atrnNum", "debitAmt").orderBy("rfdId");

        Dataset<Row> exactMatchWithRow = exactMatch.withColumn("row_num", row_number().over(matchWindow));

        Dataset<Row> matched = exactMatchWithRow
            .filter(col("row_num").equalTo(1))
            .drop("row_num");

        Dataset<Row> extraMatches = exactMatchWithRow
            .filter(col("row_num").gt(1))
            .drop("row_num");

        // Step 3: Get all reconFileDtls that have atrnNum in merchant (partial match)
        Dataset<Row> partialMatches = reconFileDtls
            .join(merchantDtls, "atrnNum");

        // Step 4: Mark duplicates: 
        // - extra exact matches (from step 2)
        // - or same atrnNum but different debitAmt (excluding matched ones)
        Dataset<Row> remainingPartialDupes = partialMatches
            .except(matched)
            .except(extraMatches);

        Dataset<Row> duplicates = extraMatches
            .union(remainingPartialDupes)
            .select("rfdId", "atrnNum", "debitAmt")
            .distinct();

        // Step 5: Unmatched = not in matched or duplicate
        Dataset<Row> allHandledIds = matched.union(duplicates).select("rfdId").distinct();

        Dataset<Row> unmatched = reconFileDtls
            .join(allHandledIds, "rfdId", "left_anti");

        // OUTPUT
        System.out.println("✅ Matched:");
        matched.show(false);

        System.out.println("🔁 Duplicates:");
        duplicates.show(false);

        System.out.println("❌ Unmatched:");
        unmatched.show(false);
    }
}
