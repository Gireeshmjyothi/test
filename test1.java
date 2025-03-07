external:
  api:
    sms:
      gateway:
        base:
          path: "https://smsapipprod.sbi.co.in:9443"
        url: "/bmg/sms/epaypgotpdom"
        user: "epaypgotpdom"
        password: "Ep@y1Dpt"
      body:
        content:
          type: "text"
        sender:
          id: "SBIBNK"
        int:
          flag: 0
        charging: 0

spring:
  mail:
    host: "10.176.245.236"
    port: 587
    username: "sbitestclient"
    password: "sbitestclient_7f827c4b3aa6cd1f08d6b9cce2c0c80e"

email:
  recipient: "ebms_uat_receiver@ebmsgits.sbi.co.in"
  from: "ebms_uat_sender@ebmsgits.sbi.co.in"
