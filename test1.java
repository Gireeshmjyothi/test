Description:

Parameter 0 of constructor in com.epay.transaction.config.kafka.env.KafkaProducerConfig required a bean of type 'com.epay.transaction.config.kafka.env.KafkaProducerSettings' that could not be found.


Action:

Consider defining a bean of type 'com.epay.transaction.config.kafka.env.KafkaProducerSettings' in your configuration.


@Configuration
public class KafkaProducerConfig {
    private final LoggerUtility log = LoggerFactoryUtility.getLogger(this.getClass());
    private final KafkaProducerSettings kafkaProducerSettings;

    public KafkaProducerConfig(KafkaProducerSettings kafkaProducerSettings) {
        this.kafkaProducerSettings = kafkaProducerSettings;
    }

    @Bean
    public KafkaTemplate<String, String> stringKafkaTemplate() {
        return new KafkaTemplate<>(stringProducerFactory());
    }

    @Bean
    public KafkaTemplate<String, byte[]> byteArrayKafkaTemplate() {
        return new KafkaTemplate<>(byteArrayProducerFactory());
    }
}


@Getter
@Setter
@Component
public abstract class KafkaProducerSettings {
    @Value("${spring.kafka.bootstrapServers}")
    private String bootstrapServers;

    @Value("${spring.kafka.producer.acks}")
}
