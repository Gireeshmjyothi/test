private void processMerchantOrderPaymentINB(List<MerchantOrderPaymentDto> merchantOrderPaymentDtoList) throws GatewayPoolingException {
        logger.info("Processing merchant order payment.");
        merchantOrderPaymentDtoList.forEach(mop -> {
            String plainDvRequest = getCallBackResponse(mop.getAtrnNumber(), mop.getDebitAmount());
            String inbResponse = inbClientService.processingDoubleVerRequest(plainDvRequest);
            InbMapWebResponse inbMapResponse = new ObjectMapper().convertValue(paymentUtil.getDecryptedData(inbResponse.trim()), InbMapWebResponse.class);
            if (merchantOrderPaymentDao.updateTransactionStatusAndPaymentStatusByATRN(mop.getAtrnNumber(),
                    InbUtil.getTransactionStatus(inbMapResponse.getStatus()),
                    InbUtil.getPaymentStatus(inbMapResponse.getStatus()),
                    "Y") <= 0) {
                logger.debug("Transaction status and Payment status not updated for the ATRN : {}", mop.getAtrnNumber());
            }
        });
    }
