@Transactional
@Modifying
@Query("UPDATE Transaction t " +
       "SET t.transactionStatus = 'FAILED' " +
       "WHERE t.transactionStatus = 'BOOKED' " +
       "AND t.paymentStatus = 'PAYMENT_INITIATED_START' " +
       "AND t.sbiOrderRefNumber IN :sbiOrderRefNumbers")
void updateTransactionStatusToFailed(@Param("sbiOrderRefNumbers") List<String> sbiOrderRefNumbers);

@Transactional
@Modifying
@Query("UPDATE Orders o " +
       "SET o.status = 'FAILED' " +
       "WHERE o.status = 'ATTEMPTED' " +
       "AND o.sbiOrderRefNumber IN :sbiOrderRefNumbers " +
       "AND EXISTS (SELECT 1 FROM Transaction t WHERE t.sbiOrderRefNumber = o.sbiOrderRefNumber " +
       "AND t.transactionStatus = 'BOOKED' AND t.paymentStatus = 'PAYMENT_INITIATED_START')")
void updateOrderStatusToFailed(@Param("sbiOrderRefNumbers") List<String> sbiOrderRefNumbers);
