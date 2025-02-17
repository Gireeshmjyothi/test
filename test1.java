@Query(value = """
                SELECT t.*, o.*
                FROM transaction t
                INNER JOIN order o
                ON t.order_ref_number = o.order_ref_number
                WHERE t.atrn_num = :atrnNumber
                AND DBMS_LOB.SUBSTR(t.push_response, 4000) LIKE %:pushStatus%
            """, nativeQuery = true)
Optional<List<Object[]>> fetchTransactionAndOrderDetail(@Param("atrnNumber") String atrnNumber, @Param("pushStatus") String pushStatus);
