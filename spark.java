public FileConfigDto findFileConfigById(UUID configId) {
        logger.info("Fetching bank config from admin service: ", configId);
        URI uri = URI.create(reconConfig.getReconProperties().getAdminServiceBasePath() + ADMIN_END_POINT + configId);
        logger.info("Uri : [{}]", uri);
        OpsResponse<BankConfigFileResponse> bankConfigResponse = webClient.post()
                .uri(uri)
                .headers(httpHeaders -> httpHeaders.set(HttpHeaders.ORIGIN, reconConfig.getReconProperties().getAdminServiceOrigin()))
                .exchangeToMono(response -> response.bodyToMono(new ParameterizedTypeReference<OpsResponse<BankConfigFileResponse>>() {
                }))
                .block();

        return bankConfigMapper.mapToDto(Objects.requireNonNull(validateResponse(bankConfigResponse)).getData().getFirst());

    }
