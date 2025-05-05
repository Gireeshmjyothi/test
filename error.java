package com.rajput.config;

import com.rajput.dto.MerchantOrderPaymentDto;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SparkConfig {
    @Value("${spark.app.name}")
    private String appName;

    @Value("${spark.master}")
    private String master;
    
    @Bean
    public SparkSession sparkSession() {
        return SparkSession.builder()
                .appName(appName)
                .master(master)
                .config("spark.ui.enabled", "false")
                .getOrCreate();
    }

    @Bean
    public Encoder<MerchantOrderPaymentDto> getEncoder() {
        return Encoders.bean(MerchantOrderPaymentDto.class);
    }
}


package com.rajput.service;

import com.rajput.dto.MerchantOrderPaymentDto;
import com.rajput.dto.ResponseDto;
import com.rajput.dto.SummaryDto;
import com.rajput.dto.UnMatchedDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SparkService {
    private final SparkSession sparkSession;
//    private final Encoder<Employee> employeeEncoder;

    private final Encoder<MerchantOrderPaymentDto> merchantOrderPayment;
    private final JdbcReaderService jdbcReaderService;
    private final FileProcessorService fileProcessorService;

    public static String formatMillis(long millis) {
        long hours = millis / (1000 * 60 * 60);
        long minutes = (millis / (1000 * 60)) % 60;
        long seconds = (millis / 1000) % 60;
        long ms = millis % 1000;

        return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, ms);
    }

    public ResponseDto process() {
        log.info("🚀 POC Application started");
        long startTime = System.currentTimeMillis();
        ResponseDto responseDto = new ResponseDto();
        List<UnMatchedDto> unMatchedRecords = new ArrayList<>();
        Dataset<MerchantOrderPaymentDto> datasetRowDb = getDbDataSet();
        Dataset<MerchantOrderPaymentDto> datasetRowFile = getFileDataset("data/merchantOrderPayment.csv", "csv", "");
        log.info("Matched");
        responseDto.setFileRecordCount(datasetRowFile.count());
        var matchedWithDB = datasetRowFile.join(datasetRowDb, datasetRowFile.col("atrnNumber").equalTo(datasetRowDb.col("atrnNumber")), "inner")
                .select(
                        datasetRowFile.col("mid"),
                        datasetRowFile.col("orderRefNumber"),
                        datasetRowFile.col("sbiOrderRefNumber"),
                        datasetRowFile.col("atrnNumber"),
                        datasetRowFile.col("debitAmount")
                );
//        matchedWithDB.show();

        responseDto.setMatchedRecords(matchedWithDB.as(merchantOrderPayment).collectAsList());
        log.info("Unmatched: updated or added records");
        var missMatchedWithDB = datasetRowFile.join(datasetRowDb, datasetRowFile.col("atrnNumber").equalTo(datasetRowDb.col("atrnNumber")), "leftanti").as(Encoders.bean(MerchantOrderPaymentDto.class));
        missMatchedWithDB.show();
        missMatchedWithDB.collectAsList().forEach(fileRow -> {
            log.info("File Record: {}", fileRow);
//            Dataset<MerchantOrderPayment> fileRowDs = datasetRowDb.filter("atrnNum = " + fileRow.getAtrnNum());
            UnMatchedDto unMatchedDto = new UnMatchedDto();
            unMatchedDto.setDbMerchantOrderPaymentDto(fileRow);
           /* if (fileRowDs.count() == 0) {
                log.info("New record: {}", fileRow.getAtrnNum());
                unMatchedDto.setNew(true);
            } else {
                log.info("Updated, DB record: {}", fileRowDs.collectAsList());
            }*/
            unMatchedRecords.add(unMatchedDto);
        });
        responseDto.setUnMatchedRecords(unMatchedRecords);
        responseDto.setTimeToProcessed(formatMillis(System.currentTimeMillis() - startTime));
        System.out.println("Spark Matched data count =======>>>> "+ matchedWithDB.count());
        System.out.println("Spark Unmatched data count =======>>>> "+ missMatchedWithDB.count());
        log.info("spark matched and unmatched count: {}, {}", matchedWithDB.count(), responseDto.getMatchedRecords().size());
        System.out.println("Spark core processed time ======>>>> "+ formatMillis(System.currentTimeMillis() - startTime));
        return responseDto;
    }

    private Dataset<MerchantOrderPaymentDto> getDbDataSet() {
//        String merchantOrderPaymentQuery = JdbcQuery.getYesterdayQuery("MERCHANT_ORDER_PAYMENTS", System.currentTimeMillis());
        Dataset<Row> datasetRow = jdbcReaderService.readJdbcQuery("MERCHANT_ORDER_PAYMENTS");

        /*Dataset<Row> datasetRow = sparkSession.read().option("header", true).option("inferSchema", true)
                .csv("data/merchantOrderPayment.csv").cache();*/
        System.out.println("db data count ======>>>> "+datasetRow.count());
        datasetRow.printSchema();
        Dataset<Row> dataset = datasetRow.withColumnRenamed("MERCHANT_ID", "mid")
                .withColumnRenamed("ORDER_REF_NUMBER", "orderRefNumber")
                .withColumnRenamed("SBI_ORDER_REF_NUMBER", "sbiOrderRefNumber")
                .withColumnRenamed("ATRN_NUM", "atrnNumber")
                .withColumnRenamed("DEBIT_AMT", "debitAmount");
        dataset.printSchema();
        return dataset.as(merchantOrderPayment);

    }

    private Dataset<MerchantOrderPaymentDto> getFileDataset(String filePath, String extension, String delimiter) {
        Dataset<Row> fileData = fileProcessorService.getFileDataset(filePath, extension, delimiter);

        /*FileValidator.validateRequiredColumns(fileData, merchantOrderPaymentDao.getListOfColumnName("12345"));

        SummaryDto summaryDto = getFileSummary(fileData);

        System.out.println("Summary Data : " + summaryDto.toString());*/
        fileData.show(10);
        fileData.describe().show();

        Dataset<Row> dataset = fileData.withColumnRenamed("MERCHANT_ID", "mid")
                .withColumnRenamed("ORDER_REF_NUMBER", "orderRefNumber")
                .withColumnRenamed("SBI_ORDER_REF_NUMBER", "sbiOrderRefNumber")
                .withColumnRenamed("ATRN_NUM", "atrnNumber")
                .withColumnRenamed("DEBIT_AMT", "debitAmount");
        dataset.printSchema();
        return dataset.as(merchantOrderPayment);
    }


    private SummaryDto getFileSummary(Dataset<Row> fileDataset) {
        fileDataset.createOrReplaceTempView("file_data");

        // Use Spark SQL for convenience
        Row summaryRow = sparkSession.sql("""
                    SELECT
                        COUNT(*) as totalRecords,
                        COUNT(DISTINCT ATRN_NUM) as uniqueAtrnNum,
                        AVG(DEBIT_AMT) as averageDebitAmt,
                        MAX(DEBIT_AMT) as maxDebitAmt,
                        MIN(DEBIT_AMT) as minDebitAmt
                    FROM file_data
                """).first();

        return SummaryDto.builder()
                .totalRecords(summaryRow.getAs("totalRecords"))
                .uniqueAtrnNum(summaryRow.getAs("uniqueAtrnNum"))
                .mean(summaryRow.getAs("averageDebitAmt"))
                .maxDebitAmt(summaryRow.getAs("maxDebitAmt"))
                .minDebitAmt(summaryRow.getAs("minDebitAmt"))
                .build();
    }
}


package com.rajput.service;

import com.rajput.config.JdbcConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class JdbcReaderService {

    private final SparkSession sparkSession;
    private final JdbcConfig jdbcConfig;

    public Dataset<Row> readJdbcQuery(String query){
        try {
            return sparkSession.read()
                    .option("header", true)
                    .option("inferSchema", true)
                    .jdbc(jdbcConfig.getJdbcUrl(), query, jdbcConfig.getJdbcProperties());
        }catch (Exception ex){
            log.error("Error while reading jdbc query : {}", query);
            throw new RuntimeException(ex.getMessage());
        }
    }

}


package com.rajput.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class JdbcConfig {
    @Value("${jdbc.url}")
    private String jdbcUrl;

    @Value("${jdbc.userName}")
    private String jdbcUserName;

    @Value("${jdbc.password}")
    private String jdbcPassword;

    @Value("${jdbc.driver}")
    private String jdbcDriver;

    @Bean
    public Properties getJdbcProperties() {
        Properties props = new Properties();
        props.put("user", jdbcUserName);
        props.put("password", jdbcPassword);
        props.put("driver", jdbcDriver);
        return props;
    }

    @Bean
    public String getJdbcUrl() {
        return jdbcUrl;
    }
}
