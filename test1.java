package com.epay.rns.config;

import com.epay.rns.dto.MerchantOrderPayment;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.api.java.UDF1;
import org.apache.spark.sql.types.DataTypes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.ByteBuffer;
import java.util.UUID;

@Configuration
public class SparkConfig {
    @Value("${spark.app.name}")
    private String appName;

    @Value("${spark.master}")
    private String master;
    @Bean
    public SparkSession sparkSession() {
        SparkSession spark = SparkSession.builder()
                .appName(appName)
                .master(master)
                .config("spark.driver.memory", "3g")
                .config("spark.ui.enabled", "false") // disable web UI
                .getOrCreate();
        registerUuidToBytesUdf(spark);
        return spark;
    }
    @Bean
    public Encoder<MerchantOrderPayment> getEncoder() {
        return Encoders.bean(MerchantOrderPayment.class);
    }

    public void registerUuidToBytesUdf(SparkSession spark) {
        spark.udf().register("uuidToBytes", (UDF1<String, byte[]>) uuid -> {
            UUID u = UUID.fromString(uuid);
            byte[] buffer = new byte[16];
            ByteBuffer bb = ByteBuffer.wrap(buffer);
            bb.putLong(u.getMostSignificantBits());
            bb.putLong(u.getLeastSignificantBits());
            return buffer;
        }, DataTypes.BinaryType);
    }
}


2025-06-03 17:15:50.610 ERROR | com.epay.rns.exception.ExceptionHandlingController:30 | principal=  | scenario= | operation= | correlation= | handleError | Request: http://localhost:9097/api/rns/v1/process/EF8A2FF412F84FD6821C9C9A49489EA5 raised {}
org.apache.spark.sql.AnalysisException: Column RFS_ID not found in schema Some(StructType(StructField(RFD_ID,BinaryType,false),StructField(ATRN_NUM,StringType,true),StructField(RECON_STATUS,StringType,true))).
	at org.apache.spark.sql.errors.QueryCompilationErrors$.columnNotFoundInSchemaError(QueryCompilationErrors.scala:1692)
	at org.apache.spark.sql.execution.datasources.jdbc.JdbcUtils$.$anonfun$getInsertStatement$4(JdbcUtils.scala:126)
	at scala.Option.getOrElse(Option.scala:189)
	at org.apache.spark.sql.execution.datasources.jdbc.JdbcUtils$.$anonfun$getInsertStatement$2(JdbcUtils.scala:126)
