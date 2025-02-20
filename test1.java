public void processPaymentPushVerification(String message,AtomicInteger atomicInteger) throws JsonProcessingException, PaymentException{
        PaymentPushVerificationDto paymentPushVerificationDto= objectMapper.readValue(message, PaymentPushVerificationDto.class);
            if(! (atomicInteger.incrementAndGet() > 3)){
                logger.info("Starting payment push verification for ATRN: {}", paymentPushVerificationDto.getAtrnNumber());
                transactionDao.getTransactionAndOrderDetail(paymentPushVerificationDto)
                        .map(this::buildPaymentVerificationResponse)
                        .map(response -> {
                            if (response.getPaymentInfo().getPushStatus().equalsIgnoreCase(PushResponseStatus)) {
                                logger.info("Push response does not match PushResponseStatus. Skipping push verification.");
                               if(encryptAndSendToMerchant(paymentPushVerificationDto, response))
                                transactionDao.updatePushVerificationStatus(paymentPushVerificationDto.getAtrnNumber(), "Y");
                            }
                            return new PaymentException(ErrorConstants.NOT_FOUND_ERROR_CODE, ErrorConstants.NOT_FOUND_ERROR_MESSAGE);
                        })
                        .orElseThrow(() -> {
                            logger.warn("Push verification process failed due to missing transaction data for ATRN: {}", paymentPushVerificationDto.getAtrnNumber());
                            return new PaymentException(ErrorConstants.NOT_FOUND_ERROR_CODE, ErrorConstants.NOT_FOUND_ERROR_MESSAGE);
                        });
            }else {
                transactionDao.updatePushVerificationStatus(paymentPushVerificationDto.getAtrnNumber(), "F");
                atomicInteger.set(0);
            }
    }


public void processPaymentPushVerification(String message, AtomicInteger atomicInteger) 
        throws JsonProcessingException, PaymentException {
    
    PaymentPushVerificationDto paymentPushVerificationDto = objectMapper.readValue(message, PaymentPushVerificationDto.class);
    String atrnNumber = paymentPushVerificationDto.getAtrnNumber();

    if (atomicInteger.incrementAndGet() > 3) {
        transactionDao.updatePushVerificationStatus(atrnNumber, "F");
        atomicInteger.set(0);
        return;
    }

    logger.info("Starting payment push verification for ATRN: {}", atrnNumber);

    Optional<TransactionResponse> transactionResponseOpt = transactionDao.getTransactionAndOrderDetail(paymentPushVerificationDto);
    
    if (transactionResponseOpt.isEmpty()) {
        logger.warn("Push verification failed due to missing transaction data for ATRN: {}", atrnNumber);
        throw new PaymentException(ErrorConstants.NOT_FOUND_ERROR_CODE, ErrorConstants.NOT_FOUND_ERROR_MESSAGE);
    }

    TransactionResponse response = transactionResponseOpt.get();
    if (response.getPaymentInfo().getPushStatus().equalsIgnoreCase(PushResponseStatus)) {
        return; // Skip push verification if status matches
    }

    logger.info("Push response does not match PushResponseStatus. Processing push verification.");
    if (encryptAndSendToMerchant(paymentPushVerificationDto, response)) {
        transactionDao.updatePushVerificationStatus(atrnNumber, "Y");
    }
}
