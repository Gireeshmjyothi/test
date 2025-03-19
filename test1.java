@Query("SELECT mopDtl FROM MerchantOrderPaymentEntity mopDtl WHERE " +
            "(mopDtl.transactionStatus IN ('BOOKED', 'PENDING') AND mopDtl.paymentStatus IN ('PAYMENT_INITIATION_START', 'PENDING')) " +
            "AND mopDtl.poolingStatus = 'P'")
    List<MerchantOrderPaymentEntity> findMerchantOrderPaymentDetails();
