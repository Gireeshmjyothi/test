package com.epay.rns.service;

import com.sbi.epay.logging.utility.LoggerFactoryUtility;
import com.sbi.epay.logging.utility.LoggerUtility;
import lombok.RequiredArgsConstructor;
import org.apache.spark.sql.*;
import org.apache.spark.sql.functions;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReconService {

    private final LoggerUtility logger = LoggerFactoryUtility.getLogger(this.getClass());
    private final SparkSession sparkSession;
    private final JdbcReaderService jdbcReaderService;
    private final JdbcWriterService jdbcWriterService;

    private static final long START_TIMESTAMP = 1726138792000L;
    private static final long END_TIMESTAMP = 1726138793000L;

    public void reconProcess() {
        logger.info("Recon process started.");

        Dataset<Row> merchantOrderPayments = readAndNormalize("MERCHANT_ORDER_PAYMENTS", "CREATED_DATE");
        Dataset<Row> reconFileDtls = readAndNormalize("RECON_FILE_DTLS", "PAYMENT_DATE");

        reconFileDtls = renameColumns(reconFileDtls);
        Dataset<Row> merchantDeduped = merchantOrderPayments.dropDuplicates();
        Dataset<Row> reconDeduped = reconFileDtls.dropDuplicates();

        Column joinCond = merchantDeduped.col("ATRN_NUM").equalTo(reconDeduped.col("ATRN_NUM"));

        Column valueMatch = joinCond;
        for (String col : columnMapping().keySet()) {
            valueMatch = valueMatch.and(merchantDeduped.col(col).equalTo(reconDeduped.col(col)));
        }

        Dataset<Row> matched = merchantDeduped.join(reconDeduped, valueMatch, "inner");
        Dataset<Row> unmatched = merchantDeduped.join(reconDeduped, joinCond, "left_anti");
        Dataset<Row> sourceDuplicates = merchantOrderPayments.except(merchantDeduped);
        Dataset<Row> targetDuplicates = reconFileDtls.except(reconDeduped);

        logDataset("Matched Rows", matched);
        logDataset("Unmatched Rows", unmatched);
        logDataset("Source Duplicates", sourceDuplicates);
        logDataset("Target Duplicates", targetDuplicates);

        writeReconResult(matched, "MATCHED", "");
        writeReconResult(unmatched, "UNMATCHED", "Record not present in recon file");
        writeReconResult(sourceDuplicates, "DUPLICATE", "Duplicate in merchant source data");
        writeReconResult(targetDuplicates, "DUPLICATE", "Duplicate in recon target data");

        logger.info("Recon process completed.");
    }

    private Dataset<Row> readAndNormalize(String tableName, String dateColumn) {
        String query = buildQuery(tableName, dateColumn);
        logger.info("Executing query for table '{}': {}", tableName, query);
        Dataset<Row> dataset = jdbcReaderService.readFromDBWithFilter(query);
        return normalize(dataset);
    }

    private String buildQuery(String tableName, String dateColumn) {
        return String.format("(SELECT * FROM %s WHERE %s BETWEEN %d AND %d) filtered_data",
                tableName, dateColumn, START_TIMESTAMP, END_TIMESTAMP);
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
        dataset.show(false);
    }

    private Map<String, String> columnMapping() {
        Map<String, String> columnMappings = new HashMap<>();
        columnMappings.put("PAYMENT_STATUS", "STATUS");
        columnMappings.put("DEBIT_AMT", "PAYMENT_AMOUNT");
        columnMappings.put("ATRN_NUM", "ATRN_NUM");
        columnMappings.put("BANK_REFERENCE_NUMBER", "BANK_REFERENCE_NUMBER");
        return columnMappings;
    }

    private void writeReconResult(Dataset<Row> dataset, String matchStatus, String mismatchReason) {
        if (dataset.isEmpty()) return;

        Column[] columns = Arrays.stream(dataset.columns()).map(functions::col).toArray(Column[]::new);

        Dataset<Row> result = dataset.withColumn("match_status", functions.lit(matchStatus))
                .withColumn("mismatch_reason", functions.lit(mismatchReason))
                .withColumn("source_json", functions.to_json(functions.struct(columns)))
                .withColumn("recon_json", functions.to_json(functions.struct(columns))) // Optional: change logic if you want different JSONs
                .withColumn("reconciled_at", functions.current_timestamp())
                .withColumn("batch_date", functions.lit(Date.valueOf(LocalDate.now())))
                .select(
                        functions.col("ATRN_NUM").alias("atrn_num"),
                        functions.col("match_status"),
                        functions.col("mismatch_reason"),
                        functions.col("source_json"),
                        functions.col("recon_json"),
                        functions.col("reconciled_at"),
                        functions.col("batch_date")
                );

        jdbcWriterService.writeToOracle(result, "RECONCILIATION_RESULT");
    }
}
