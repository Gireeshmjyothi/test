ALTER TABLE MERCHANT_ORDER_PAYMENTS ADD  PUSH_STATUS VARCHAR2(10);


ALTER TABLE MERCHANT_ORDER_PAYMENTS 
ADD POOLING_STATUS VARCHAR2(10) DEFAULT 'P' NOT NULL;

UPDATE MERCHANT_ORDER_PAYMENTS 
SET POOLING_STATUS = 'P' 
WHERE POOLING_STATUS IS NULL;
COMMIT;


@Query("SELECT mopDtl FROM MerchantOrderPaymentEntity mopDtl WHERE mopDtl.transactionStatus IN ('BOOKED', 'PENDING') " +
            "AND mopDtl.paymentStatus IN ('PAYMENT_INITIATION_START', 'PENDING')")
    List<MerchantOrderPaymentEntity> findMerchantOrderPaymentDetails();

@Query("SELECT mopDtl FROM MerchantOrderPaymentEntity mopDtl WHERE " +
       "(mopDtl.transactionStatus IN ('BOOKED', 'PENDING') AND mopDtl.paymentStatus IN ('PAYMENT_INITIATION_START', 'PENDING')) " +
       "AND mopDtl.poolingStatus = 'P'")

