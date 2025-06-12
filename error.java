import static org.apache.spark.sql.functions.*;
import org.apache.kafka.clients.producer.*;

import java.util.*;

private void publishInBatchesToKafka(Dataset<Row> dataset, String topic, String status) {
    // Add status and repartition to control load
    Dataset<Row> enrichedDf = dataset
        .withColumn("STATUS", lit(status))
        .repartition(100); // adjust this based on your Kafka topic partitions and message size

    enrichedDf.foreachPartition(partition -> {
        // Kafka producer setup
        Properties props = new Properties();
        props.put("bootstrap.servers", "your-kafka:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("acks", "1");

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        List<String> batch = new ArrayList<>();
        final int BATCH_SIZE = 500; // tune this based on expected record size and Kafka's message.max.bytes

        while (partition.hasNext()) {
            Row row = partition.next();

            String json = String.format(
                "{\"RFD_ID\":\"%s\", \"ATRN_NUM\":\"%s\", \"STATUS\":\"%s\"}",
                row.getAs("RFD_ID").toString().toUpperCase(),
                row.getAs("ATRN_NUM"),
                status
            );

            batch.add(json);

            // When batch limit is reached, publish
            if (batch.size() >= BATCH_SIZE) {
                sendBatch(producer, topic, batch, status);
                batch.clear();
            }
        }

        // Send leftover records
        if (!batch.isEmpty()) {
            sendBatch(producer, topic, batch, status);
        }

        producer.close();
    });
}

// Helper method to send batch to Kafka
private void sendBatch(KafkaProducer<String, String> producer, String topic, List<String> batch, String status) {
    String uuidKey = UUID.randomUUID().toString() + "_" + status;
    String payload = "[" + String.join(",", batch) + "]";
    ProducerRecord<String, String> record = new ProducerRecord<>(topic, uuidKey, payload);
    producer.send(record);
}
