ALTER TABLE MERCHANT_ORDER_PAYMENTS ADD  PUSH_STATUS VARCHAR2(10);


ALTER TABLE MERCHANT_ORDER_PAYMENTS 
ADD POOLING_STATUS VARCHAR2(10) DEFAULT 'P' NOT NULL;

  kafka:
    bootstrapServers: dev-cluster-kafka-bootstrap-dev-kafka.apps.dev.sbiepay.sbi:443
    consumer:
      groupId: gatewayPooling-consumers
      keyDeserializer: org.apache.kafka.common.serialization.StringDeserializer
      valueDeserializer: org.apache.kafka.common.serialization.StringDeserializer
      autoOffsetReset: latest # consumer reading msg from latest offset
      autoCommitInterval: 100 # interval of auto commit
      enableAutoCommit: true #ensure offset are updated periodically without manual intervention
      sessionTimeoutMS: 300000
      requestTimeoutMS: 420000 #max time broker wait to collect data before responding a request
      fetchMaxWaitMS: 200 # max time the broker waits
      maxPollRecords: 5 # max number of the records consumer return in single poll operation
      retryMaxAttempts: 3
      retryBackOffInitialIntervalMS: 10000
      retryBackOffMaxIntervalMS: 30000
      retryBackOffMultiplier: 2
      spring.json.trusted.packages: com.epay.reporting
      numberOfConsumers: 1





