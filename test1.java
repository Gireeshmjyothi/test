<!DOCTYPE html>
SBIePAY_ATRN Booked Customer
<html>
<head>
    <style>
        body {
            font: 12px/18px "Trebuchet MS";
        }
    </style>
</head>
<body>
<br/>
Dear Customer,
<br/></br>
You have booked an order on {Merchant Name (Brand Name)} with the below details:
<br/><br/>
&#45;&nbsp;Merchant Website: {Merchant URL}
<br/>
&#45;&nbsp;Merchant Order Number: {Merchant Order ID}
<br/>
&#45;&nbsp;SBIePay transaction reference number: {ATRN}
<br/>
&#45;&nbsp;Transaction booking date: {Txn Date}
<br/>
&#45;&nbsp;Transaction amount: {Posting Amount}
<br/>
&#45;&nbsp;Transaction status: {Status_Description}
<br/><br/>
For any further assistance, please contact your merchant.
<br/><br/>
Regards
<br/>
SBIePay Team
<br/><br/>
<b>Note - This is an auto generated email and you need not reply to this.</b>
</body>
</html>
</html>



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
on <span th:text="${payMode}"></span>
at <span th:text="${merchantBrandName}"></span>
on <span th:text="${txnDate}"></span>
has been successfully processed through SBIePay.

<br/><br/>
<b>Note - This is an auto-generated email, and you do not need to reply to this.</b>
</body>
</html>
