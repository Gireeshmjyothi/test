Caused by: java.lang.IllegalStateException: No Acknowledgment available as an argument, the listener container must have a MANUAL AckMode to populate the Acknowledgment.
	at org.springframework.kafka.listener.adapter.MessagingMessageListenerAdapter.checkAckArg(MessagingMessageListenerAdapter.java:441)
	at org.springframework.kafka.listener.adapter.MessagingMessageListenerAdapter.invokeHandler(MessagingMessageListenerAdapter.java:427)
	at org.springframework.kafka.listener.adapter.MessagingMessageListenerAdapter.invoke(MessagingMessageListenerAdapter.java:384)
	at org.springframework.kafka.listener.adapter.RecordMessagingMessageListenerAdapter.onMessage(RecordMessagingMessageListenerAdapter.java:85)
	at org.springframework.kafka.listener.adapter.RecordMessagingMessageListenerAdapter.onMessage(RecordMessagingMessageListenerAdapter.java:50)
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.doInvokeOnMessage(KafkaMessageListenerContainer.java:2800)
	... 13 common frames omitted
Caused by: org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException: Could not resolve method parameter at index 1 in public void com.epay.payment.etl.listener.PaymentPushVerificationListener.paymentPushVerification(org.apache.kafka.clients.consumer.ConsumerRecord<java.lang.String, java.lang.String>,org.springframework.kafka.support.Acknowledgment): 1 error(s): [Error in object 'acknowledgment': codes []; arguments []; default message [Payload value must not be empty]] 


	@KafkaListener(topics = "${spring.kafka.topic.payment.push.verification}")
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
