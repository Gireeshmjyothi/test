spark:
  group: "spark.apache.org"
  version: "v1beta1"
  type: "Java"
  mode: "ClusterMode"
  image: "registry.dev.sbiepay.sbi:8443/spark/sparkrecon:4.0.0_12092025v2"
  mainClass: "com.epay.operations.recon.ReconSparkAppMain"
  mainApplicationFile: "local:///opt/spark/work-dir/recon-spark-job-0.0.1.jar"
  arguments: []
  serviceAccount: "spark-sa"
  driverCores: "1"
  driverMemory: "512m"
  executorCores: "1"
  executorInstances: "2"
  executorMemory: "1g"

  labels:
    app: "spark-demo"
    test: "validation"
    version: "4.0.0"

  env:
    - name: "rfId"
      value: "1A6CF13CDF224845A15AF740E2716015"

  extraJavaOptions: "-DrfId=1A6CF13CDF224845A15AF740E2716015"
  sparkVersion: "4.0.0"

  sparkConf:
    spark.kubernetes.container.image: "registry.dev.sbiepay.sbi:8443/spark/sparkrecon:4.0.0_12092025v2"
    spark.kubernetes.container.image.pullPolicy: "Always"
    spark.driver.memory: "512m"
    spark.driver.cores: "1"
    spark.executor.memory: "512m"
    spark.executor.cores: "1"
    spark.executor.instances: "2"
    spark.kubernetes.authenticate.driver.serviceAccountName: "spark-sa"
    spark.kubernetes.executor.serviceAccount: "spark-sa"
    spark.kubernetes.namespace: "dev-spark"
    spark.kubernetes.driver.label.app: "spark-demo"
    spark.kubernetes.executor.label.app: "spark-demo"
    spark.kubernetes.driver.label.test: "validation"
    spark.kubernetes.executor.label.test: "validation"
    spark.eventLog.enabled: "true"
    spark.sql.adaptive.enabled: "true"
    spark.sql.adaptive.coalescePartitions.enabled: "true"
    spark.kubernetes.driver.env.rfId: "1A6CF13CDF224845A15AF740E2716015"        "spark.kubernetes.authenticate.driver.serviceAccountName": "spark-sa",
        "spark.kubernetes.executor.serviceAccount": "spark-sa",
        "spark.kubernetes.namespace": "dev-spark",
        "spark.kubernetes.driver.label.app": "spark-demo",
        "spark.kubernetes.executor.label.app": "spark-demo",
        "spark.kubernetes.driver.label.test": "validation",
        "spark.kubernetes.executor.label.test": "validation",
        "spark.eventLog.enabled": "true",
        "spark.sql.adaptive.enabled": "true",
        "spark.sql.adaptive.coalescePartitions.enabled": "true",
        "spark.kubernetes.driver.env.rfId": "1A6CF13CDF224845A15AF740E2716015"
    }
}
      
package com.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "spark")
public class SparkConfig {

    private String group;
    private String version;
    private String type;
    private String mode;
    private String image;
    private String mainClass;
    private String mainApplicationFile;
    private List<String> arguments;
    private String serviceAccount;
    private String driverCores;
    private String driverMemory;
    private String executorCores;
    private String executorInstances;
    private String executorMemory;

    private Map<String, String> labels;
    private List<EnvVar> env;

    private String extraJavaOptions;
    private String sparkVersion;
    private Map<String, String> sparkConf;

    @Data
    public static class EnvVar {
        private String name;
        private String value;
    }
      }
