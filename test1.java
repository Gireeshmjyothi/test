public boolean processPaymentPushVerification(PaymentPushVerificationDto dto) throws JsonProcessingException {
        logger.info("Starting payment push verification for ATRN: {}", dto.getAtrnNumber());
        return transactionDao.getTransactionAndOrderDetail(dto)
                .map(this::buildPaymentVerificationResponse)
                .map(response -> {
                    if (!response.getPaymentInfo().getPushStatus().equalsIgnoreCase(PushResponseStatus)) {
                        logger.info("Push response does not match PushResponseStatus. Skipping push verification.");
                        return true;
                    }
                    return encryptAndSendToMerchant(dto, response) && updatePushVerificationStatus(dto.getAtrnNumber());
                })
                .orElseGet(() -> {
                    logger.warn("Push verification process failed due to missing transaction data for ATRN: {}", dto.getAtrnNumber());
                    return false;
                });
    }


private PaymentVerificationResponse buildPaymentVerificationResponse(List<Object[]> transactionOrderList) {
        logger.info("Mapping Order data.");
        // Retrieve the record from object[]
        if(CollectionUtils.isEmpty(transactionOrderList)) ;

        Object[] record = transactionOrderList.get(0);

        PaymentVerificationDto paymentVerificationDto = paymentVerificationMapper.transactionToPaymentVerificationDto((Transaction) record[0]);
        OrderInfoDto orderDto = paymentVerificationMapper.orderToOrderInfoDto((Order) record[1]);

        logger.info("Transaction and Order data mapped.");
        // Build and return the response
        return PaymentVerificationResponse.builder()
                .paymentInfo(paymentVerificationDto)
                .orderInfo(orderDto)
                .build();
    }
