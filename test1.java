@Configuration
public class KafkaConsumerConfig {
    private final KafkaConsumerSettings kafkaConsumerSettings;

    public KafkaConsumerConfig(KafkaConsumerSettings kafkaConsumerSettings) {
        this.kafkaConsumerSettings = kafkaConsumerSettings;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> stringKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(stringConsumerFactory());
        factory.setConcurrency(kafkaConsumerSettings.getNumberOfConsumers());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, byte[]> byteArrayKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, byte[]> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(byteArrayConsumerFactory());
        factory.setConcurrency(kafkaConsumerSettings.getNumberOfConsumers());
        return factory;
    }

    private ConsumerFactory<String, String> stringConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(StringDeserializer.class));
    }

    private ConsumerFactory<String, byte[]> byteArrayConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(ByteArrayDeserializer.class));
    }

    private Map<String, Object> consumerConfigs(Class<?> valueDeserializerClass) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConsumerSettings.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerSettings.getGroupId());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, kafkaConsumerSettings.isAutoCommitCursor());
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, kafkaConsumerSettings.getAutoCommitCursorIntervalMS());
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, kafkaConsumerSettings.getSessionTimeoutMS());
        props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, kafkaConsumerSettings.getRequestTimeoutMS());
        props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, kafkaConsumerSettings.getFetchMaxWaitMS());
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, kafkaConsumerSettings.getMaxPollRecords());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConsumerSettings.getOffsetReset());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaConsumerSettings.getKeyDeserializer());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializerClass);
        return props;
    }
}
