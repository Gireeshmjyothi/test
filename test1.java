package com.epay.payment.service;

import com.epay.payment.config.EmailSmsConfig;
import com.epay.payment.dao.CustomerDao;
import com.epay.payment.dao.NotificationDao;
import com.epay.payment.dao.OrderDao;
import com.epay.payment.dto.*;
import com.epay.payment.etl.producer.EmailNotificationProducer;
import com.epay.payment.etl.producer.SmsNotificationProducer;
import com.epay.payment.exceptions.PaymentException;
import com.epay.payment.externalservice.AdminServiceClient;
import com.epay.payment.util.EmailUtil;
import com.epay.payment.util.ErrorConstants;
import com.epay.payment.util.SmsUtil;
import com.epay.payment.util.enums.NotificationTemplateType;
import com.epay.payment.util.enums.RequestType;
import com.sbi.epay.logging.utility.LoggerFactoryUtility;
import com.sbi.epay.logging.utility.LoggerUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentNotificationService {
    private static final LoggerUtility LOGGER = LoggerFactoryUtility.getLogger(PaymentNotificationService.class);
    private final NotificationDao notificationDao;
    private final AdminServiceClient adminServiceClient;
    private final EmailNotificationProducer emailNotificationProducer;
    private final SmsNotificationProducer smsNotificationProducer;
    private final EmailSmsConfig emailSmsConfig;
    private final CustomerDao customerDao;
    private final OrderDao orderDao;

    public <T> String publishPaymentNotification(T dto, Class<T> type, NotificationTemplateType notificationTemplateType, RequestType requestType) {
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
            throw new PaymentException(ErrorConstants.NOT_FOUND_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_MESSAGE, "This given Class type"));
        }

        sendNotification(merchantNotificationViewDto, customerDto, dto, notificationTemplateType, requestType);
        return "Notification processed successfully.";
    }

    private <T> void sendNotification(MerchantNotificationViewDto merchantNotificationViewDto, CustomerDto customerDto, T object, NotificationTemplateType notificationTemplateType, RequestType type) {
        if ("Y".equalsIgnoreCase(merchantNotificationViewDto.getEmailAlertCustomer())) {
            emailNotificationProducer.publish(type.name(), "TransactionCustomerEmail", PaymentEmailDtoFactory.create(merchantNotificationViewDto, object, notificationTemplateType, type, customerDto.getEmail(), emailSmsConfig));
        }
        if ("Y".equalsIgnoreCase(merchantNotificationViewDto.getEmailAlertMerchant())) {
            emailNotificationProducer.publish(type.name(), "TransactionMerchantEmail", PaymentEmailDtoFactory.create(merchantNotificationViewDto, object, notificationTemplateType, type, merchantNotificationViewDto.getCommunicationEmail(), emailSmsConfig));
        }
        if ("Y".equalsIgnoreCase(merchantNotificationViewDto.getSmsAlertCustomer())) {
            smsNotificationProducer.publish(type.name(), "TransactionCustomerSms", PaymentSmsDtoFactory.create(object, notificationTemplateType, type, customerDto.getPhoneNumber()));
        }
        if ("Y".equalsIgnoreCase(merchantNotificationViewDto.getSmsAlertMerchant())) {
            smsNotificationProducer.publish(type.name(), "TransactionMerchantSms", PaymentSmsDtoFactory.create(object, notificationTemplateType, type, merchantNotificationViewDto.getMobileNo()));
        }
    }
}

class PaymentEmailDtoFactory {
    public static <T> PaymentEmailDto create(MerchantNotificationViewDto merchantNotificationViewDto, T object, NotificationTemplateType notificationTemplateType, RequestType requestType, String recipientEmail, EmailSmsConfig emailSmsConfig) {
        NotificationDto notificationDto = NotificationDto.builder()
                .merchantBrandName(merchantNotificationViewDto.getBrandName())
                .gtwPostingAmount(object instanceof OrderDto ? ((OrderDto) object).getOrderAmount() : ((TransactionDto) object).getDebitAmt())
                .currencyCode("INR")
                .payMode(object instanceof OrderDto ? ((OrderDto) object).getPaymentMode() : ((TransactionDto) object).getPayMode())
                .txnDate(new Date())
                .build();

        return PaymentEmailDto.builder()
                .entityId(UUID.randomUUID())
                .entityType(notificationTemplateType)
                .requestType("Email")
                .emailTemplate(EmailUtil.getEMailType(requestType))
                .from(emailSmsConfig.getFrom())
                .recipient(recipientEmail)
                .bcc(emailSmsConfig.getRecipient())
                .body(EmailUtil.generatedTransactionNotification(notificationDto))
                .subject(EmailUtil.getEMailType(requestType).getSubjectName())
                .build();
    }
}

class PaymentSmsDtoFactory {
    public static <T> PaymentSmsDto create(T object, NotificationTemplateType notificationTemplateType, RequestType requestType, String mobileNumber) {
        return PaymentSmsDto.builder()
                .entityId(UUID.randomUUID())
                .entityType(notificationTemplateType)
                .message(getMessage(requestType, object))
                .mobileNumber(mobileNumber)
                .requestType("SMS")
                .build();
    }

    private static <T> String getMessage(RequestType requestType, T object) {
        return MessageFormat.format(SmsUtil.getSMSTemplate(requestType),
                "INR",
                object instanceof OrderDto ? ((OrderDto) object).getOrderAmount() : ((TransactionDto) object).getDebitAmt(),
                object instanceof OrderDto ? ((OrderDto) object).getPaymentMode() : ((TransactionDto) object).getPayMode(),
                new Date());
    }
}

<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <style>
        body {
            font-family: "Trebuchet MS", sans-serif;
            font-size: 12px;
            line-height: 18px;
            color: #333;
        }
    </style>
</head>
<body>
    <br/>
    Dear Customer,
    <br/><br/>
    You have booked an order on <b th:text="${merchantBrandName}"></b> with the below details:
    <br/><br/>
    
    &#45;&nbsp;Merchant Website: <b th:text="${merchantUrl}"></b>  
    <br/>
    &#45;&nbsp;Merchant Order Number: <b th:text="${merchantOrderId}"></b>  
    <br/>
    &#45;&nbsp;SBIePay Transaction Reference Number: <b th:text="${atrn}"></b>  
    <br/>
    &#45;&nbsp;Transaction Booking Date: <b th:text="${txnDate}"></b>  
    <br/>
    &#45;&nbsp;Transaction Amount: <b th:text="${currencyCode} + ' ' + ${gtwPostingAmount}"></b>  
    <br/>
    &#45;&nbsp;Transaction Status: <b th:text="${statusDescription}"></b>  
    <br/><br/>

    For any further assistance, please contact your merchant.
    <br/><br/>

    Regards,  
    <br/>
    <b>SBIePay Team</b>  
    <br/><br/>

    <b>Note - This is an auto-generated email, and you do not need to reply.</b>
</body>
</html>
