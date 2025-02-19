public void processPaymentPushVerification(String message, ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
    try {
        PaymentPushVerificationDto dto = objectMapper.readValue(message, PaymentPushVerificationDto.class);
        log.info("Processing payment push verification for ATRN: {}", dto.getAtrnNumber());

        boolean processFlag = transactionDao.getTransactionAndOrderDetail(dto)
                .map(this::buildPaymentVerificationResponse)
                .map(response -> {
                    if (response.getOrderInfo() == null || !response.getPaymentInfo().getPushStatus().equalsIgnoreCase(PushResponseStatus)) {
                        log.info("Push response does not match PushResponseStatus. Skipping push verification.");
                        acknowledgment.acknowledge();
                    }
                    return encryptAndSendToMerchant(dto, response) && updatePushVerificationStatus(dto.getAtrnNumber());
                })
                .orElseGet(() -> {
                    log.warn("Push verification failed, missing transaction data for ATRN: {}", dto.getAtrnNumber());
                    sendToRetryTopic(message, record);
                    return false;
                });

        if (processFlag) {
            acknowledgment.acknowledge();
        } else {
            sendToRetryTopic(message, record);
        }
    } catch (JsonProcessingException e) {
        log.error("JSON Parsing error: {}", e.getMessage());
        sendToRetryTopic(message, record);
    }
}

private int getRetryCount(ConsumerRecord<String, String> record) {
    return Optional.ofNullable(record.headers().lastHeader("retry-count"))
            .map(header -> ByteBuffer.wrap(header.value()).getInt())
            .orElse(0);
}

@Component
@RequiredArgsConstructor
public class RetryPushVerificationListener {
    private final LoggerUtility log = LoggerFactoryUtility.getLogger(this.getClass());
    private final PaymentPushVerificationService paymentPushVerificationService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "retry-topic", groupId = "${spring.kafka.consumer.groupId}")
    public void retryPushVerificationListener(ConsumerRecord<String, String> consumerRecord, Acknowledgment acknowledgment) {
        int retryCount = getRetryCount(consumerRecord);
        log.info("Retrying message from retry-topic (Attempt {}/3) for key: {}", retryCount + 1, consumerRecord.key());

        try {
            paymentPushVerificationService.processPaymentPushVerification(consumerRecord.value(), acknowledgment);
            acknowledgment.acknowledge(); // Acknowledge after successful processing
        } catch (Exception e) {
            log.error("Processing failed on retry attempt {}/3 for key: {}", retryCount + 1, consumerRecord.key(), e);
            
            if (retryCount < 2) {  // Retry max 2 more times (Total 3 attempts)
                sendToRetryTopic(consumerRecord.value(), retryCount + 1);
            } else {
                log.error("Max retries reached. Updating status as failed for key: {}", consumerRecord.key());
                updateStatusAsFailed(consumerRecord.value());  // Update status as failed
                acknowledgment.acknowledge();  // Acknowledge the message so it is not retried again
            }
        }
    }

    private int getRetryCount(ConsumerRecord<String, String> record) {
        return Optional.ofNullable(record.headers().lastHeader("retry-count"))
                .map(header -> ByteBuffer.wrap(header.value()).getInt())
                .orElse(0);
    }

    private void sendToRetryTopic(String message, int retryCount) {
        kafkaTemplate.send("retry-topic", MessageBuilder.withPayload(message)
                .setHeader("retry-count", retryCount)
                .build());
        log.warn("Message sent to retry-topic (Attempt {}/3): {}", retryCount, message);
    }

    private void updateStatusAsFailed(String message) {
        // Logic to update status as failed in DB or logs
        log.error("Payment processing permanently failed. Message: {}", message);
        // Example: transactionDao.updateStatus(paymentId, "FAILED");
    }
}

