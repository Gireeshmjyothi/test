@Query(value = """
                SELECT t, o
                FROM Transaction t
                INNER JOIN Order o
                ON t.orderRefNumber = o.orderRefNumber
                WHERE t.atrnNum = :atrnNumber
                AND t.pushResponse LIKE %:pushStatus%
            """)
    Optional<List<Object[]>> fetchTransactionAndOrderDetail(@Param("atrnNumber") String atrnNumber, @Param("pushStatus") String pushStatus);
