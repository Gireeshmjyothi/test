
Parameter 0 of constructor in com.rajput.service.SparkService required a bean of type 'org.apache.spark.sql.SparkSession' that could not be found.


Action:

Consider defining a bean of type 'org.apache.spark.sql.SparkSession' in your configuration.


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
   /* @Bean
    public SparkSession sparkSession() {
        return SparkSession.builder()
                .appName(appName)
                .master(master)
//                .config("spark.ui.enabled", "false")// disable web UI
                .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
                .getOrCreate();
    }*/

    @Bean
    public  SparkConf sparkConf(){
        return new SparkConf()
                .setAppName(appName)
                .setMaster(master)
                .set("spark.driver.host", "127.0.0.1");
    }

    @Bean
    public JavaSparkContext javaSparkContext(SparkConf sparkConf){
        return new JavaSparkContext(sparkConf);
    }
    @Bean
    public Encoder<MerchantOrderPaymentDto> getEncoder() {
        return Encoders.bean(MerchantOrderPaymentDto.class);
    }
}
