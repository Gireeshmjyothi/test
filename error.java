private String encryptPaymentVerificationResponse(PaymentVerificationResponse paymentVerificationResponse, MerchantInfoDTO merchantDto) {
        try {
            return encryptionDecryptionUtil.encryptRequest(objectMapper.writeValueAsString(paymentVerificationResponse), merchantDto);
        } catch (JsonProcessingException e) {
            logger.error("Error in encryptPaymentVerificationData ", e.getMessage());
            throw new TransactionException(ErrorConstants.INVALID_ERROR_CODE, MessageFormat.format(ErrorConstants.INVALID_ERROR_MESSAGE, "paymentVerificationResponse", "Error occurred while encrypting the payment verification response."));
        }
    }
