@Bean
    public SparkSession sparkSession() {
        SparkSession.Builder sparkSession = SparkSession.builder()
                .appName("operations-recon-spark-service");

        if (LOCAL.equalsIgnoreCase(reconProperties.getEnv())) {
            sparkSession = sparkSession.master(reconProperties.getSparkMaster());
        } else {
            sparkSession = sparkSession
                    .master(reconProperties.getSparkMaster())
                    .config("spark.hadoop.fs.s3a.endpoint", reconProperties.getAwsUrl())
                    .config("spark.hadoop.fs.s3a.endpoint.region", reconProperties.getAwsRegion())
                    .config("spark.hadoop.fs.s3a.access.key", reconProperties.getAwsKey())
                    .config("spark.hadoop.fs.s3a.secret.key", reconProperties.getAwsSecret());
        }
        return sparkSession.getOrCreate();
    }
