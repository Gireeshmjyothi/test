Map<String, String> errorDetails = new HashMap<>();
try {
    HttpStatusCode status = getWebClient().post()
            .uri(smsBasePath + smsURL)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .header(HttpHeaders.AUTHORIZATION, "Basic " + createBasicAuthHeader(smsUserName, smsPassword))
            .bodyValue(requestBody)
            .retrieve()
            .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(String.class)
                    .flatMap(errorBody -> {
                        logger.error("Error response from SMS API: {} for request: {}", errorBody, smsDto);
                        parseErrorResponse(errorBody, errorDetails);
                        return Mono.error(new NotificationException(
                                response.statusCode().toString(),
                                "Error during SMS request: " + errorDetails.getOrDefault("error_desc", "Unknown error")
                        ));
                    })
            )
            .toBodilessEntity()
            .doOnSuccess(response -> {
                logger.info("SMS API response status: {}", response.getStatusCode());
            })
            .doOnError(error -> {
                logger.error("Error occurred while calling SMS API: {}", error.getMessage(), error);
            })
            .map(ResponseEntity::getStatusCode)
            .block();

    if (status != null && status.is2xxSuccessful()) {
        logger.debug("Successful SMS notification for smsData: {}", smsDto);
        return true;
    } else {
        logger.error("Failed SMS notification with status: {} for smsData: {}", status, smsDto);
        throw new NotificationException(NotificationConstant.FAILURE_CODE, 
                MessageFormat.format(NotificationConstant.FAILURE_MSG, "SMS"));
    }
} catch (Exception e) {
    logger.error("Error in SMS notification: {} for smsData: {}", e.getMessage(), smsDto, e);
    throw new NotificationException(NotificationConstant.FAILURE_CODE, 
            MessageFormat.format(NotificationConstant.FAILURE_MSG, "SMS"));
}


private void parseErrorResponse(String errorBody, Map<String, String> errorDetails) {
    Arrays.stream(errorBody.split("&"))
            .map(pair -> pair.split("=", 2)) // Limit split to 2 to prevent ArrayIndexOutOfBounds
            .forEach(pair -> errorDetails.put(pair[0], pair.length > 1 ? pair[1] : ""));
}
