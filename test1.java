SELECT T.* 
           FROM MERCHANT_ORDER_PAYMENTS T
           WHERE TRANSACTION_STATUS ='PENDING'
           AND PAYMENT_STATUS ='PENDING' 
           AND POOLING_STATUS ='P'
           AND (TIMESTAMP '1970-01-01 00:00:00' + NUMTODSINTERVAL(CREATED_DATE_NUM / 1000, 'SECOND')) 
           BETWEEN (TIMESTAMP '1970-01-01 00:00:00' + NUMTODSINTERVAL(1741267929199 / 1000, 'SECOND'))
           AND (TIMESTAMP '1970-01-01 00:00:00' + NUMTODSINTERVAL(1741267929199 / 1000 + 86400, 'SECOND'))
