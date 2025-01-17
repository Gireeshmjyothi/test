@Transactional
    @Modifying
    @Query("UPDATE Transaction t " +
            "SET t.transactionStatus = 'FAILED' " +
            "WHERE t.transactionStatus = 'BOOKED' " +
            "AND t.sbiOrderRefNumber IN :sbiOrderRefNumber")
    void updateTransactionStatusToFailed(@Param("sbiOrderRefNumber") List<String> sbiOrderRefNumber);

    @Transactional
    @Modifying
    @Query("UPDATE Orders o, Transaction t " +
            "SET o.status = 'FAILED', t.transactionStatus = 'FAILED' " +
            "WHERE o.sbiOrderRefNumber = t.sbiOrderRefNumber " +
            "AND o.status = 'ATTEMPTED' " +
            "AND t.transactionStatus = 'BOOKED' " +
            "AND t.paymentStatus = 'PAYMENT_INITIATED_START' " +
            "AND o.sbiOrderRefNumber IN :orderRefNumbers")
    void updateOrderAndTransactionStatusToFailed(List<String> orderRefNumbers);


@Transactional
    public void updateStatusOfOrderAndTransaction(List<TokenAndOrderDto> tokenAndOrderDtoList){
        List<String> listOfSbiOrderRefNum = tokenAndOrderDtoList.stream().map(TokenAndOrderDto::getSbiOrderRefNumber).toList();
        transactionRepository.updateTransactionStatusToFailed(listOfSbiOrderRefNum);
        transactionRepository.updateOrderAndTransactionStatusToFailed(listOfSbiOrderRefNum);
    }


Caused by: org.hibernate.query.SyntaxException: At 1:15 and token ',', mismatched input ',' expecting SE [UPDATE Orders o, Transaction t SET o.status = 'FAILED', t.transactionStatus = 'FAILED' WHERE o.sbiOrderRefNumber = t.sbiOrderRefNumber AND o.status = 'ATTEMPTED' AND t.transactionStatus = 'BOOKED' AND t.paymentStatus = 'PAYMENT_INITIATED_START' AND o.sbiOrderRefNumber IN :orderRefNumbers]
