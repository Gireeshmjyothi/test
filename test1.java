/**
     * This method a generic data to publish email and sms.
     * @param dto generic object.
     * @param type class type.
     * @param notificationEntityType entity notification type.
     * @param requestType type of email and sms needs to be sent.
     * @param <T> to maintain generic.
     */
    public <T> String publishPaymentNotification(T dto, Class<T> type, NotificationEntityType notificationEntityType, RequestType requestType) {
        logger.info("");
        MerchantNotificationViewDto merchantNotificationViewDto;
        CustomerDto customerDto;
        
        if (type.isAssignableFrom(OrderDto.class)) {
            OrderDto orderDto = (OrderDto) dto;
            merchantNotificationViewDto = adminServiceClient.getMerchantNotification(orderDto.getMId());
            customerDto = customerDao.getCustomerByCustomerId(orderDto.getCustomerId());
        } else if (type.isAssignableFrom(TransactionDto.class)) {
            TransactionDto transactionDto = (TransactionDto) dto;
            merchantNotificationViewDto = adminServiceClient.getMerchantNotification(transactionDto.getMerchantId());
            customerDto = orderDao.getCustomerBySbiOrderRefNumber(transactionDto.getSbiOrderRefNumber());
        } else {
            throw new PaymentException(ErrorConstants.NOT_FOUND_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_MESSAGE, ""));
        }

        buildNotification(merchantNotificationViewDto, customerDto, dto, notificationEntityType, requestType);
        return "Notification processed successfully.";
    }

    /**
     * This method is used to send an email.
     * @param paymentEmailDto contains email details
     */
    public void sendEmail(PaymentEmailDto paymentEmailDto) {
        logger.info("Sending EMAIL to the respective user.");
        notificationDao.sendEmailNotification(paymentEmailDto);
    }

    /**
     * This method is used to send an email.
     * @param paymentSmsDto contains sms details
     */
    public void sendSms(PaymentSmsDto paymentSmsDto) {
        logger.info("Sending SMS to the respective user.");
        notificationDao.sendSmsNotification(paymentSmsDto);
    }

    /**
     * This method is used for publish Email and Sms.
     * @param merchantNotificationViewDto merchant notification details.
     * @param customerDto customer details.
     * @param object generic object for data mapping.
     * @param notificationEntityType entity notification type
     * @param type type of email and sms needs to be sent.
     * @param <T> to maintain generic.
     */
    private <T> void buildNotification(MerchantNotificationViewDto merchantNotificationViewDto, CustomerDto customerDto, T object, NotificationEntityType notificationEntityType, RequestType type) {
        if (EMAIL_SMS_CONSENT.equalsIgnoreCase(merchantNotificationViewDto.getEmailAlertCustomer())) {
            logger.info("Publishing EMAIL for customer.");
            emailNotificationProducer.publish(type.name(), "TransactionCustomerEmail", create(merchantNotificationViewDto, object, notificationEntityType, type, customerDto.getEmail(), emailSmsConfig));
        }
        if (EMAIL_SMS_CONSENT.equalsIgnoreCase(merchantNotificationViewDto.getEmailAlertMerchant())) {
            logger.info("Publishing EMAIL for merchant.");
            emailNotificationProducer.publish(type.name(), "TransactionMerchantEmail", create(merchantNotificationViewDto, object, notificationEntityType, type, merchantNotificationViewDto.getCommunicationEmail(), emailSmsConfig));
        }
        if (EMAIL_SMS_CONSENT.equalsIgnoreCase(merchantNotificationViewDto.getSmsAlertCustomer())) {
            logger.info("Publishing SMS for customer.");
            smsNotificationProducer.publish(type.name(), "TransactionCustomerSms", create(merchantNotificationViewDto, object, notificationEntityType, type, customerDto.getPhoneNumber()));
        }
        if (EMAIL_SMS_CONSENT.equalsIgnoreCase(merchantNotificationViewDto.getSmsAlertMerchant())) {
            logger.info("Publishing SMS for merchant.");
            smsNotificationProducer.publish(type.name(), "TransactionMerchantSms", create(merchantNotificationViewDto, object, notificationEntityType, type, merchantNotificationViewDto.getMobileNo()));
        }
    }

    /**
     * This method is used to map data for email.
     * @param merchantNotificationViewDto merchant notification details.
     * @param object generic object.
     * @param notificationEntityType notification entity type.
     * @param requestType type of email and sms needs to be sent.
     * @param recipientEmail email recipient.
     * @param emailSmsConfig email and sms data from.
     * @return payment email dto.
     * @param <T> keep generic.
     */
    private static <T> PaymentEmailDto create(MerchantNotificationViewDto merchantNotificationViewDto, T object, NotificationEntityType notificationEntityType, RequestType requestType, String recipientEmail, EmailSmsConfig emailSmsConfig) {
        NotificationDto notificationDto = NotificationDto.builder()
                .merchantBrandName(merchantNotificationViewDto.getBrandName())
                .gtwPostingAmount(object instanceof OrderDto ? ((OrderDto) object).getOrderAmount() : ((TransactionDto) object).getDebitAmt())
                .currencyCode(object instanceof OrderDto ? ((OrderDto) object).getCurrencyCode() : ((TransactionDto) object).getCurrencyCode())
                .payMode(object instanceof OrderDto ? ((OrderDto) object).getPaymentMode() : ((TransactionDto) object).getPayMode())
                .txnDate(new Date())
                .build();

        return PaymentEmailDto.builder()
                .entityId(UUID.randomUUID())
                .entityType(notificationEntityType)
                .emailTemplate(EmailUtil.getEMailType(requestType))
                .requestType(requestType.getName())
                .from(emailSmsConfig.getFrom())
                .recipient(recipientEmail)
                .bcc(emailSmsConfig.getRecipient())
                .body(EmailUtil.generatedTransactionNotification(notificationDto))
                .subject(EmailUtil.getEMailType(requestType).getSubjectName())
                .build();
    }

    /**
     * This method is used to map data for sms.
     * @param merchantNotificationViewDto merchant notification details.
     * @param object generic object.
     * @param notificationEntityType notification entity type.
     * @param requestType type of email needs to be sent.
     * @param mobileNumber mobile for receiver.
     * @return payment sms dto.
     * @param <T> keep generic.
     */
    public static <T> PaymentSmsDto create(MerchantNotificationViewDto merchantNotificationViewDto, T object, NotificationEntityType notificationEntityType, RequestType requestType, String mobileNumber) {
        return PaymentSmsDto.builder()
                .entityId(UUID.randomUUID())
                .entityType(notificationEntityType)
                .message(getMessage(merchantNotificationViewDto, requestType, object))
                .mobileNumber(mobileNumber)
                .requestType(requestType.getName())
                .build();
    }

    /**
     * This method is used format the message
     * @param merchantNotificationViewDto merchant notification details.
     * @param requestType type of sms needs to be sent.
     * @param object  generic object.
     * @return formatted message.
     * @param <T> keep generic.
     */
    private static <T> String getMessage(MerchantNotificationViewDto merchantNotificationViewDto, RequestType requestType, T object) {
        return MessageFormat.format(SmsUtil.getSMSTemplate(requestType),
                object instanceof OrderDto ? ((OrderDto) object).getCurrencyCode() : ((TransactionDto) object).getCurrencyCode(),
                object instanceof OrderDto ? ((OrderDto) object).getOrderAmount() : ((TransactionDto) object).getDebitAmt(),
                object instanceof OrderDto ? ((OrderDto) object).getPaymentMode() : ((TransactionDto) object).getPayMode(),
                merchantNotificationViewDto.getBrandName(),
                new Date());
    }
