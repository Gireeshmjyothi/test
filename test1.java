spring:
  kafka:
    properties:
      security.protocol: S
      ssl:
        truststore:
          location: C:/certs/kafka/dev-cluster-cluster-ca-cert
          password: Xe8FrxOG
          type: PKCS
        keystore:
          location: C:/certs/kafka/dev-cluster-clients-ca-cert
          password: J55Fooopd
          type: P
    consumer:
      number-of-consumer: 1
