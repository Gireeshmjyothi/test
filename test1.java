public boolean processPaymentPushVerification(PaymentPushVerificationDto paymentPushVerificationDto) throws JsonProcessingException {
        boolean processFlag = false;
            logger.info("Fetching payment push data from db.");
            Optional<List<Object[]>> transaction = transactionDao.getTransactionAndOrderDetail(paymentPushVerificationDto);
            if(transaction.isPresent()){
                logger.info("Mapping order and payment info.");
                PaymentVerificationResponse paymentVerificationResponse =  buildPaymentVerificationResponse(transaction.get());
                if(paymentVerificationResponse.getOrderInfo().getReturnUrl().equalsIgnoreCase(PushResponseStatus)){
                    EncryptionKeyDto encryptionKeyDto = kmsServiceClient.getEncryptionKey(paymentPushVerificationDto.getMId());
                    String encryptedPaymentPushData = encryptionDecryptionUtil.encryptRequest(objectMapper.writeValueAsString(paymentVerificationResponse),encryptionKeyDto);
                    boolean merchantPushResponseFlag = merchantServiceClient.postPaymentPushVerification(encryptedPaymentPushData);
                    if(merchantPushResponseFlag){
                        processFlag = transactionDao.updatePushVerificationStatus(paymentPushVerificationDto.getAtrnNumber(), "Y")>0;
                    }
                    return processFlag;
                }
            }else{
                logger.error("Error while fetching payment push verification data.");
            }
        return processFlag;
    }
