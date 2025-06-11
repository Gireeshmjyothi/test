private void publishToKafka(Dataset<Row> dataset, String topic) {
        try {
            Dataset<Row> kafkaDf = dataset.selectExpr(
                    "RFD_ID AS key",
                    "to_json(named_struct('RFD_ID', RFD_ID, 'ATRN_NUM', recon.ATRN_NUM)) AS value"
            );
            kafkaDf.printSchema();
            kafkaDf.describe();
            kafkaDf.write()
                    .format("kafka")
                    .option("kafka.bootstrap.servers", "dev-cluster-kafka-bootstrap-dev-kafka.apps.dev.sbiepay.sbi:443")
                    .option("topic", topic)
                    .save();

            logger.info("Successfully published records to Kafka topic '{}'.", topic);

        } catch (Exception e) {
            logger.error("Failed to publish data to Kafka topic '{}': {}", topic, e.getMessage());
        }
    }
