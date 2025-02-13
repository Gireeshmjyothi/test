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
    private final LoggerUtility logger = LoggerFactoryUtility.getLogger(this.getClass());
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
            sendNotification(merchantNotificationViewDto, customerDto, orderDto, notificationTemplateType, requestType);
        } else if (type.isAssignableFrom(TransactionDto.class)) {
            TransactionDto transactionDto = (TransactionDto) dto;
            merchantNotificationViewDto = adminServiceClient.getMerchantNotification(transactionDto.getMerchantId());
            customerDto = orderDao.getCustomerBySbiOrderRefNumber(transactionDto.getSbiOrderRefNumber());
            sendNotification(merchantNotificationViewDto, customerDto, transactionDto, notificationTemplateType, requestType);
        } else {
            throw new PaymentException(ErrorConstants.NOT_FOUND_ERROR_CODE, 
                MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_MESSAGE, "Invalid class type"));
        }
        return "Notification processed successfully.";
    }

    private <T> void sendNotification(MerchantNotificationViewDto merchantNotificationViewDto, CustomerDto customerDto, T object, NotificationTemplateType notificationTemplateType, RequestType requestType) {
        Optional.ofNullable(merchantNotificationViewDto).ifPresent(merchant -> {
            Optional.ofNullable(customerDto).ifPresent(customer -> {
                if ("Y".equalsIgnoreCase(merchant.getEmailAlertCustomer())) {
                    sendEmailNotification(merchant, customer, object, notificationTemplateType, requestType, customer.getEmail());
                }
                if ("Y".equalsIgnoreCase(merchant.getEmailAlertMerchant())) {
                    sendEmailNotification(merchant, customer, object, notificationTemplateType, requestType, merchant.getCommunicationEmail());
                }
                if ("Y".equalsIgnoreCase(merchant.getSmsAlertCustomer())) {
                    sendSmsNotification(object, notificationTemplateType, requestType, customer.getPhoneNumber());
                }
                if ("Y".equalsIgnoreCase(merchant.getSmsAlertMerchant())) {
                    sendSmsNotification(object, notificationTemplateType, requestType, merchant.getMobileNo());
                }
            });
        });
    }

    private <T> void sendEmailNotification(MerchantNotificationViewDto merchant, CustomerDto customer, T object, NotificationTemplateType notificationTemplateType, RequestType requestType, String recipientEmail) {
        PaymentEmailDto emailDto = PaymentEmailDtoFactory.create(merchant, object, notificationTemplateType, requestType, recipientEmail, emailSmsConfig);
        emailNotificationProducer.publish(emailDto.getRequestType(), "TransactionCustomerEmail", emailDto);
    }

    private <T> void sendSmsNotification(T object, NotificationTemplateType notificationTemplateType, RequestType requestType, String mobileNumber) {
        PaymentSmsDto smsDto = PaymentSmsDtoFactory.create(object, notificationTemplateType, requestType, mobileNumber);
        smsNotificationProducer.publish(smsDto.getRequestType(), "TransactionCustomerSms", smsDto);
    }

    public void sendEmail(PaymentEmailDto paymentEmailDto) {
        logger.info("Sending Email Notification");
        notificationDao.sendEmailNotification(paymentEmailDto);
    }

    public void sendSms(PaymentSmsDto paymentSmsDto) {
        logger.info("Sending SMS Notification");
        notificationDao.sendSmsNotification(paymentSmsDto);
    }
}
