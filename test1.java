public MerchantNotificationViewDto getMerchantNotification(String mId) {
    logger.info("Preparing request to get merchant notification view for merchant ID: {}", mId);
    
    URI uri = URI.create(getBaseUrl() + MessageFormat.format(MERCHANT_NOTIFICATION_ENDPOINT, mId));

    ResponseDto<MerchantNotificationViewDto> responseDto = getWebClient()
            .post()
            .uri(uri)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                logger.error("Received 4xx error from Admin Service for merchant ID: {}", mId);
                return Mono.error(new PaymentException(ErrorConstants.BAD_REQUEST_ERROR_CODE, 
                    MessageFormat.format(ErrorConstants.BAD_REQUEST_ERROR_MESSAGE, "Admin Service")));
            })
            .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
                logger.error("Received 5xx error from Admin Service for merchant ID: {}", mId);
                return Mono.error(new PaymentException(ErrorConstants.EXTERNAL_SERVICE_ERROR_CODE, 
                    MessageFormat.format(ErrorConstants.EXTERNAL_SERVICE_ERROR_MESSAGE, "Admin Service")));
            })
            .bodyToMono(new ParameterizedTypeReference<ResponseDto<MerchantNotificationViewDto>>() {})
            .block();

    if (responseDto == null || responseDto.getStatus().equals(PaymentConstants.FAILURE_RESPONSE_CODE) || responseDto.getData() == null) {
        logger.error("Merchant Notification not found for merchant ID: {}", mId);
        throw new PaymentException(ErrorConstants.NOT_FOUND_ERROR_CODE, 
                MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_MESSAGE, "Merchant Notification"));
    }

    logger.info("Successfully retrieved Merchant Notification for merchant ID: {}", mId);
    return responseDto.getData(); 
}
