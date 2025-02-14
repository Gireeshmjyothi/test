2025-02-14 14:15:30 [org.springframework.kafka.KafkaListenerEndpointContainer#0-0-C-1] ERROR org.thymeleaf.TemplateEngine  - userId= - correlationId= - [THYMELEAF][org.springframework.kafka.KafkaListenerEndpointContainer#0-0-C-1] Exception processing template "TRANSACTION_SUCCESS": An error happened during template parsing (template: "templates/TRANSACTION_SUCCESS.html")
org.thymeleaf.exceptions.TemplateInputException: An error happened during template parsing (template: "templates/TRANSACTION_SUCCESS.html")
	at org.thymeleaf.templateparser.markup.AbstractMarkupTemplateParser.parse(AbstractMarkupTemplateParser.java:235)


<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <style>
        body {
            font: 12px/18px "Trebuchet MS";
        }
    </style>
</head>
<body>
Transaction of <span th:text="'INR ' + ${gtwPostingAmt}"></span>
on <span th:text="${paymode}"></span>
at <span th:text="${merchantBrandName}"></span>
on <span th:text="${txnDate}"></span>
has been successfully processed through SBIePay.

<br/><br/>
<b>Note - This is an auto-generated email, and you do not need to reply to this.</b>
</body>
</html>

public static Map<String, Object> generatedTransactionNotification(NotificationDto notificationDto) {
        return Map.of("currencyCode", notificationDto.getCurrencyCode(), "getGtwPostingAmount", notificationDto.getGtwPostingAmount(), "getPayMode", notificationDto.getPayMode(), "txnTime", notificationDto.getTxnDate());
    }
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <style>
        body {
            font: 12px/18px "Trebuchet MS";
        }
    </style>
</head>
<body>
    Transaction of <span th:text="${currencyCode} + ' ' + ${gtwPostingAmt}"></span>
    on <span th:text="${paymode}"></span>
    at <span th:text="${merchantBrandName}"></span>
    on <span th:text="${txnDate}"></span>
    has been successfully processed through SBIePay.

    <br/><br/>
    <b>Note - This is an auto-generated email, and you do not need to reply to this.</b>
</body>
</html>
	    
