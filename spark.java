import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import static org.apache.spark.sql.functions.*;

public class ReconProcessor {

    public static void process(SparkSession spark, Dataset<Row> reconFileDtls, Dataset<Row> merchantDtls) {

        // Step 1: Exact match (atrnNum + debitAmt)
        Dataset<Row> matched = reconFileDtls
            .join(merchantDtls,
                reconFileDtls.col("atrnNum").equalTo(merchantDtls.col("atrnNum"))
                    .and(reconFileDtls.col("debitAmt").equalTo(merchantDtls.col("debitAmt"))),
                "inner")
            .select(reconFileDtls.col("rfdId"), reconFileDtls.col("atrnNum"), reconFileDtls.col("debitAmt"));

        // Step 2: Duplicate: atrnNum match, but debitAmt mismatch
        Dataset<Row> potentialDuplicates = reconFileDtls
            .join(merchantDtls, "atrnNum")
            .filter(reconFileDtls.col("debitAmt").notEqual(merchantDtls.col("debitAmt")))
            .select(reconFileDtls.col("rfdId"), reconFileDtls.col("atrnNum"), reconFileDtls.col("debitAmt"));

        // Step 3: Remove already matched rfdIds from duplicates
        Dataset<Row> duplicates = potentialDuplicates
            .join(matched.select("rfdId"), "rfdId", "left_anti");

        // Step 4: Unmatched: not in matched, not in duplicates
        Dataset<Row> allMatchedOrDupIds = matched
            .select("rfdId")
            .union(duplicates.select("rfdId"))
            .distinct();

        Dataset<Row> unmatched = reconFileDtls
            .join(allMatchedOrDupIds, "rfdId", "left_anti");

        // Final Output
        System.out.println("✅ Matched:");
        matched.show(false);

        System.out.println("🔁 Duplicates:");
        duplicates.show(false);

        System.out.println("❌ Unmatched:");
        unmatched.show(false);
    }
}
