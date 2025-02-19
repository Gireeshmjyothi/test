public void processPaymentPushVerification(String message, Acknowledgment acknowledgment) {
        try {
            PaymentPushVerificationDto paymentPushVerificationDto = objectMapper.readValue(message, PaymentPushVerificationDto.class);
            logger.info("Starting payment push verification for ATRN: {}", paymentPushVerificationDto.getAtrnNumber());
            boolean processFlag = transactionDao.getTransactionAndOrderDetail(paymentPushVerificationDto)
                    .map(this::buildPaymentVerificationResponse)
                    .map(response -> {
                        if (response.getOrderInfo() == null || !response.getPaymentInfo().getPushStatus().equalsIgnoreCase(PushResponseStatus)) {
                            logger.info("Push response does not match PushResponseStatus. Skipping push verification.");
                            acknowledgment.acknowledge();
                        }
                        return encryptAndSendToMerchant(paymentPushVerificationDto, response) && updatePushVerificationStatus(paymentPushVerificationDto.getAtrnNumber());
                    })
                    .orElseGet(() -> {
                        logger.warn("Push verification process failed due to missing transaction data for ATRN: {}", paymentPushVerificationDto.getAtrnNumber());
                        sendToRetryTopic(message);
                        return false;
                    });
            if (processFlag) {
                acknowledgment.acknowledge();
            } else {
                sendToRetryTopic(message);
            }
        } catch (JsonProcessingException e){
            logger.error("Error while parsing payment push verification : {}", e.getMessage());
            //Processing for retry
            sendToRetryTopic(message);
        }
    }


    public void sendToRetryTopic(String message){
        kafkaTemplate.send("retry-topic", message);
    }


@Component
@RequiredArgsConstructor
public class RetryPushVerificationListener {
    private final LoggerUtility log = LoggerFactoryUtility.getLogger(this.getClass());
    private final PaymentPushVerificationService paymentPushVerificationService;

    @KafkaListener(topics = "retry-topic", groupId = "${spring.kafka.consumer.groupId}")
    public void retryPushVerificationListener(ConsumerRecord<String, String> consumerRecord, Acknowledgment acknowledgment) {
        log.info("Retrying message from retry-topic for key: {}", consumerRecord.key());
        String message = consumerRecord.value();
        paymentPushVerificationService.processPaymentPushVerification(message, acknowledgment);
    }
}
