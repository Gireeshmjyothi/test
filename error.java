private void publishToKafka(Dataset<Row> dataset, String topic) {
    if (dataset.isEmpty()) {
        logger.info("Dataset is empty. Skipping Kafka publish for topic: {}", topic);
        return;
    }

    try {
        Dataset<Row> kafkaDf = dataset.selectExpr(
            "CAST(RFD_ID AS STRING) AS key", // Kafka requires key/value as STRING or BINARY
            "to_json(named_struct('RFD_ID', CAST(RFD_ID AS STRING), 'ATRN_NUM', recon.ATRN_NUM)) AS value"
        );

        kafkaDf.write()
               .format("kafka")
               .option("kafka.bootstrap.servers", "dev-cluster-kafka-bootstrap-dev-kafka.apps.dev.sbiepay.sbi:443")
               .option("topic", topic)
               .save();

        logger.info("Successfully published {} records to Kafka topic '{}'.", dataset.count(), topic);

    } catch (Exception e) {
        logger.error("Failed to publish data to Kafka topic '{}': {}", topic, e.getMessage(), e);
    }
}
