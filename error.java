private void publishInBatchesToKafka(Dataset<Row> dataset, String topic, String status) {
    Dataset<Row> enrichedDf = dataset
        .withColumn("STATUS", lit(status))
        .repartition(100); // Tune based on your data size & Kafka partition count

    enrichedDf.foreachPartition(partition -> {
        Properties props = new Properties();
        props.put("bootstrap.servers", "your-kafka:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("acks", "1");
        props.put("max.request.size", "5000000"); // Increase if needed

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        List<String> batch = new ArrayList<>();
        final int BATCH_SIZE = 500;

        while (partition.hasNext()) {
            Row row = partition.next();

            byte[] rfdBytes = row.getAs("RFD_ID");
            String rfdHex = bytesToHex(rfdBytes).toUpperCase();

            String atrnNum = row.getAs("ATRN_NUM").toString();

            String json = String.format(
                "{\"RFD_ID\":\"%s\", \"ATRN_NUM\":\"%s\", \"STATUS\":\"%s\"}",
                rfdHex, atrnNum, status
            );

            batch.add(json);

            if (batch.size() >= BATCH_SIZE) {
                sendBatch(producer, topic, batch, status);
                batch.clear();
            }
        }

        if (!batch.isEmpty()) {
            sendBatch(producer, topic, batch, status);
        }

        producer.close();
    });
}


private String bytesToHex(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (byte b : bytes) {
        sb.append(String.format("%02x", b));
    }
    return sb.toString();
}
