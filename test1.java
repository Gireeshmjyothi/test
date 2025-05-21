package com.epay.rns.service;

import com.sbi.epay.logging.utility.LoggerFactoryUtility;
import com.sbi.epay.logging.utility.LoggerUtility;
import lombok.RequiredArgsConstructor;
import org.apache.spark.sql.*;
import org.apache.spark.sql.functions;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReconService {

    private final LoggerUtility logger = LoggerFactoryUtility.getLogger(this.getClass());
    private final SparkSession sparkSession;
    private final JdbcReaderService jdbcReaderService;

    private static final long START_TIMESTAMP = 1726138792000L;
    private static final long END_TIMESTAMP = 1726138793000L;

    public void reconProcess() {
        logger.info("Recon process started.");

        logger.info("Reading merchant order payments from DB.");
        Dataset<Row> merchantOrderPayments = readAndNormalize("MERCHANT_ORDER_PAYMENTS", "CREATED_DATE");

        logger.info("Reading recon file details from DB.");
        Dataset<Row> reconFileDtls = readAndNormalize("RECON_FILE_DTLS", "PAYMENT_DATE");

        logger.info("Renaming recon file columns to match source schema.");
        reconFileDtls = renameColumns(reconFileDtls);

        logger.info("Deduplicating merchant order payments.");
        Dataset<Row> merchantDeduped = merchantOrderPayments.dropDuplicates();

        logger.info("Deduplicating recon file details.");
        Dataset<Row> reconDeduped = reconFileDtls.dropDuplicates();

        Column joinCond = merchantDeduped.col("ATRN_NUM").equalTo(reconDeduped.col("ATRN_NUM"));
        Column valueMatch = joinCond;
        for (String col : columnMapping().keySet()) {
            valueMatch = valueMatch.and(merchantDeduped.col(col).equalTo(reconDeduped.col(col)));
        }

        logger.info("Finding matched records.");
        Dataset<Row> matched = merchantDeduped.join(reconDeduped, valueMatch, "inner");

        logger.info("Finding unmatched records.");
        Dataset<Row> unmatched = merchantDeduped.join(reconDeduped, joinCond, "left_anti");

        logger.info("Finding duplicate records in source.");
        Dataset<Row> sourceDuplicates = merchantOrderPayments.except(merchantDeduped);

        logger.info("Finding duplicate records in target.");
        Dataset<Row> targetDuplicates = reconFileDtls.except(reconDeduped);

        logDataset("Matched Rows", matched);
        logDataset("Unmatched Rows", unmatched);
        logDataset("Source Duplicates", sourceDuplicates);
        logDataset("Target Duplicates", targetDuplicates);

        // Write results to RECONCILIATION_RESULT
        writeToReconResult(matched
                .withColumn("match_status", functions.lit("MATCHED"))
                .withColumn("mismatch_reason", functions.lit(null))
                .withColumn("source_json", functions.to_json(functions.struct(merchantDeduped.columns())))
                .withColumn("recon_json", functions.to_json(functions.struct(reconDeduped.columns())))
        );

        writeToReconResult(unmatched
                .withColumn("match_status", functions.lit("UNMATCHED"))
                .withColumn("mismatch_reason", functions.lit("Source missing in target"))
                .withColumn("source_json", functions.to_json(functions.struct(merchantDeduped.columns())))
                .withColumn("recon_json", functions.lit(null))
        );

        writeToReconResult(sourceDuplicates
                .withColumn("match_status", functions.lit("DUPLICATE_SOURCE"))
                .withColumn("mismatch_reason", functions.lit("Duplicate in source"))
                .withColumn("source_json", functions.to_json(functions.struct(merchantOrderPayments.columns())))
                .withColumn("recon_json", functions.lit(null))
        );

        writeToReconResult(targetDuplicates
                .withColumn("match_status", functions.lit("DUPLICATE_TARGET"))
                .withColumn("mismatch_reason", functions.lit("Duplicate in target"))
                .withColumn("source_json", functions.lit(null))
                .withColumn("recon_json", functions.to_json(functions.struct(reconFileDtls.columns())))
        );

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

    private void writeToReconResult(Dataset<Row> dataset) {
        dataset = dataset
                .withColumn("atrn_num", functions.col("ATRN_NUM"))
                .withColumn("reconciled_at", functions.current_timestamp())
                .withColumn("batch_date", functions.current_date());

        Dataset<Row> finalDataset = dataset.select(
                "atrn_num", "match_status", "mismatch_reason",
                "source_json", "recon_json", "reconciled_at", "batch_date"
        );

        finalDataset.write()
                .format("jdbc")
                .option("url", "jdbc:oracle:thin:@//<host>:<port>/<service>")
                .option("dbtable", "RECONCILIATION_RESULT")
                .option("user", "<username>")
                .option("password", "<password>")
                .option("driver", "oracle.jdbc.OracleDriver")
                .mode(SaveMode.Append)
                .save();
    }
            }
