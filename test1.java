private void processMerchantOrderPaymentINB(List<MerchantOrderPaymentDto> merchantOrderPaymentDtoList) throws GatewayPoolingException {
    logger.info("Processing merchant order payment.");

    // Filter and process records where pooling status is not 'Y' 
    List<Object[]> batchUpdateData = merchantOrderPaymentDtoList.stream()
            .filter(mop -> !"Y".equals(mop.getPoolingStatus()))
            .map(mop -> {
                try {
                    String plainDvRequest = getCallBackResponse(mop.getAtrnNumber(), mop.getDebitAmount());
                    String inbResponse = inbClientService.processingDoubleVerRequest(plainDvRequest);
                    InbMapWebResponse inbMapResponse = new ObjectMapper().convertValue(paymentUtil.getDecryptedData(inbResponse.trim()), InbMapWebResponse.class);

                    return new Object[]{
                            InbUtil.getTransactionStatus(inbMapResponse.getStatus()),
                            InbUtil.getPaymentStatus(inbMapResponse.getStatus()),
                            "Y",  // pooling status
                            mop.getAtrnNumber()
                    };
                } catch (Exception e) {
                    logger.error("Failed to process ATRN {}: {}", mop.getAtrnNumber(), e.getMessage());
                    return null;  // Skip this record in case of errors
                }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

    // Perform batch update if there are records to update
    if (!batchUpdateData.isEmpty()) {
        int[] updatedRecords = merchantOrderPaymentDao.batchUpdateTransactionStatusAndPaymentStatus(batchUpdateData);
        logger.info("Successfully updated {} records.", Arrays.stream(updatedRecords).sum());
    } else {
        logger.debug("No eligible records for updating transaction and payment status.");
    }
}
