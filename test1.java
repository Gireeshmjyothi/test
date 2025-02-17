Caused by: java.lang.IllegalStateException: No Acknowledgment available as an argument, the listener container must have a MANUAL AckMode to populate the Acknowledgment.
	at org.springframework.kafka.listener.adapter.MessagingMessageListenerAdapter.checkAckArg(MessagingMessageListenerAdapter.java:441)
	at org.springframework.kafka.listener.adapter.MessagingMessageListenerAdapter.invokeHandler(MessagingMessageListenerAdapter.java:427)
	at org.springframework.kafka.listener.adapter.MessagingMessageListenerAdapter.invoke(MessagingMessageListenerAdapter.java:384)
	at org.springframework.kafka.listener.adapter.RecordMessagingMessageListenerAdapter.onMessage(RecordMessagingMessageListenerAdapter.java:85)
	at org.springframework.kafka.listener.adapter.RecordMessagingMessageListenerAdapter.onMessage(RecordMessagingMessageListenerAdapter.java:50)
	at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.doInvokeOnMessage(KafkaMessageListenerContainer.java:2800)
	... 13 common frames omitted
Caused by: org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException: Could not resolve method parameter at index 1 in public void com.epay.payment.etl.listener.PaymentPushVerificationListener.paymentPushVerification(org.apache.kafka.clients.consumer.ConsumerRecord<java.lang.String, java.lang.String>,org.springframework.kafka.support.Acknowledgment): 1 error(s): [Error in object 'acknowledgment': codes []; arguments []; default message [Payload value must not be empty]] 
