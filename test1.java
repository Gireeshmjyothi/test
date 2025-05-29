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

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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
        logger.info("🚀 Recon process started at : {}", sparkService.formatMillis(processStartTime));

        // Load recon file details
        logger.info("🚀 Fetch recon file details starts: {}", currentTimeMillis());
        long localStartTime = currentTimeMillis();
        Dataset<Row> reconFileDtls = readAndNormalize("RECON_FILE_DTLS", "PAYMENT_DATE", startTime, endTime);
        logger.info("🚀 Fetch recon file details ends: {}", sparkService.formatMillis(currentTimeMillis() - localStartTime));

        // Load merchant order payments to check if data exists in reference
        logger.info("🚀 Fetch merchant order payments starts: {}", currentTimeMillis());
        localStartTime = currentTimeMillis();
        Dataset<Row> merchantOrderPayments = readAndNormalize("MERCHANT_ORDER_PAYMENTS", "CREATED_DATE", startTime, endTime).dropDuplicates("ATRN_NUM");
        logger.info("🚀 Fetch merchant order payments ends: {}", sparkService.formatMillis(currentTimeMillis() - localStartTime));

        // Rename reconFileDtls columns to match merchantOrderPayments
        reconFileDtls = renameColumns(reconFileDtls).alias("recon");

        // Matched based on ATRN_NUM and DEBIT_AMT (and other mapped fields)
        Column joinCond = reconFileDtls.col("ATRN_NUM").equalTo(merchantOrderPayments.col("ATRN_NUM"))
                .and(reconFileDtls.col("DEBIT_AMT").equalTo(merchantOrderPayments.col("DEBIT_AMT")));

        Column valueMatch = joinCond;
        for (String col : columnMapping().keySet()) {
            valueMatch = valueMatch.and(reconFileDtls.col(col).equalTo(merchantOrderPayments.col(col)));
        }

        // Matched rows
        logger.info("🚀 Fetch matched data starts: {}", currentTimeMillis());
        localStartTime = currentTimeMillis();
        Dataset<Row> matched = reconFileDtls.join(merchantOrderPayments, valueMatch, "inner").select("recon.*").dropDuplicates("ATRN_NUM");
        logger.info("🚀 Fetch matched data ends: {}", sparkService.formatMillis(currentTimeMillis() - localStartTime));

        // Unmatched rows from reconFileDtls not in merchantOrderPayments
        logger.info("🚀 Fetch unmatched data starts: {}", sparkService.formatMillis(currentTimeMillis()));
        localStartTime = currentTimeMillis();
        Dataset<Row> unmatched = reconFileDtls.join(merchantOrderPayments, joinCond, "left_anti").distinct();
        logger.info("🚀 Fetch unmatched data ends: {}", sparkService.formatMillis(currentTimeMillis() - localStartTime));

        // Duplicates in reconFileDtls
        logger.info("🚀 Fetch duplicate data starts: {}", sparkService.formatMillis(currentTimeMillis()));
        localStartTime = currentTimeMillis();
        Dataset<Row> reconFileDetailDuplicate = getDuplicates(reconFileDtls, "ATRN_NUM");
        logger.info("🚀 Fetch duplicate data ends: {}", sparkService.formatMillis(currentTimeMillis() - localStartTime));

        // Save results
        logger.info("🚀 Insert process data starts: {}", sparkService.formatMillis(currentTimeMillis()));
        localStartTime = currentTimeMillis();
        saveToReconciliationTable(matched, "MATCHED", "matched");
        saveToReconciliationTable(unmatched, "UNMATCHED", "atrn not matched");
        saveToReconciliationTable(reconFileDetailDuplicate, "TARGET_DUPLICATE", "duplicate atrn in recon file details");
        logger.info("🚀 Insert process data ends: {}", sparkService.formatMillis(currentTimeMillis() - localStartTime));

        logger.info("Recon process completed in: {}", sparkService.formatMillis(currentTimeMillis() - processStartTime));
    }

    private Dataset<Row> readAndNormalize(String tableName, String dateColumn, long startTime, long endTime) {
        String query = buildQuery(tableName, dateColumn, startTime, endTime);
        logger.info("Executing query for table '{}': {}", tableName, query);
        return normalize(jdbcReaderService.readFromDBWithFilter(tableName));
    }

    private String buildQuery(String tableName, String dateColumn, long startTime, long endTime) {
        return String.format("(SELECT * FROM %s WHERE %s BETWEEN %d AND %d) filtered_data", tableName, dateColumn, startTime, endTime);
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
            if (Arrays.asList(dataset.columns()).contains(entry.getValue())) {
                dataset = dataset.withColumnRenamed(entry.getValue(), entry.getKey());
            }
        }
        return dataset;
    }

    private Map<String, String> columnMapping() {
        Map<String, String> columnMappings = new HashMap<>();
        columnMappings.put("DEBIT_AMT", "PAYMENT_AMOUNT");
        columnMappings.put("ATRN_NUM", "ATRN_NUM");
        return columnMappings;
    }

    private Dataset<Row> getDuplicates(Dataset<Row> dataset, String... keyCols) {
        Column[] groupCols = Arrays.stream(keyCols).map(functions::col).toArray(Column[]::new);
        Dataset<Row> duplicates = dataset.groupBy(groupCols).count().filter("count > 1");
        return duplicates.join(dataset, JavaConverters.asScalaBufferConverter(Arrays.asList(keyCols)).asScala().toSeq(), "inner");
    }

    private void saveToReconciliationTable(Dataset<Row> dataset, String matchStatus, String reason) {
        Date currentTimeStamp = Date.valueOf(LocalDateTime.now().toLocalDate());
        Timestamp batchTime = Timestamp.valueOf(LocalDateTime.now());

        Dataset<Row> resultDataset = dataset
                .withColumn("match_status", lit(matchStatus))
                .withColumn("mismatch_reason", lit(reason))
                .withColumn("source_json", functions.to_json(functions.struct(Arrays.stream(dataset.columns()).map(functions::col).toArray(Column[]::new))))
                .withColumn("recon_json", functions.to_json(functions.struct(Arrays.stream(dataset.columns()).map(functions::col).toArray(Column[]::new))))
                .withColumn("reconciled_at", lit(currentTimeStamp))
                .withColumn("batch_date", lit(batchTime))
                .repartition(4);

        jdbcReaderService.writeToReconFileResult(resultDataset, "RECONCILIATION_RESULT");
    }
}
