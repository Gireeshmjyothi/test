import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;

import java.util.Arrays;

saveToReconciliationTable(matched, "MATCHED", true);
saveToReconciliationTable(unmatched, "UNMATCHED", false);
saveToReconciliationTable(sourceDuplicates, "SOURCE_DUPLICATE", false);
saveToReconciliationTable(targetDuplicates, "TARGET_DUPLICATE", false);

private void saveToReconciliationTable(Dataset<Row> dataset, String matchStatus, boolean isMatched) {
        Dataset<Row> resultDataset = dataset.withColumn("match_status", functions.lit(matchStatus))
                .withColumn("mismatch_reason", functions.lit(isMatched ? null : "Data mismatch"))
                .withColumn("source_json", functions.to_json(structFromColumns(dataset.columns())))
                .withColumn("recon_json", functions.to_json(structFromColumns(dataset.columns())))
                .withColumn("reconciled_at", functions.current_timestamp())
                .withColumn("batch_date", functions.current_date());

        resultDataset.select(
                        "ATRN_NUM", "match_status", "mismatch_reason",
                        "source_json", "recon_json", "reconciled_at", "batch_date"
                ).write()
                .format("jdbc")
                .option("url", "jdbc:oracle:thin:@<HOST>:<PORT>:<SID>") // replace with your actual values
                .option("dbtable", "RECONCILIATION_RESULT")
                .option("user", "<USERNAME>")
                .option("password", "<PASSWORD>")
                .option("driver", "oracle.jdbc.OracleDriver")
                .mode("append")
                .save();
    }

    private Column structFromColumns(String[] columns) {
        Column[] cols = Arrays.stream(columns)
                .map(functions::col)
                .toArray(Column[]::new);
        return functions.struct(cols);
    }
