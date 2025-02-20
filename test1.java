@Data
@RequiredArgsConstructor
public class PaymentPushVerificationDto {
    private String mId;
    private String atrnNumber;
}


Just make sure if we are sending full object in Kafka, if in future any property getting change for this class can create the issue for remaining msg in the queue, so its always advisable for Kafka Publish we should not have same object what we are using application wide or it should be single property which class not going to modify


         @KafkaListener(topics = "${spring.kafka.topic.payment.push.verification}")
    public void paymentPushVerification(ConsumerRecord<String, String> consumerRecord) throws PaymentException, JsonProcessingException {
        log.debug("Push verification request received for key: {} and value: {}", consumerRecord.key(), consumerRecord.value());
        String message = consumerRecord.value();
        paymentPushVerificationService.processPaymentPushVerification(message, atomicInteger);
    }
