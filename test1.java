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



#Kafka consumer setting
spring.kafka.consumer.groupId=transaction-consumers
spring.kafka.consumer.enableAutoCommit=true
spring.kafka.consumer.autoCommitInterval=100
spring.kafka.consumer.sessionTimeoutMS=300000
spring.kafka.consumer.requestTimeoutMS=420000
spring.kafka.consumer.fetchMaxWaitMS=200
spring.kafka.consumer.maxPollRecords=5
spring.kafka.consumer.autoOffsetReset=latest
spring.kafka.consumer.keyDeserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.valueDeserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.retryMaxAttempts=3
spring.kafka.consumer.retryBackOffInitialIntervalMS=10000
spring.kafka.consumer.retryBackOffMaxIntervalMS=30000
spring.kafka.consumer.numberOfConsumers=1


#Kafka producer setting
spring.kafka.producer.acks=all
spring.kafka.producer.retries=3
spring.kafka.producer.batchSize=1000
spring.kafka.producer.lingerMs=1
spring.kafka.producer.bufferMemory=33554432
spring.kafka.producer.keyDeserializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.valueDeserializer=org.apache.kafka.common.serialization.StringSerializer

#Topics
spring.kafka.topic.transaction.notification.sms=transaction_sms_notification_topic
spring.kafka.topic.transaction.notification.email=transaction_email_notification_topic
spring.kafka.topic.partitions=4
spring.kafka.topic.replicationFactor=1
