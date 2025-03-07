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

#SMS and Email config
external.api.sms.gateway.base.path=https://smsapipprod.sbi.co.in:9443
external.api.sms.gateway.url=/bmg/sms/epaypgotpdom
external.api.sms.gateway.user=epaypgotpdom
external.api.sms.gateway.password=Ep@y1Dpt
external.api.sms.body.content.type=text
external.api.sms.body.sender.id=SBIBNK
external.api.sms.body.int.flag=0
external.api.sms.body.charging=0

spring.mail.host=10.176.245.236
spring.mail.port=587
spring.mail.username=sbitestclient
spring.mail.password=sbitestclient_7f827c4b3aa6cd1f08d6b9cce2c0c80e

email.recipient=ebms_uat_receiver@ebmsgits.sbi.co.in
email.from=ebms_uat_sender@ebmsgits.sbi.co.in
