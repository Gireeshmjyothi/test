public MerchantNotificationViewDto getMerchantNotification(String mId) {
        logger.info("Preparing Request to get merchant notification view.");
        URI uri = URI.create(getBaseUrl() + MessageFormat.format(MERCHANT_NOTIFICATION_ENDPOINT, mId));
       ResponseDto<MerchantNotificationViewDto> responseDto = getWebClient()
                .post()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new PaymentException(ErrorConstants.EXTERNAL_SERVICE_ERROR_CODE, MessageFormat.format(ErrorConstants.EXTERNAL_SERVICE_ERROR_MESSAGE, "Admin Service"))))
                .bodyToMono(ResponseDto.class)
                .block();

        if (responseDto.getStatus().equals(PaymentConstants.FAILURE_RESPONSE_CODE)) {
            throw new PaymentException(ErrorConstants.NOT_FOUND_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_MESSAGE, "Merchant Notification"));
        }

        Map<String, String> response = objectMapper.convertValue(responseDto.getData().getFirst(), Map.class);

        return MerchantNotificationViewDto.builder()
                .emailAlertMerchant(response.get("emailAlertMerchant"))
                .emailAlertCustomer(response.get("emailAlertCustomer"))
                .smsAlertMerchant(response.get("smsAlertMerchant"))
                .smsAlertCustomer(response.get("smsAlertCustomer"))
                .communicationEmail(response.get("communicationEmail"))
                .merchPushResponseFlag(response.get("merchPushResponseFlag"))
                .mobileNo(response.get("mobileNo"))
                .mId(response.get("mid"))
                .brandName(response.get("brandName"))
                .businessName(response.get("businessName"))
                .build();

    }
