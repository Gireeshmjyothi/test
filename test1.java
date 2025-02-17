@Component
@RequiredArgsConstructor
public class PaymentPushVerificationListener {
    private final LoggerUtility log = LoggerFactoryUtility.getLogger(this.getClass());
    private final ObjectMapper objectMapper;
    private final PaymentPushVerificationService paymentPushVerificationService;
    private final KafkaTemplate kafkaTemplate;

    @KafkaListener(topics = "${spring.kafka.topic.payment.push.verification}")
    public void paymentPushVerification(ConsumerRecord<String, String> consumerRecord, Acknowledgment acknowledgment) {
        log.debug("Push verification request received for key : {} and value : {}", consumerRecord.key(), consumerRecord.value());
        String message = consumerRecord.value();
        try {
            PaymentPushVerificationDto paymentPushVerificationDto = objectMapper.readValue(consumerRecord.value(), PaymentPushVerificationDto.class);
            boolean processFlag = paymentPushVerificationService.processPaymentPushVerification(paymentPushVerificationDto);
            if (processFlag) {
                acknowledgment.acknowledge();
            } else {
                kafkaTemplate.send("retry-topic", message);
            }
        } catch (PaymentException e) {
            log.error("PaymentException during onSmsMessage kafka listening message[key:{} and value: {}], error: {}", consumerRecord.key(), consumerRecord.value(), e.getErrorMessage());
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException during onSmsMessage kafka listening message[key:{} and value: {}], error: {}", consumerRecord.key(), consumerRecord.value(), e.getMessage());
        } catch (Exception e) {
            kafkaTemplate.send("dead-letter-topic", message);
        }
    }
}
