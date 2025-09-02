{
    "apiVersion": "spark.apache.org/v1beta1",
    "group": "sparkoperator.k8s.io",
    "version": "v1beta1",
    "type": "Java",
    "mode": "cluster",
    "image": "registry.dev.sbiepay.sbi:8443/spark/spark:01082025v5",
    "mainClass": "org.example.Main",
    "mainApplicationFile": "local:///opt/spark/jars/SparkDemo-0.1.jar",
    "arguments": [],
    "serviceAccount": "spark-sa",
    "driverCores": "1",
    "driverMemory": "512m",
    "executorCores": "1",
    "executorInstances": "2",
    "executorMemory": "1g"
}
