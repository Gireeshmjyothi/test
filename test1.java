@Setter
@Component
@ConfigurationProperties(prefix = "spring.kafka")
public class KafkaProducerSettings {
    @Value("${spring.kafka.bootstrapServers}")
    private String bootstrapServers;

    @Value("${spring.kafka.producer.acks}")
    private String acks;

    @Value("${spring.kafka.producer.retries}")
    private int retries;

    @Value("${spring.kafka.producer.batchSize}")
    private int batchSize;

    @Value("${spring.kafka.producer.lingerMs}")
    private int lingerMs;

    @Value("${spring.kafka.producer.bufferMemory}")
    private long bufferMemory;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${spring.kafka.properties.security.protocol:}")
    private String securityProtocol;
    
    @Value("${spring.kafka.properties.ssl.truststore.location:}")
    private String trustLocation;
    
    @Value("${spring.kafka.properties.ssl.truststore.password:}")
    private String trustPassword;
    
    @Value("${spring.kafka.properties.ssl.truststore.type:}")
    private String trustType;
    
    @Value("${spring.kafka.properties.ssl.keystore.location:}")
    private String keyLocation;
    
    @Value("${spring.kafka.properties.ssl.keystore.password:}")
    private String keyPassword;
    
    @Value("${spring.kafka.properties.ssl.keystore.type:}")
    private String keyType;
}



spring:
  kafka:
    consumer:
      group-id: transaction-consumers
      enable-auto-commit: true
      auto-commit-interval: 100
      session-timeout-ms: 300000
      request-timeout-ms: 420000
      fetch-max-wait-ms: 200
      max-poll-records: 5
      auto-offset-reset: latest
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      retry:
        max-attempts: 3
        backoff:
          initial-interval-ms: 10000
          max-interval-ms: 30000
      number-of-consumers: 1

    producer:
      acks: all
      retries: 3
      batch-size: 1000
      linger-ms: 1
      buffer-memory: 33554432
      key-deserializer: org.apache.kafka.common.serialization.StringSerializer
      value-deserializer: org.apache.kafka.common.serialization.StringSerializer

    topic:
      transaction:
        notification:
          sms: transaction_sms_notification_topic
          email: transaction_email_notification_topic
      partitions: 4
      replication-factor: 1
