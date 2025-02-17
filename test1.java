@Component
@RequiredArgsConstructor
public class PaymentPushVerificationListener {
    
    private static final Logger log = LoggerFactory.getLogger(PaymentPushVerificationListener.class);
    
    private final ObjectMapper objectMapper;
    private final PaymentPushVerificationService paymentPushVerificationService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "${spring.kafka.topic.payment.push.verification}")
    public void paymentPushVerification(ConsumerRecord<String, String> consumerRecord, Acknowledgment acknowledgment) {
        log.debug("Push verification request received for key: {} and value: {}", consumerRecord.key(), consumerRecord.value());

        String message = consumerRecord.value();
        
        try {
            // Parse JSON message
            PaymentPushVerificationDto paymentPushVerificationDto = objectMapper.readValue(message, PaymentPushVerificationDto.class);
            
            // Process the message
            boolean processFlag = paymentPushVerificationService.processPaymentPushVerification(paymentPushVerificationDto);

            if (processFlag) {
                acknowledgment.acknowledge(); // ✅ Acknowledge if processed successfully
                log.info("Message successfully processed and acknowledged for key: {}", consumerRecord.key());
            } else {
                kafkaTemplate.send("retry-topic", message); // 🔄 Send to retry-topic if processing failed
                log.warn("Processing failed, message sent to retry-topic for key: {}", consumerRecord.key());
            }

        } catch (JsonProcessingException e) {
            log.error("❌ JSON Parsing failed for key: {}, moving message to dead-letter-topic: {}", consumerRecord.key(), e.getMessage());
            kafkaTemplate.send("dead-letter-topic", message);

        } catch (PaymentException e) {
            log.error("❌ PaymentException occurred for key: {}, error: {}", consumerRecord.key(), e.getErrorMessage());
            // ❗ No need to move to DLQ, because business errors should not block processing.

        } catch (Exception e) {
            log.error("❌ Unexpected error for key: {}, moving message to dead-letter-topic", consumerRecord.key(), e);
            kafkaTemplate.send("dead-letter-topic", message);
        }
    }
}
