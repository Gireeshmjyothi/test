package com.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@RestController
@RequestMapping("/api/spark")
@Slf4j
public class SparkSubmitController {

    @PostMapping("/submit")
    public ResponseEntity<String> submitSparkJob() {
        try {
            String sparkHome = "/path/to/spark"; // e.g., /usr/local/spark
            String jarPath = "/path/to/your-fat-jar.jar"; // e.g., /app/build/libs/spark-example-1.0-SNAPSHOT.jar
            String mainClass = "com.example.sparkjob.SimpleSparkJob";

            ProcessBuilder builder = new ProcessBuilder(
                    sparkHome + "/bin/spark-submit",
                    "--class", mainClass,
                    "--master", "local[*]",
                    jarPath
            );

            builder.redirectErrorStream(true);
            Process process = builder.start();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.info(line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                return ResponseEntity.ok("Spark job submitted successfully.");
            } else {
                return ResponseEntity.status(500).body("Spark job failed with exit code: " + exitCode);
            }

        } catch (Exception e) {
            log.error("Error while submitting Spark job", e);
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}

package com.example.sparkjob;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.net.URL;
import java.nio.file.Paths;

import static org.apache.spark.sql.functions.col;

public class SimpleSparkJob {
    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
                .appName("Simple Spark Filter Job")
                .master("local[*]") // for local testing
                .getOrCreate();

        try {
            // Get CSV from resources
            URL resource = SimpleSparkJob.class.getClassLoader().getResource("data.csv");
            if (resource == null) {
                throw new IllegalArgumentException("File not found!");
            }
            String filePath = Paths.get(resource.toURI()).toString();

            // Read CSV
            Dataset<Row> df = spark.read()
                    .option("header", "true")
                    .option("inferSchema", "true")
                    .csv(filePath);

            // Filter rows where amount > 1000
            Dataset<Row> filtered = df.filter(col("amount").gt(1000));
            filtered.show();

            // Output to local folder
            filtered.write()
                    .option("header", "true")
                    .mode("overwrite")
                    .csv("output/filtered_data");

        } catch (Exception e) {
            e.printStackTrace();
        }

        spark.stop();
    }
}
