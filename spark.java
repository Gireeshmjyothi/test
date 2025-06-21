import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import static org.apache.spark.sql.functions.*;

public class ReconProcessor {

    public static void process(SparkSession spark, Dataset<Row> reconFileDtls, Dataset<Row> merchantDtls) {

        // 1. ✅ Matched: Exact (atrnNum, debitAmt)
        Dataset<Row> matched = reconFileDtls
            .join(merchantDtls,
                reconFileDtls.col("atrnNum").equalTo(merchantDtls.col("atrnNum"))
                    .and(reconFileDtls.col("debitAmt").equalTo(merchantDtls.col("debitAmt"))),
                "inner")
            .select(reconFileDtls.col("*"))
            .distinct();

        // 2. 🔁 Duplicates: Same atrnNum, different debitAmt
        Dataset<Row> matchedKeys = matched.select("atrnNum", "debitAmt").distinct();

        // Remove matched rows from reconFileDtls to avoid duplication
        Dataset<Row> unmatchedByKey = reconFileDtls
            .join(matchedKeys,
                reconFileDtls.col("atrnNum").equalTo(matchedKeys.col("atrnNum"))
                    .and(reconFileDtls.col("debitAmt").equalTo(matchedKeys.col("debitAmt"))),
                "left_anti");

        // Now from the remaining, check if atrnNum exists in merchantDtls → duplicate
        Dataset<Row> merchantAtrns = merchantDtls.select("atrnNum").distinct();

        Dataset<Row> duplicates = unmatchedByKey
            .join(merchantAtrns, "atrnNum") // If atrnNum matches but not debitAmt
            .select(unmatchedByKey.col("*"))
            .distinct();

        // 3. ❌ Unmatched = not matched, not duplicate
        Dataset<Row> matchedAndDupIds = matched
            .select("rfdId")
            .union(duplicates.select("rfdId"))
            .distinct();

        Dataset<Row> unmatched = reconFileDtls
            .join(matchedAndDupIds, "rfdId", "left_anti");

        // Final Outputs
        System.out.println("✅ == MATCHED ==");
        matched.show(false);

        System.out.println("🔁 == DUPLICATES ==");
        duplicates.show(false);

        System.out.println("❌ == UNMATCHED ==");
        unmatched.show(false);
    }
}
