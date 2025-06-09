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

    public void reconProcess(String rfsId) {
        logger.info("Recon process started.");
        long processStartTime = currentTimeMillis();
        logger.info("🚀 Recon process started at : {} ", sparkService.formatMillis(processStartTime));

        // Load datasets
        logger.info("🚀 fetch merchant order payments starts : {} ", currentTimeMillis());
        long localStartTime = currentTimeMillis();
        Dataset<Row> merchantOrderPayments = readAndNormalize("MERCHANT_ORDER_PAYMENTS", "CREATED_DATE", 1726138792000L, 1726138792000L).alias("src");
        logger.info("🚀 fetch merchant order payments ends : {}", sparkService.formatMillis(currentTimeMillis() - localStartTime));

        logger.info("🚀 fetch recon file details starts : {}", currentTimeMillis());
        localStartTime = currentTimeMillis();
        Dataset<Row> reconFileDtls = readAndNormalize("RECON_FILE_DTLS", "PAYMENT_DATE", 1726138792000L, 1726138792000L).alias("tgt");
        logger.info("🚀 fetch recon file details ends : {}", sparkService.formatMillis(currentTimeMillis() - localStartTime));

        // Rename recon file columns to match source
        reconFileDtls = renameColumns(reconFileDtls).alias("recon");

        // Join and match logic
        Column joinCond = reconFileDtls.col("ATRN_NUM").equalTo(merchantOrderPayments.col("ATRN_NUM"))
                .and(reconFileDtls.col("DEBIT_AMT").equalTo(merchantOrderPayments.col("DEBIT_AMT")));

        Column valueMatch = joinCond;
        for (String col : columnMapping().keySet()) {
            valueMatch = valueMatch.and(reconFileDtls.col(col).equalTo(merchantOrderPayments.col(col)));
        }

        //Filter only unique ATRN_NUMs from reconFileDtls
        Dataset<Row> reconUnique = reconFileDtls.groupBy("ATRN_NUM")
                .count()
                .filter("count = 1")
                .select("ATRN_NUM");

        Dataset<Row> filteredRecon = reconFileDtls.join(reconUnique, "ATRN_NUM");

        //Join unique-only records to find matched ones
        Column matchCondition = filteredRecon.col("ATRN_NUM").equalTo(merchantOrderPayments.col("ATRN_NUM"))
                .and(filteredRecon.col("DEBIT_AMT").equalTo(merchantOrderPayments.col("DEBIT_AMT")));

        // Matched rows
        logger.info("🚀 Fetch matched data starts: {}", currentTimeMillis());
        localStartTime = currentTimeMillis();
        Dataset<Row> matched = filteredRecon
                .join(merchantOrderPayments, matchCondition, "inner")
                .dropDuplicates("ATRN_NUM");
        logger.info("🚀 Fetch matched data ends at: {}", sparkService.formatMillis(currentTimeMillis() - localStartTime));

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

        // Update match_status back in RECON_FILE_DTLS
        logger.info("🚀 Update process data starts: {}", sparkService.formatMillis(currentTimeMillis()));
        localStartTime = currentTimeMillis();
        /*updateReconFileDetails(matched, "MATCHED");
        updateReconFileDetails(unmatched, "UNMATCHED");
        updateReconFileDetails(reconFileDetailDuplicate, "DUPLICATE");*/
        matched.printSchema();
        matched.col("RFD_ID");
        jdbcReaderService.stageReconStatus(matched, "MATCHED");
        jdbcReaderService.stageReconStatus(unmatched, "UNMATCHED");
        jdbcReaderService.stageReconStatus(reconFileDetailDuplicate, "DUPLICATE");
        jdbcReaderService.updateReconFromStage();
        jdbcReaderService.clearStageTable();
        logger.info("🚀 Update process data ends: {}", sparkService.formatMillis(currentTimeMillis() - localStartTime));

        logger.info("Recon process completed in: {}", sparkService.formatMillis(currentTimeMillis() - processStartTime));
    }

    private Dataset<Row> getDuplicates(Dataset<Row> dataset, String... keyCols) {
        Column[] groupCols = Arrays.stream(keyCols).map(functions::col).toArray(Column[]::new);
        Dataset<Row> duplicates = dataset.groupBy(groupCols).count().filter("count > 1");
        return duplicates.join(dataset, JavaConverters.asScalaBufferConverter(Arrays.asList(keyCols)).asScala().toSeq(), "inner");
    }

    private Dataset<Row> readAndNormalize(String tableName, String dateColumn, long startTime, long endTime) {
        String query = buildQuery(tableName, dateColumn, startTime, endTime);
        logger.info("Executing query for table '{}': {}", tableName, query);
        return normalize(jdbcReaderService.readFromDBWithFilter(tableName));
    }

    private String buildQuery(String tableName, String dateColumn, long startTime, long endTime) {
        return String.format("(SELECT * FROM %s WHERE %s BETWEEN %d AND %d) filtered_data", tableName, dateColumn, startTime, endTime);
    }

    private Dataset<Row> readAndNormalize(String tableName, String column, String value) {
        String query = buildQuery(tableName, column, value);
        logger.info("Executing query for table '{}': {}", tableName, query);
        return normalize(jdbcReaderService.readFromDBWithFilter(tableName));
    }

    private String buildQuery(String tableName, String column, String value) {
        return String.format("(SELECT * FROM %s WHERE %s = %s) filtered_data", tableName, column, value);
    }

    private Dataset<Row> normalize(Dataset<Row> dataset) {
        for (String col : columnMapping().keySet()) {
            if (Arrays.asList(dataset.columns()).contains(col)) {
                dataset = dataset.withColumn(col, functions.trim(dataset.col(col)));
            }
        }
        if (Arrays.asList(dataset.columns()).contains("ATRN_NUM")) {
            dataset = dataset.withColumn("ATRN_NUM", functions.trim(dataset.col("ATRN_NUM")));
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
        columnMappings.put("PAYMENT_STATUS", "STATUS");

        return columnMappings;
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

    private void updateReconFileDetails(Dataset<Row> dataset, String status) {
        Dataset<Row> updated = dataset.select(functions.col("recon.ATRN_NUM"))
                .withColumn("RECON_STATUS", functions.lit(status)).distinct();

        String stageTableName = "RECON_FILE_DTLS_STAGE";
        jdbcReaderService.writeToStageTable(updated, stageTableName);
        jdbcReaderService.mergeStageToMain(stageTableName, "RECON_FILE_DTLS", "ATRN_NUM");
    }
}
