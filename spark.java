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


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class KafkaPublisherService {

    private final KafkaConfigProperties config;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private KafkaProducer<String, String> createProducer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", config.getBootstrapServers());
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return new KafkaProducer<>(props);
    }

    public void publishMatched(List<TransactionData> matchedList) {
        try (KafkaProducer<String, String> producer = createProducer()) {
            for (TransactionData data : matchedList) {
                String msg = String.format("%s:%s:%s", data.getAtrn(), data.getBankRef(), data.getRfId());
                producer.send(new ProducerRecord<>(config.getTopicMatched(), msg));
            }
        }
    }

    public void publishUnmatched(List<TransactionData> unmatchedList) {
        try (KafkaProducer<String, String> producer = createProducer()) {
            for (TransactionData data : unmatchedList) {
                String msg = String.format("%s:%s:%s", data.getAtrn(), data.getBankRef(), data.getRfId());
                producer.send(new ProducerRecord<>(config.getTopicUnmatched(), msg));
            }
        }
    }

    public void publishDuplicate(List<TransactionData> duplicateList) {
        try (KafkaProducer<String, String> producer = createProducer()) {
            for (TransactionData data : duplicateList) {
                String json = objectMapper.writeValueAsString(data);
                producer.send(new ProducerRecord<>(config.getTopicDuplicate(), json));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

