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
