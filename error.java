private String encryptPaymentVerificationResponse(PaymentVerificationResponse paymentVerificationResponse, MerchantInfoDTO merchantDto) {
        try {
            return encryptionDecryptionUtil.encryptRequest(objectMapper.writeValueAsString(paymentVerificationResponse), merchantDto);
        } catch (JsonProcessingException e) {
            logger.error("Error in encryptPaymentVerificationData ", e.getMessage());
            throw new TransactionException(ErrorConstants.INVALID_ERROR_CODE, MessageFormat.format(ErrorConstants.INVALID_ERROR_MESSAGE, "paymentVerificationResponse", "Error occurred while encrypting the payment verification response."));
        }
    }



 @ExceptionHandler(value = {JwtException.class})
    public ResponseEntity<Object> handleJwtException(JwtException ex, WebRequest request) {
        String requestUri = ((ServletWebRequest)request).getRequest().getRequestURI();
        logger.error("Error in Authorization ", ex);
        ErrorDto errorDto = ErrorDto.builder()
                .errorCode(String.valueOf(HttpStatus.UNAUTHORIZED.value()))
                .errorMessage(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                TransactionResponse.builder()
                        .status(0)
                        .errors(List.of(errorDto))
                        .build());
    }
