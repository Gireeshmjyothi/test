@Transactional
@Modifying
@Query(value = "UPDATE orders o " +
               "SET o.status = 'FAILED' " +
               "WHERE o.sbiOrderRefNumber IN :orderRefNumbers " +
               "AND o.status = 'ATTEMPTED' " +
               "AND EXISTS (SELECT 1 FROM transaction t WHERE t.sbiOrderRefNumber = o.sbiOrderRefNumber AND t.status = 'BOOKED' AND t.paymentStatus = 'PAYMENT_INITIATION_START')", nativeQuery = true)
int updateOrderStatusToFailed(List<String> orderRefNumbers);

@Transactional
@Modifying
@Query(value = "UPDATE transaction t " +
               "SET t.status = 'FAILED' " +
               "WHERE t.sbiOrderRefNumber IN :orderRefNumbers " +
               "AND t.status = 'BOOKED' " +
               "AND t.paymentStatus = 'PAYMENT_INITIATION_START' " +
               "AND EXISTS (SELECT 1 FROM orders o WHERE o.sbiOrderRefNumber = t.sbiOrderRefNumber AND o.status = 'ATTEMPTED')", nativeQuery = true)
int updateTransactionStatusToFailed(List<String> orderRefNumbers);
