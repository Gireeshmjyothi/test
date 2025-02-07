<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SBIePAY ATRN Booked Customer</title>
    <style>
        body {
            font-family: "Trebuchet MS", sans-serif;
            font-size: 12px;
            line-height: 18px;
        }
    </style>
</head>
<body>
    <p>Dear Customer,</p>

    <p>
        You have booked an order on <b>{Merchant Name (Brand Name)}</b> with the following details:
    </p>

    <ul>
        <li>Merchant Website: <b>{Merchant URL}</b></li>
        <li>Merchant Order Number: <b>{Merchant Order ID}</b></li>
        <li>SBIePay Transaction Reference Number: <b>{ATRN}</b></li>
        <li>Transaction Booking Date: <b>{Txn Date}</b></li>
        <li>Transaction Amount: <b>{Posting Amount}</b></li>
        <li>Transaction Status: <b>{Status_Description}</b></li>
    </ul>

    <p>
        For any further assistance, please contact your merchant.
    </p>

    <p>Regards,</p>
    <p><b>SBIePay Team</b></p>

    <p><b>Note:</b> This is an auto-generated email, and you do not need to reply to this.</p>
</body>
</html>
