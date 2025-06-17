public void registerUuidToHexUdf(SparkSession spark) {
    spark.udf().register("uuidToHex", (UDF1<String, String>) uuidStr -> {
        UUID uuid = UUID.fromString(uuidStr);
        return uuid.toString().replace("-", "").toUpperCase();
    }, DataTypes.StringType);

    spark.udf().register("uuidToBytes", (UDF1<String, byte[]>) uuidStr -> {
        UUID uuid = UUID.fromString(uuidStr);
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }, DataTypes.BinaryType);
}
