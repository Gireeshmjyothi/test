import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {
    private final KafkaConsumerSettings kafkaConsumerSettings;

    public KafkaConsumerConfig(KafkaConsumerSettings kafkaConsumerSettings) {
        this.kafkaConsumerSettings = kafkaConsumerSettings;
    }

    /*** Auto-Commit Configurations ***/
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> autoCommitStringKafkaListenerContainerFactory() {
        return createFactory(stringConsumerFactory(true), null);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, byte[]> autoCommitByteArrayKafkaListenerContainerFactory() {
        return createFactory(byteArrayConsumerFactory(true), null);
    }

    /*** Manual Acknowledgment Configurations ***/
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> manualAckStringKafkaListenerContainerFactory() {
        return createFactory(stringConsumerFactory(false), ContainerProperties.AckMode.MANUAL);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, byte[]> manualAckByteArrayKafkaListenerContainerFactory() {
        return createFactory(byteArrayConsumerFactory(false), ContainerProperties.AckMode.MANUAL);
    }

    /*** Record-Level Acknowledgment Configurations ***/
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> recordAckStringKafkaListenerContainerFactory() {
        return createFactory(stringConsumerFactory(false), ContainerProperties.AckMode.RECORD);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, byte[]> recordAckByteArrayKafkaListenerContainerFactory() {
        return createFactory(byteArrayConsumerFactory(false), ContainerProperties.AckMode.RECORD);
    }

    /*** Helper Method to Create Factory ***/
    private ConcurrentKafkaListenerContainerFactory<String, ?> createFactory(
            ConsumerFactory<String, ?> consumerFactory, ContainerProperties.AckMode ackMode) {
        ConcurrentKafkaListenerContainerFactory<String, ?> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(kafkaConsumerSettings.getNumberOfConsumers());
        if (ackMode != null) {
            factory.getContainerProperties().setAckMode(ackMode);
        }
        return factory;
    }

    /*** Consumer Factory Methods ***/
    private ConsumerFactory<String, String> stringConsumerFactory(boolean enableAutoCommit) {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(enableAutoCommit, StringDeserializer.class));
    }

    private ConsumerFactory<String, byte[]> byteArrayConsumerFactory(boolean enableAutoCommit) {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(enableAutoCommit, ByteArrayDeserializer.class));
    }

    private Map<String, Object> consumerConfigs(boolean enableAutoCommit, Class<?> valueDeserializerClass) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConsumerSettings.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerSettings.getGroupId());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
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
