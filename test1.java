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
