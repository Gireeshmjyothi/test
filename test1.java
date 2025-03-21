private void processMerchantOrderPaymentINB(List<MerchantOrderPaymentDto> merchantOrderPaymentDtoList) throws GatewayPoolingException {
    logger.info("Processing merchant order payment.");

    List<Object[]> successList = new ArrayList<>();
    List<String> pendingOrFailedList = new ArrayList<>();
    List<String> failedList = new ArrayList<>();

    merchantOrderPaymentDtoList.stream()
            .filter(mop -> !"Y".equals(mop.getPoolingStatus()))  // Skip already processed records
            .forEach(mop -> {
                try {
                    String plainDvRequest = getCallBackResponse(mop.getAtrnNumber(), mop.getDebitAmount());
                    String inbResponse = inbClientService.processingDoubleVerRequest(plainDvRequest);
                    InbMapWebResponse inbMapResponse = new ObjectMapper().convertValue(
                            paymentUtil.getDecryptedData(inbResponse.trim()), InbMapWebResponse.class);

                    String transactionStatus = InbUtil.getTransactionStatus(inbMapResponse.getStatus());
                    String paymentStatus = InbUtil.getPaymentStatus(inbMapResponse.getStatus());

                    if ("SUCCESS".equalsIgnoreCase(inbMapResponse.getStatus())) {
                        successList.add(new Object[]{transactionStatus, paymentStatus, "Y", mop.getAtrnNumber()});
                    } else {
                        pendingOrFailedList.add(mop.getAtrnNumber());
                    }

                } catch (Exception e) {
                    logger.error("Failed to process ATRN {}: {}", mop.getAtrnNumber(), e.getMessage());
                    failedList.add(mop.getAtrnNumber());
                    pendingOrFailedList.add(mop.getAtrnNumber());
                }
            });

    // Batch update for successful records
    if (!successList.isEmpty()) {
        int[] updatedRecords = merchantOrderPaymentDao.batchUpdateTransactionStatusAndPaymentStatus(successList);
        logger.info("Successfully updated {} records.", Arrays.stream(updatedRecords).sum());
    } else {
        logger.debug("No successful transactions to update.");
    }

    // Log pending/failed transactions
    if (!pendingOrFailedList.isEmpty()) {
        logger.warn("Pending or Failed ATRNs: {}", String.join(", ", pendingOrFailedList));
    }

    // Log failed transactions separately
    if (!failedList.isEmpty()) {
        logger.error("Failed to process ATRNs: {}", String.join(", ", failedList));
    }
}
