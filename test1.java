 @Query("""
        SELECT t, o
        FROM Transaction t
        INNER JOIN Order o
        ON t.orderRefNumber = o.orderRefNumber
        WHERE t.atrnNumber = :atrnNumber
        AND (:orderRefNumber IS NULL OR t.orderRefNumber = :orderRefNumber)
        AND (:sbiOrderRefNumber IS NULL OR t.sbiOrderRefNumber = :sbiOrderRefNumber)
        AND (:orderAmount IS NULL OR o.orderAmount = :orderAmount)
    """)
    Optional<List<Object[]>> findTransactionWithOrder(
            @Param("atrnNumber") String atrnNumber,
            @Param("orderRefNumber") String orderRefNumber,
            @Param("sbiOrderRefNumber") String sbiOrderRefNumber,
            @Param("orderAmount") BigDecimal orderAmount);


private PaymentPushStatusVerificationResponse mapListOfObjectToPaymentPushStatusVerificationResponse(List<Object[]> transactionResponse) {
        if(transactionResponse.isEmpty()){
            throw new TransactionException(ErrorConstants.NOT_FOUND_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_MESSAGE, "Transaction"));
        }
        Object[] transactionsRaw = transactionResponse.getFirst();
        List<PaymentInfoDto> transactions = new ArrayList<>();
        OrderDetailDto order = null;
            PaymentInfoDto transaction = objectMapper.convertValue(transactionsRaw[0], PaymentInfoDto.class);
            transactions.add(transaction);
            
        Object[] orderRaw = transactionResponse.get(1);
        
        if (orderRaw != null) {
            order = objectMapper.convertValue(orderRaw[1], OrderDetailDto.class);
        }
        return PaymentPushStatusVerificationResponse.builder()
                .paymentInfo(transactions)
                .orderInfo(order)
                .build();
    }

 @Query("""
            SELECT t, o
            FROM Transaction t
            INNER JOIN Order o
            ON t.orderRefNumber = o.orderRefNumber
            WHERE t.orderRefNumber = :orderRefNumber
            AND t.merchantId = :merchantId
            AND o.orderAmount = :orderAmount
            AND (:atrnNumber IS NULL OR t.atrnNumber = :atrnNumber)
            AND (:sbiOrderRefNumber IS NULL OR t.sbiOrderRefNumber = :sbiOrderRefNumber)
            """)
