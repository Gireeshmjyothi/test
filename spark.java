com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Cannot construct instance of `com.epay.operations.recon.spark.dto.ErrorDto` (no Creators, like default constructor, exist): cannot deserialize from Object value (no delegate- or property-based Creator)
 at [Source: REDACTED (`StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION` disabled); line: 1, column: 24] (through reference chain: com.epay.operations.recon.spark.externalservice.model.response.OpsResponse["errors"]->java.util.ArrayList[0])

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
