@Query(value = """
                SELECT t.*, o.*
                FROM transaction t
                INNER JOIN order o
                ON t.order_ref_number = o.order_ref_number
                WHERE t.atrn_num = :atrnNumber
                AND DBMS_LOB.SUBSTR(t.push_response, 4000) LIKE %:pushStatus%
            """, nativeQuery = true)
Optional<List<Object[]>> fetchTransactionAndOrderDetail(@Param("atrnNumber") String atrnNumber, @Param("pushStatus") String pushStatus);

@Modifying
@Query(value = """
                UPDATE Transaction t
                SET t.pushResponse = :pushResponse
                WHERE t.atrnNum = :atrnNum
            """, nativeQuery = true)
int updatePushResponse(@Param("atrnNum") String atrnNum, @Param("pushResponse") String pushResponse);

@Query("SELECT t, o FROM Transaction t " +
       "INNER JOIN Order o ON t.orderRefNumber = o.orderRefNumber " +
       "WHERE t.atrnNum = :atrnNumber " +
       "AND t.pushResponse LIKE CONCAT('%', :pushStatus, '%')")
List<Object[]> fetchTransactionAndOrderDetail(@Param("atrnNumber") String atrnNumber, 
                                               @Param("pushStatus") String pushStatus);
