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
                    sendToRetryTopic(message, 1); // Start with retry count 1
                    return false;
                });

        if (processFlag) {
            acknowledgment.acknowledge();
        } else {
            sendToRetryTopic(message, 1); // Start with retry count 1
        }
    } catch (JsonProcessingException e) {
        logger.error("Error while parsing payment push verification : {}", e.getMessage());
        sendToRetryTopic(message, 1);
    }
}


public void sendToRetryTopic(String message, int retryCount) {
    if (retryCount >= 3) {
        logger.info("Max retry attempts reached. Updating status to 'F'.");
        updatePushVerificationStatusToFailed(message); // Method to update the status in DB
        return;
    }

    kafkaTemplate.send(MessageBuilder
        .withPayload(message)
        .setHeader(KafkaHeaders.TOPIC, "retry-topic")
        .setHeader("retryCount", retryCount)
        .build());
}


@KafkaListener(topics = "retry-topic", groupId = "${spring.kafka.consumer.groupId}")
public void retryPushVerificationListener(ConsumerRecord<String, String> consumerRecord, Acknowledgment acknowledgment) {
    log.info("Retrying message from retry-topic for key: {}", consumerRecord.key());
    
    String message = consumerRecord.value();
    int retryCount = consumerRecord.headers().lastHeader("retryCount") != null ?
            Integer.parseInt(new String(consumerRecord.headers().lastHeader("retryCount").value())) : 0;

    if (retryCount >= 3) {
        log.info("Max retry attempts reached for key: {}. Marking as failed.", consumerRecord.key());
        updatePushVerificationStatusToFailed(message);
        acknowledgment.acknowledge();
        return;
    }

    paymentPushVerificationService.processPaymentPushVerification(message, acknowledgment);
    sendToRetryTopic(message, retryCount + 1);
}


@Component
@RequiredArgsConstructor
public class PaymentPushVerificationListener {
    private final LoggerUtility log = LoggerFactoryUtility.getLogger(this.getClass());
    private final PaymentPushVerificationService paymentPushVerificationService;

    @KafkaListener(topics = "${spring.kafka.topic.payment.push.verification}", groupId = "${spring.kafka.consumer.groupId}", containerFactory = "manualAckKafkaListenerContainerFactory")
    public void paymentPushVerification(ConsumerRecord<String, String> consumerRecord, Acknowledgment acknowledgment) {
        log.debug("Push verification request received for key: {} and value: {}", consumerRecord.key(), consumerRecord.value());
        String message = consumerRecord.value();
        paymentPushVerificationService.processPaymentPushVerification(message, acknowledgment, 0);
    }
}


Description:

A component required a bean named 'manualAckKafkaListenerContainerFactory' that could not be found.


Action:

Consider defining a bean named 'manualAckKafkaListenerContainerFactory' in your configuration.


