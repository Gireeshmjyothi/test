package com.epay.rns.service;

import com.sbi.epay.logging.utility.LoggerFactoryUtility;
import com.sbi.epay.logging.utility.LoggerUtility;
import lombok.RequiredArgsConstructor;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReconService {
    private final LoggerUtility logger = LoggerFactoryUtility.getLogger(this.getClass());
    private final SparkSession sparkSession;

    private final JdbcReaderService jdbcReaderService;

    public void reconProcess() {
        logger.info("Recon process start.");
        String merchantOrderPaymentQuery = String.format(
                "(SELECT * FROM %s WHERE CREATED_DATE BETWEEN %d AND %d) filtered_data",
                "MERCHANT_ORDER_PAYMENTS", 1726138792000L, 1726138793000L
        );

        String reconFileDtlQuery = String.format(
                "(SELECT * FROM %s WHERE PAYMENT_DATE BETWEEN %d AND %d) filtered_data",
                "RECON_FILE_DTLS", 1726138792000L, 1726138793000L
        );

        logger.info("Fetching merchant order payments.");
        Dataset<Row> merchantOrderPayments = jdbcReaderService.readFromDBWithFilter(merchantOrderPaymentQuery);

        logger.info("Fetching recon file details.");
        Dataset<Row> reconFileDtls = jdbcReaderService.readFromDBWithFilter(reconFileDtlQuery);

        // Rename columns in source and target to common names
        for (Map.Entry<String, String> entry : columnMapping().entrySet()) {
            reconFileDtls = reconFileDtls.withColumnRenamed(entry.getValue(), entry.getKey());
        }

        // Deduplicate
        Dataset<Row> merchantOrderDropDuplicate = merchantOrderPayments.dropDuplicates();
        Dataset<Row> reconDropDuplicate = reconFileDtls.dropDuplicates();

        // Join on key
        Column joinCond = merchantOrderDropDuplicate.col("ATRN_NUM").equalTo(reconDropDuplicate.col("ATRN_NUM"));

        // Matched rows (key + all mapped column values equal)
        Column valueMatch = joinCond;
        for (String col : columnMapping().keySet()) {
            valueMatch = valueMatch.and(merchantOrderDropDuplicate.col(col).equalTo(reconDropDuplicate.col(col)));
        }

        Dataset<Row> matched = merchantOrderDropDuplicate.join(reconDropDuplicate, valueMatch, "inner");

        // Unmatched (left anti join to get rows in source not in target)
        Dataset<Row> unmatched = merchantOrderDropDuplicate.join(reconDropDuplicate, joinCond, "left_anti");

        // Duplicates (if any)
        Dataset<Row> sourceDuplicates = merchantOrderPayments.except(merchantOrderDropDuplicate);
        Dataset<Row> targetDuplicates = reconFileDtls.except(reconDropDuplicate);

        System.out.println("--- Matched Rows ---");
        matched.show();

        System.out.println("--- Unmatched Rows ---");
        unmatched.show();

        System.out.println("--- Source Duplicates ---");
        sourceDuplicates.show();

        System.out.println("--- Target Duplicates ---");
        targetDuplicates.show();

        logger.info("Recon Process End");
    }

    private Map<String, String> columnMapping(){
        Map<String, String> columnMappings = new HashMap<>();
        columnMappings.put("PAYMENT_STATUS", "STATUS");
        columnMappings.put("DEBIT_AMT", "PAYMENT_AMOUNT");
        columnMappings.put("ATRN_NUM", "ATRN_NUM");
        columnMappings.put("BANK_REFERENCE_NUMBER", "BANK_REFERENCE_NUMBER");
        return columnMappings;
    }

}
