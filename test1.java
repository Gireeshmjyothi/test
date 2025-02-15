@Query(value = """
                SELECT t, o
                FROM Transaction t
                INNER JOIN Order o
                ON t.orderRefNumber = o.orderRefNumber
                WHERE t.atrnNumber = :atrnNumber
                AND t.pushResponse = 'N'
                AND (:orderRefNumber IS NULL OR t.orderRefNumber = :orderRefNumber)
                AND (:sbiOrderRefNumber IS NULL OR t.sbiOrderRefNumber = :sbiOrderRefNumber)
                AND (:orderAmount IS NULL OR o.orderAmount = :orderAmount)
            """)
Optional<List<Object[]>> fetchTransactionAndOrderDetail(
        @Param("atrnNumber") String atrnNumber,
        @Param("orderRefNumber") String orderRefNumber,
        @Param("sbiOrderRefNumber") String sbiOrderRefNumber,
        @Param("orderAmount") BigDecimal orderAmount);



@Modifying
@Query(value = """
        UPDATE Transaction t 
        SET t.pushResponse = :newStatus
        WHERE t.atrnNumber = :atrnNumber
    """)
int updatePushResponseStatus(
        @Param("atrnNumber") String atrnNumber,
        @Param("newStatus") String newStatus);
