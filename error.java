package com.epay.rns.service;

import com.sbi.epay.logging.utility.LoggerFactoryUtility;
import com.sbi.epay.logging.utility.LoggerUtility;
import lombok.RequiredArgsConstructor;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;
import org.springframework.stereotype.Service;
import scala.collection.JavaConverters;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.lang.System.currentTimeMillis;
import static org.apache.spark.sql.functions.lit;

@Service
@RequiredArgsConstructor
public class ReconService {
    private final LoggerUtility logger = LoggerFactoryUtility.getLogger(this.getClass());
    private final JdbcReaderService jdbcReaderService;
    private final SparkService sparkService;


    public void reconProcess(long startTime, long endTime) {
        logger.info("Recon process started.");
        long processStartTime = currentTimeMillis();
        logger.info("🚀 Recon process started at : {} ", sparkService.formatMillis(processStartTime));
        // Load datasets
        logger.info("Reading merchant order payments from DB.");
        Dataset<Row> merchantOrderPayments = readAndNormalize("MERCHANT_ORDER_PAYMENTS", "CREATED_DATE", startTime, endTime)
                .alias("src");
        merchantOrderPayments.printSchema();
        logger.info("Reading recon file details from DB.");
        Dataset<Row> reconFileDtls = readAndNormalize("RECON_FILE_DTLS", "PAYMENT_DATE", startTime, endTime)
                .alias("tgt");

        reconFileDtls.printSchema();
        // Rename recon file columns to match source
        logger.info("Renaming recon file columns to match source schema.");
        reconFileDtls = renameColumns(reconFileDtls).alias("tgt");

        reconFileDtls.printSchema();
        // Deduplicate both datasets
        logger.info("Deduplicating merchant order payments.");
        Dataset<Row> merchantDeduped = merchantOrderPayments.dropDuplicates().alias("src");

        logger.info("Deduplicating recon file details.");
        Dataset<Row> reconDeduped = reconFileDtls.dropDuplicates().alias("tgt");

        // Join only on key columns (ATRN_NUM)
        Column joinCond = merchantDeduped.col("ATRN_NUM").equalTo(reconDeduped.col("ATRN_NUM"));

        // Value match on all mapped fields
        Column valueMatch = joinCond;
        for (String col : columnMapping().keySet()) {
            valueMatch = valueMatch.and(
                    merchantDeduped.col(col).equalTo(reconDeduped.col(col))
            );
        }

        // Matched records
        logger.info("Finding matched records.");
        Dataset<Row> matched = merchantDeduped.join(reconDeduped, valueMatch, "inner")
                .select("src.*"); // Take only source columns to avoid ambiguity

        // Unmatched records from source
        logger.info("Finding unmatched records (source not in target).");
        Dataset<Row> unmatched = merchantDeduped.join(reconDeduped, joinCond, "left_anti");

        // Duplicate Detection by ATRN_NUM
        logger.info("Finding duplicate records in source.");
        Dataset<Row> sourceDuplicates = getDuplicates(merchantOrderPayments, "ATRN_NUM")
                .join(merchantOrderPayments, "ATRN_NUM")
                .alias("src_dup");

        logger.info("Finding duplicate records in target.");
        Dataset<Row> targetDuplicates = getDuplicates(reconFileDtls, "ATRN_NUM")
                .join(reconFileDtls, "ATRN_NUM")
                .alias("tgt_dup");

        // Log summaries
        logDataset("Matched Rows", matched);
        logDataset("Unmatched Rows", unmatched);
        logDataset("Target Duplicates", targetDuplicates);

        // Save results
        logger.info("Inserting reconciliation results into DB.");
        saveToReconciliationTable(matched, "MATCHED", "matched");
        saveToReconciliationTable(unmatched, "UNMATCHED", "atrn not matched");
        saveToReconciliationTable(targetDuplicates, "TARGET_DUPLICATE", "duplicate atrn in recon file details");
        logger.info("time took to calculate and process : {}", sparkService.formatMillis(currentTimeMillis() - processStartTime));
        logger.info("Recon process completed.");
    }

    private Dataset<Row> getDuplicates(Dataset<Row> dataset, String... keyCols) {
        Column[] groupCols = Arrays.stream(keyCols)
                .map(functions::col)
                .toArray(Column[]::new);
        Dataset<Row> duplicates = dataset.groupBy(groupCols)
                .count()
                .filter("count > 1");

        return duplicates.join(dataset, JavaConverters.asScalaBufferConverter(Arrays.asList(keyCols)).asScala().toSeq(), "inner");
    }

    private Dataset<Row> readAndNormalize(String tableName, String dateColumn, long startTime, long endTime) {
        String query = buildQuery(tableName, dateColumn, startTime, endTime);
        logger.info("Executing query for table '{}': {}", tableName, query);
        Dataset<Row> dataset = jdbcReaderService.readFromDBWithFilter(query);
        return normalize(dataset);
    }

    private String buildQuery(String tableName, String dateColumn, long startTime, long endTime) {
        return String.format("(SELECT * FROM %s WHERE %s BETWEEN %d AND %d) filtered_data",
                tableName, dateColumn, startTime, endTime);
    }

    private Dataset<Row> normalize(Dataset<Row> dataset) {
        for (String col : columnMapping().keySet()) {
            if (Arrays.asList(dataset.columns()).contains(col)) {
                dataset = dataset.withColumn(col, functions.upper(functions.trim(dataset.col(col))));
            }
        }

        if (Arrays.asList(dataset.columns()).contains("ATRN_NUM")) {
            dataset = dataset.withColumn("ATRN_NUM", functions.upper(functions.trim(dataset.col("ATRN_NUM"))));
        }

        return dataset;
    }

    private Dataset<Row> renameColumns(Dataset<Row> dataset) {
        for (Map.Entry<String, String> entry : columnMapping().entrySet()) {
            dataset = dataset.withColumnRenamed(entry.getValue(), entry.getKey());
        }
        return dataset;
    }

    private void logDataset(String label, Dataset<Row> dataset) {
        logger.info("{} count: {}", label, dataset.count());
        dataset.show(false); // false = don't truncate
    }

    private Map<String, String> columnMapping() {
        Map<String, String> columnMappings = new HashMap<>();
        columnMappings.put("PAYMENT_STATUS", "STATUS");
        columnMappings.put("DEBIT_AMT", "PAYMENT_AMOUNT");
        columnMappings.put("ATRN_NUM", "ATRN_NUM");
        columnMappings.put("BANK_REFERENCE_NUMBER", "BANK_REFERENCE_NUMBER");
        return columnMappings;
    }

    private void saveToReconciliationTable(Dataset<Row> dataset, String matchStatus, String reason) {
        Dataset<Row> resultDataset = dataset.withColumn("match_status", lit(matchStatus))
                .withColumn("mismatch_reason", lit(reason))
                .withColumn("source_json", functions.to_json(structFromColumns(dataset.columns())))
                .withColumn("recon_json", functions.to_json(structFromColumns(dataset.columns())))
                .withColumn("reconciled_at", functions.current_timestamp())
                .withColumn("batch_date", functions.current_date());

        resultDataset.printSchema();
        jdbcReaderService.writeToReconFileResult(resultDataset, "RECONCILIATION_RESULT");
    }

    private Column structFromColumns(String[] columns) {
        Column[] cols = Arrays.stream(columns)
                .map(functions::col)
                .toArray(Column[]::new);
        return functions.struct(cols);
    }

}
