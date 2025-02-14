/**
 * This method publishes a generic data notification for email and SMS.
 * 
 * @param dto                     Generic object containing the data.
 * @param type                    The class type of the object.
 * @param notificationEntityType   The type of entity for the notification.
 * @param requestType              The type of email or SMS to be sent.
 * @param <T>                      Generic type parameter.
 * @return                         Notification processing status.
 */
public <T> String publishPaymentNotification(T dto, Class<T> type, NotificationEntityType notificationEntityType, RequestType requestType) {
    logger.info("Processing payment notification for request type: {}", requestType);
    
    MerchantNotificationViewDto merchantNotificationViewDto;
    CustomerDto customerDto;

    if (type.isAssignableFrom(OrderDto.class)) {
        logger.debug("Processing OrderDto object.");
        OrderDto orderDto = (OrderDto) dto;
        merchantNotificationViewDto = adminServiceClient.getMerchantNotification(orderDto.getMId());
        customerDto = customerDao.getCustomerByCustomerId(orderDto.getCustomerId());
    } else if (type.isAssignableFrom(TransactionDto.class)) {
        logger.debug("Processing TransactionDto object.");
        TransactionDto transactionDto = (TransactionDto) dto;
        merchantNotificationViewDto = adminServiceClient.getMerchantNotification(transactionDto.getMerchantId());
        customerDto = orderDao.getCustomerBySbiOrderRefNumber(transactionDto.getSbiOrderRefNumber());
    } else {
        logger.error("Invalid object type provided for notification.");
        throw new PaymentException(ErrorConstants.NOT_FOUND_ERROR_CODE, 
                MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_MESSAGE, "Invalid object type"));
    }

    buildNotification(merchantNotificationViewDto, customerDto, dto, notificationEntityType, requestType);
    logger.info("Notification processed successfully.");
    return "Notification processed successfully.";
}

/**
 * Sends an email notification.
 * 
 * @param paymentEmailDto Contains email details.
 */
public void sendEmail(PaymentEmailDto paymentEmailDto) {
    logger.info("Sending email notification to recipient: {}", paymentEmailDto.getRecipient());
    notificationDao.sendEmailNotification(paymentEmailDto);
}

/**
 * Sends an SMS notification.
 * 
 * @param paymentSmsDto Contains SMS details.
 */
public void sendSms(PaymentSmsDto paymentSmsDto) {
    logger.info("Sending SMS notification to recipient: {}", paymentSmsDto.getMobileNumber());
    notificationDao.sendSmsNotification(paymentSmsDto);
}

/**
 * Builds and publishes email and SMS notifications.
 * 
 * @param merchantNotificationViewDto Merchant notification details.
 * @param customerDto                 Customer details.
 * @param object                      Generic object for data mapping.
 * @param notificationEntityType       Notification entity type.
 * @param type                         Type of email/SMS notification.
 * @param <T>                          Generic type parameter.
 */
private <T> void buildNotification(MerchantNotificationViewDto merchantNotificationViewDto, CustomerDto customerDto, T object, NotificationEntityType notificationEntityType, RequestType type) {
    logger.info("Building notification for type: {}", type);

    if (EMAIL_SMS_CONSENT.equalsIgnoreCase(merchantNotificationViewDto.getEmailAlertCustomer())) {
        logger.info("Publishing EMAIL for customer: {}", customerDto.getEmail());
        emailNotificationProducer.publish(type.name(), "TransactionCustomerEmail", 
                create(merchantNotificationViewDto, object, notificationEntityType, type, customerDto.getEmail(), emailSmsConfig));
    }
    if (EMAIL_SMS_CONSENT.equalsIgnoreCase(merchantNotificationViewDto.getEmailAlertMerchant())) {
        logger.info("Publishing EMAIL for merchant: {}", merchantNotificationViewDto.getCommunicationEmail());
        emailNotificationProducer.publish(type.name(), "TransactionMerchantEmail", 
                create(merchantNotificationViewDto, object, notificationEntityType, type, merchantNotificationViewDto.getCommunicationEmail(), emailSmsConfig));
    }
    if (EMAIL_SMS_CONSENT.equalsIgnoreCase(merchantNotificationViewDto.getSmsAlertCustomer())) {
        logger.info("Publishing SMS for customer: {}", customerDto.getPhoneNumber());
        smsNotificationProducer.publish(type.name(), "TransactionCustomerSms", 
                create(merchantNotificationViewDto, object, notificationEntityType, type, customerDto.getPhoneNumber()));
    }
    if (EMAIL_SMS_CONSENT.equalsIgnoreCase(merchantNotificationViewDto.getSmsAlertMerchant())) {
        logger.info("Publishing SMS for merchant: {}", merchantNotificationViewDto.getMobileNo());
        smsNotificationProducer.publish(type.name(), "TransactionMerchantSms", 
                create(merchantNotificationViewDto, object, notificationEntityType, type, merchantNotificationViewDto.getMobileNo()));
    }
}

/**
 * Maps data for email notifications.
 * 
 * @param merchantNotificationViewDto Merchant notification details.
 * @param object                      Generic object.
 * @param notificationEntityType       Notification entity type.
 * @param requestType                  Email/SMS request type.
 * @param recipientEmail               Email recipient.
 * @param emailSmsConfig               Email/SMS configuration.
 * @param <T>                          Generic type parameter.
 * @return                             Payment email DTO.
 */
private static <T> PaymentEmailDto create(MerchantNotificationViewDto merchantNotificationViewDto, T object, NotificationEntityType notificationEntityType, RequestType requestType, String recipientEmail, EmailSmsConfig emailSmsConfig) {
    logger.debug("Creating email notification for recipient: {}", recipientEmail);

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
 * Maps data for SMS notifications.
 * 
 * @param merchantNotificationViewDto Merchant notification details.
 * @param object                      Generic object.
 * @param notificationEntityType       Notification entity type.
 * @param requestType                  SMS request type.
 * @param mobileNumber                 Mobile number of the recipient.
 * @param <T>                          Generic type parameter.
 * @return                             Payment SMS DTO.
 */
public static <T> PaymentSmsDto create(MerchantNotificationViewDto merchantNotificationViewDto, T object, NotificationEntityType notificationEntityType, RequestType requestType, String mobileNumber) {
    logger.debug("Creating SMS notification for mobile number: {}", mobileNumber);
    return PaymentSmsDto.builder()
            .entityId(UUID.randomUUID())
            .entityType(notificationEntityType)
            .message(getMessage(merchantNotificationViewDto, requestType, object))
            .mobileNumber(mobileNumber)
            .requestType(requestType.getName())
            .build();
}

/**
 * Formats the message for SMS notifications.
 * 
 * @param merchantNotificationViewDto Merchant notification details.
 * @param requestType                 Type of SMS notification.
 * @param object                      Generic object.
 * @param <T>                         Generic type parameter.
 * @return                            Formatted SMS message.
 */
private static <T> String getMessage(MerchantNotificationViewDto merchantNotificationViewDto, RequestType requestType, T object) {
    logger.debug("Generating SMS message for request type: {}", requestType);
    return MessageFormat.format(SmsUtil.getSMSTemplate(requestType),
            object instanceof OrderDto ? ((OrderDto) object).getCurrencyCode() : ((TransactionDto) object).getCurrencyCode(),
            object instanceof OrderDto ? ((OrderDto) object).getOrderAmount() : ((TransactionDto) object).getDebitAmt(),
            object instanceof OrderDto ? ((OrderDto) object).getPaymentMode() : ((TransactionDto) object).getPayMode(),
            merchantNotificationViewDto.getBrandName(),
            new Date());
                }
