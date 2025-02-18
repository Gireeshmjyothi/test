@KafkaListener(topics = "${spring.kafka.topic.payment.push.verification}", groupId = "payment-group")
public void paymentPushVerification(ConsumerRecord<String, String> consumerRecord, Acknowledgment acknowledgment) {
    log.debug("Push verification request received for key: {} and value: {}", consumerRecord.key(), consumerRecord.value());
    String message = consumerRecord.value();
    
    try {
        PaymentPushVerificationDto paymentPushVerificationDto = objectMapper.readValue(message, PaymentPushVerificationDto.class);
        boolean processFlag = paymentPushVerificationService.processPaymentPushVerification(paymentPushVerificationDto);
        
        if (processFlag) {
            acknowledgment.acknowledge();
            log.info("Payment push notification message successfully processed and acknowledged for key: {}", consumerRecord.key());
        } else {
            // Send to retry topic with an exponential delay (if required)
            kafkaTemplate.send("retry-topic", message);
            log.warn("Processing failed, message sent to retry-topic for key: {}", consumerRecord.key());
        }
    } catch (JsonProcessingException e) {
        log.error("JSON Parsing failed for key: {}, moving message to dead-letter-topic: {}", consumerRecord.key(), e.getMessage());
        kafkaTemplate.send("dead-letter-topic", message);
    } catch (PaymentException e) {
        log.error("PaymentException occurred for key: {}, error: {}", consumerRecord.key(), e.getErrorMessage());
    } catch (Exception e) {
        log.error("Unexpected error for key: {}, moving message to dead-letter-topic", consumerRecord.key(), e);
        kafkaTemplate.send("dead-letter-topic", message);
    }
}

@KafkaListener(topics = "retry-topic", groupId = "payment-group")
public void retryListener(ConsumerRecord<String, String> consumerRecord, Acknowledgment acknowledgment) {
    log.info("Retrying message from retry-topic for key: {}", consumerRecord.key());
    String message = consumerRecord.value();

    try {
        PaymentPushVerificationDto paymentPushVerificationDto = objectMapper.readValue(message, PaymentPushVerificationDto.class);
        boolean processFlag = paymentPushVerificationService.processPaymentPushVerification(paymentPushVerificationDto);
        
        if (processFlag) {
            acknowledgment.acknowledge();
            log.info("Message successfully processed from retry-topic for key: {}", consumerRecord.key());
        } else {
            log.warn("Retry failed, moving message to dead-letter-topic: {}", consumerRecord.key());
            kafkaTemplate.send("dead-letter-topic", message);
        }
    } catch (Exception e) {
        log.error("Retry attempt failed for key: {}, moving to dead-letter-topic", consumerRecord.key(), e);
        kafkaTemplate.send("dead-letter-topic", message);
    }
}

@KafkaListener(topics = "dead-letter-topic", groupId = "payment-group")
public void deadLetterListener(ConsumerRecord<String, String> consumerRecord) {
    log.error("Dead-letter message received, manual intervention needed: {}", consumerRecord.value());
    // Store in DB, trigger an alert, or manually reprocess
}
