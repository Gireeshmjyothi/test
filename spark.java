
public FileConfigDto findFileConfigById(UUID configId) {
    logger.info("Fetching bank config from admin service: {}", configId);

    URI uri = URI.create(
            reconConfig.getReconProperties().getAdminServiceBasePath()
                    + ADMIN_END_POINT
                    + configId
    );
    logger.info("Uri : [{}]", uri);

    OpsResponse<BankConfigFileResponse> bankConfigResponse = webClient.post()
            .uri(uri)
            .headers(httpHeaders -> httpHeaders.set(
                    HttpHeaders.ORIGIN,
                    reconConfig.getReconProperties().getAdminServiceOrigin()
            ))
            .exchangeToMono(response -> {
                if (response.statusCode().is2xxSuccessful()) {
                    // ✅ Success
                    return response.bodyToMono(new ParameterizedTypeReference<OpsResponse<BankConfigFileResponse>>() {});
                } else {
                    // ❌ Error → Log and return null
                    return response.bodyToMono(new ParameterizedTypeReference<ErrorDto>() {})
                            .doOnNext(errorDto -> 
                                    logger.error("Admin service error: status={}, message={}", 
                                            response.statusCode(), errorDto.getMessage()))
                            .then(Mono.empty()); // return empty instead of throwing
                }
            })
            .block();

    if (bankConfigResponse == null || bankConfigResponse.getData().isEmpty()) {
        logger.warn("No bank config found for configId={}", configId);
        return null;
    }

    return bankConfigMapper.mapToDto(
            Objects.requireNonNull(validateResponse(bankConfigResponse))
                    .getData()
                    .getFirst()
    );
}
