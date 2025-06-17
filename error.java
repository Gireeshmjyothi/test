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
    
    public void registerUuidToBytesUdf(SparkSession spark) {
        spark.udf().register("uuidToBytes", (UDF1<String, byte[]>) uuidStr -> {
            UUID uuid = UUID.fromString(uuidStr);
            ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
            buffer.putLong(uuid.getMostSignificantBits());
            buffer.putLong(uuid.getLeastSignificantBits());
            return buffer.array();
        }, DataTypes.BinaryType);
    }
