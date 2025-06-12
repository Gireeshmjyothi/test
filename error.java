private void publishToKafka(Dataset<Row> dataset, String topic, String status) {
    // Add status column and repartition
    Dataset<Row> enrichedDf = dataset
            .withColumn("status", functions.lit(status))
            .repartition(10);

    // Capture only the necessary data to avoid serializing 'this'
    String bootstrapServers = kafkaProducerSettings.getBootstrapServers();
    String acks = kafkaProducerSettings.getAcks();
    String retries = kafkaProducerSettings.getRetries();
    String batchSize = kafkaProducerSettings.getBatchSize();
    String lingerMs = kafkaProducerSettings.getLingerMs();
    String bufferMemory = kafkaProducerSettings.getBufferMemory();
    boolean sslEnabled = kafkaProducerSettings.isSslConfigProvided();
    String trustLocation = kafkaProducerSettings.getTrustLocation();
    String trustPassword = kafkaProducerSettings.getTrustPassword();
    String trustType = kafkaProducerSettings.getTrustType();
    String keyLocation = kafkaProducerSettings.getKeyLocation();
    String keyPassword = kafkaProducerSettings.getKeyPassword();
    String keyType = kafkaProducerSettings.getKeyType();
    String securityProtocol = kafkaProducerSettings.getSecurityProtocol();

    enrichedDf.foreachPartition((ForeachPartitionFunction<Row>) partition -> {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.ACKS_CONFIG, acks);
        props.put(ProducerConfig.RETRIES_CONFIG, retries);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        props.put(ProducerConfig.LINGER_MS_CONFIG, lingerMs);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        if (sslEnabled) {
            props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, securityProtocol);
            props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, CommonUtil.getAbsolutePath(trustLocation));
            props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, trustPassword);
            props.put(SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG, trustType);
            props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, CommonUtil.getAbsolutePath(keyLocation));
            props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, keyPassword);
            props.put(SslConfigs.SSL_KEYSTORE_TYPE_CONFIG, keyType);
        }

        props.put(ProducerConfig.CLIENT_ID_CONFIG, "recon-producer-" + UUID.randomUUID());

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        List<String> batch = new ArrayList<>();
        final int BATCH_SIZE = 500;

        while (partition.hasNext()) {
            Row row = partition.next();

            byte[] rfdBytes = row.getAs("RFD_ID");
            String rfdHex = bytesToHex(rfdBytes).toUpperCase();

            String atrnNum = row.getAs("ATRN_NUM").toString();

            String json = String.format(
                    "{\"rfdId\":\"%s\", \"atrnNum\":\"%s\", \"status\":\"%s\"}",
                    rfdHex, atrnNum, status
            );

            batch.add(json);

            if (batch.size() >= BATCH_SIZE) {
                sendBatch(producer, topic, batch);
                batch.clear();
            }
        }

        if (!batch.isEmpty()) {
            sendBatch(producer, topic, batch);
        }

        producer.close();
    });
}
private static void sendBatch(KafkaProducer<String, String> producer, String topic, List<String> batch) {
    for (String msg : batch) {
        producer.send(new ProducerRecord<>(topic, UUID.randomUUID().toString(), msg));
    }
}

private static String bytesToHex(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (byte b : bytes) {
        sb.append(String.format("%02x", b));
    }
    return sb.toString();
}
