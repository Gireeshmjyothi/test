import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.*;

import java.util.List;
import java.util.Properties;

public class KafkaPublisher {
    private final KafkaProducer<String, String> producer;
    private final String topic;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public KafkaPublisher(String bootstrapServers, String topic) {
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        this.producer = new KafkaProducer<>(props);
        this.topic = topic;
    }

    // Used for matched/unmatched
    public void publishFormatted(List<TransactionData> list) {
        for (TransactionData data : list) {
            String msg = data.getAtrn() + ":" + data.getBankRef() + ":" + data.getRfId();
            producer.send(new ProducerRecord<>(topic, msg));
        }
    }

    // Used for duplicate - sends full object as JSON
    public void publishRaw(List<TransactionData> list) {
        for (TransactionData data : list) {
            try {
                String json = objectMapper.writeValueAsString(data);
                producer.send(new ProducerRecord<>(topic, json));
            } catch (Exception e) {
                e.printStackTrace();  // You can replace with proper logging
            }
        }
    }

    public void close() {
        producer.flush();
        producer.close();
    }
}

public class MatchedDataService {
    public void processMatchedData(List<TransactionData> matchedList) {
        KafkaPublisher publisher = new KafkaPublisher("localhost:9092", "matched-topic");
        publisher.publishFormatted(matchedList);
        publisher.close();
    }
}

