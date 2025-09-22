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
                    // ❌ Error → Convert to ErrorDto
                    return response.bodyToMono(new ParameterizedTypeReference<ErrorDto>() {})
                            .flatMap(errorDto -> {
                                logger.error("Error from admin service: status={}, message={}", 
                                             response.statusCode(), errorDto.getMessage());
                                return Mono.error(new CustomAdminServiceException(
                                        response.statusCode(),
                                        errorDto.getMessage()
                                ));
                            });
                }
            })
            .block();

    return bankConfigMapper.mapToDto(
            Objects.requireNonNull(validateResponse(bankConfigResponse))
                    .getData()
                    .getFirst()
    );
}
