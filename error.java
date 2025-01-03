private PaymentVerificationResponse buildPaymentVerificationResponse(List<Object[]> transactionOrderList) {
        logger.info("Mapping Order data.");
        OrderInfoDto orderDto = objectMapper.convertValue(transactionOrderList.getFirst()[1], OrderInfoDto.class);
        Order firstOrder = (Order) transactionOrderList.getFirst()[1];
        orderDto.setSbiOrderId(ObjectUtils.isEmpty(firstOrder.getSbiOrderRefNumber()) ? "" : firstOrder.getSbiOrderRefNumber());
        orderDto.setMerchantOrderNumber(ObjectUtils.isEmpty(firstOrder.getOrderRefNumber()) ? "" : firstOrder.getOrderRefNumber());
        orderDto.setOrderStatus(ObjectUtils.isEmpty(firstOrder.getStatus()) ? OrderStatus.valueOf("") : firstOrder.getStatus());
        orderDto.setCurrency(ObjectUtils.isEmpty(firstOrder.getCurrencyCode()) ? "" : firstOrder.getCurrencyCode());

        logger.info("Mapping Transaction data.");
        List<PaymentVerificationDto> transactionsDTOs = transactionOrderList.stream().map(record -> {
            PaymentVerificationDto paymentVerificationDto = objectMapper.convertValue(record[0], PaymentVerificationDto.class);
            Transaction transaction = ((Transaction) record[0]);
            paymentVerificationDto.setAtrn(ObjectUtils.isEmpty(transaction.getAtrnNumber()) ? "" : transaction.getAtrnNumber());
            paymentVerificationDto.setOrderAmount(ObjectUtils.isEmpty(firstOrder.getOrderAmount()) ? BigDecimal.ZERO : firstOrder.getOrderAmount());
            paymentVerificationDto.setTotalAmount(ObjectUtils.isEmpty(transaction.getDebitAmount()) ? BigDecimal.ZERO : transaction.getDebitAmount());
            paymentVerificationDto.setTransactionStatus(ObjectUtils.isEmpty(transaction.getTransactionStatus()) ? "" : transaction.getTransactionStatus());
            paymentVerificationDto.setPayMode(ObjectUtils.isEmpty(transaction.getPaymentStatus()) ? "" : transaction.getPayMode());
            paymentVerificationDto.setBankName(ObjectUtils.isEmpty(transaction.getChannelBank()) ? "" : transaction.getChannelBank());
            paymentVerificationDto.setBankTxnNumber(ObjectUtils.isEmpty(transaction.getBankReferenceNumber()) ? "" : transaction.getBankReferenceNumber());
            paymentVerificationDto.setProcessor(ObjectUtils.isEmpty(transaction.getChannelBank()) ? "" : transaction.getChannelBank());
            paymentVerificationDto.setTransactionTime(ObjectUtils.isEmpty(transaction.getCreatedDate()) ? Long.valueOf("0") : transaction.getCreatedDate());
            paymentVerificationDto.setCIN(ObjectUtils.isEmpty(transaction.getCin()) ? "" : transaction.getCin());

            return paymentVerificationDto;
        }).collect(Collectors.toList());

        logger.info("Transaction and Order data mapped.");
        return PaymentVerificationResponse.builder()
                .paymentInfo(transactionsDTOs)
                .orderInfo(orderDto)
                .build();
    }

private PaymentVerificationResponse buildPaymentVerificationResponse(List<Object[]> transactionOrderList) {
    logger.info("Mapping Order data.");
    
    // Retrieve the first order data and map to OrderInfoDto
    Object[] firstRecord = transactionOrderList.get(0);
    Order firstOrder = (Order) firstRecord[1];
    OrderInfoDto orderDto = mapOrderToDto(firstOrder);

    logger.info("Mapping Transaction data.");
    // Map transactions to PaymentVerificationDto
    List<PaymentVerificationDto> transactionsDTOs = transactionOrderList.stream()
            .map(this::mapTransactionToDto)
            .collect(Collectors.toList());

    logger.info("Transaction and Order data mapped.");
    // Build and return the response
    return PaymentVerificationResponse.builder()
            .paymentInfo(transactionsDTOs)
            .orderInfo(orderDto)
            .build();
}

private OrderInfoDto mapOrderToDto(Order order) {
    return OrderInfoDto.builder()
            .sbiOrderId(ObjectUtils.defaultIfNull(order.getSbiOrderRefNumber(), ""))
            .merchantOrderNumber(ObjectUtils.defaultIfNull(order.getOrderRefNumber(), ""))
            .orderStatus(ObjectUtils.defaultIfNull(order.getStatus(), OrderStatus.UNKNOWN))
            .currency(ObjectUtils.defaultIfNull(order.getCurrencyCode(), ""))
            .build();
}

private PaymentVerificationDto mapTransactionToDto(Object[] record) {
    Transaction transaction = (Transaction) record[0];
    return PaymentVerificationDto.builder()
            .atrn(ObjectUtils.defaultIfNull(transaction.getAtrnNumber(), ""))
            .orderAmount(ObjectUtils.defaultIfNull(transaction.getOrderAmount(), BigDecimal.ZERO))
            .totalAmount(ObjectUtils.defaultIfNull(transaction.getDebitAmount(), BigDecimal.ZERO))
            .transactionStatus(ObjectUtils.defaultIfNull(transaction.getTransactionStatus(), ""))
            .payMode(ObjectUtils.defaultIfNull(transaction.getPayMode(), ""))
            .bankName(ObjectUtils.defaultIfNull(transaction.getChannelBank(), ""))
            .bankTxnNumber(ObjectUtils.defaultIfNull(transaction.getBankReferenceNumber(), ""))
            .processor(ObjectUtils.defaultIfNull(transaction.getChannelBank(), ""))
            .transactionTime(ObjectUtils.defaultIfNull(transaction.getCreatedDate(), 0L))
            .cin(ObjectUtils.defaultIfNull(transaction.getCin(), ""))
            .build();
}
