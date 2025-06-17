public void registerUuidToHexUdf(SparkSession spark) {
    spark.udf().register("uuidToHex", (UDF1<String, String>) uuidStr -> {
        UUID uuid = UUID.fromString(uuidStr);
        return uuid.toString().replace("-", "").toUpperCase();  // optional: no dashes, uppercase
    }, DataTypes.StringType);
}

.withColumn("RFD_ID", functions.callUDF("uuidToHex", col("RFD_ID")))
