import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.expressions.Window;
import org.apache.spark.sql.expressions.WindowSpec;

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

        // Step 2: From exact matches, keep only first occurrence per (atrnNum, debitAmt)
        WindowSpec windowSpec = Window.partitionBy("atrnNum", "debitAmt").orderBy("rfdId");

        Dataset<Row> rankedMatches = exactMatch.withColumn("rn", row_number().over(windowSpec));

        Dataset<Row> matched = rankedMatches
                .filter(col("rn").equalTo(1))
                .drop("rn");

        // Step 3: Identify all reconFile rows that have same atrnNum as merchantDtls (joined)
        Dataset<Row> joinedOnAtrn = reconFileDtls.alias("r")
                .join(merchantDtls.alias("m"), col("r.atrnNum").equalTo(col("m.atrnNum")), "inner")
                .select(col("r.rfdId"), col("r.atrnNum"), col("r.debitAmt"));

        // Step 4: Duplicates = joinedOnAtrn - matched
        Dataset<Row> duplicates = joinedOnAtrn
                .join(matched.select("rfdId"), "rfdId", "left_anti");

        // Step 5: Unmatched = reconFileDtls - (matched + duplicates)
        Dataset<Row> matchedAndDuplicates = matched.union(duplicates).select("rfdId");

        Dataset<Row> unmatched = reconFileDtls
                .join(matchedAndDuplicates, "rfdId", "left_anti");

        // Final Outputs
        System.out.println("== ✅ MATCHED ==");
        matched.orderBy("rfdId").show();

        System.out.println("== 🔁 DUPLICATES ==");
        duplicates.orderBy("rfdId").show();

        System.out.println("== ❌ UNMATCHED ==");
        unmatched.orderBy("rfdId").show();
    }
}
