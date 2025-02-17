@Query(value = """
                SELECT t, o
                FROM Transaction t
                INNER JOIN Order o
                ON t.orderRefNumber = o.orderRefNumber
                WHERE t.atrnNum = :atrnNumber
                AND DBMS_LOB.SUBSTR(t.pushResponse, 4000) LIKE %:pushStatus%
            """)
Optional<List<Object[]>> fetchTransactionAndOrderDetail(@Param("atrnNumber") String atrnNumber, @Param("pushStatus") String pushStatus);@Query(value = """
                SELECT t, o
                FROM Transaction t
                INNER JOIN Order o
                ON t.orderRefNumber = o.orderRefNumber
                WHERE t.atrnNum = :atrnNumber
                AND DBMS_LOB.SUBSTR(t.pushResponse, 4000) LIKE %:pushStatus%
            """)
Optional<List<Object[]>> fetchTransactionAndOrderDetail(@Param("atrnNumber") String atrnNumber, @Param("pushStatus") String pushStatus);
