import static org.apache.spark.sql.functions.*;

private void publishListToKafka(Dataset<Row> dataset, String topic, String status) {
    try {
        // Add STATUS column dynamically
        Dataset<Row> enrichedDf = dataset.withColumn("STATUS", lit(status));

        // Create a struct for each row with UUID as key
        Dataset<Row> jsonDf = enrichedDf.selectExpr(
            "named_struct(" +
                "'RFD_ID', upper(hex(RFD_ID)), " +  // UUID format
                "'ATRN_NUM', ATRN_NUM, " +
                "'STATUS', STATUS" +
            ") as record"
        );

        // Collect all records into a JSON array string
        Dataset<Row> aggregated = jsonDf
            .agg(to_json(collect_list(col("record"))).alias("value"))
            .withColumn("key", expr("uuid()")); // Generate a random UUID for the key

        // Write as a single Kafka message
        aggregated
            .selectExpr("cast(key as string)", "cast(value as string)")
            .write()
            .format("kafka")
            .option("kafka.bootstrap.servers", "your-kafka:9092")
            .option("topic", topic)
            .save();

        logger.info("Published bulk UUID-keyed message with STATUS='{}' to topic {}", status, topic);

    } catch (Exception e) {
        logger.error("Error publishing to Kafka topic '{}': {}", topic, e.getMessage(), e);
    }
}
