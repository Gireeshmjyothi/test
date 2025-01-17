List<Token> findByTokenTypeAndTokenExpiryTimeLessThan(TokenType tokenType, Long currentDate);

@Query("SELECT t FROM Token t JOIN Order o ON t.mid = o.mid " +
       "WHERE t.tokenType = :tokenType " +
       "AND t.tokenExpiryTime < :currentDate " +
       "AND (o.orderStatus != 'PAID' OR o.orderStatus IS NULL)")
List<Token> findTokensByTypeExpiryAndOrderStatus(@Param("tokenType") TokenType tokenType,
                                                 @Param("currentDate") Long currentDate);
@Query(value = "SELECT t.* FROM token t " +
            "JOIN orders o ON t.order_hash = o.order_hash " +
            "WHERE t.token_type = :tokenType " +
            "AND t.token_expiry_time < :currentDate " +
            "AND (o.status != 'PAID' OR o.status IS NULL)",
            nativeQuery = true)
    List<Token> findTokensByTypeExpiryAndOrderStatus(@Param("tokenType") TokenType tokenType,
                                                     @Param("currentDate") Long currentDate);

Caused by: org.hibernate.exception.SQLGrammarException: JDBC exception executing SQL [SELECT t.* FROM token t JOIN orders o ON t.order_hash = o.order_hash WHERE t.token_type = ? AND t.token_expiry_time < ? AND (o.status != 'PAID' OR o.status IS NULL)] [ORA-01722: invalid number

    SELECT t.* FROM token t 
JOIN orders o ON t.order_hash = o.order_hash 
WHERE t.token_type = :tokenType 
AND t.token_expiry_time < :currentDate;   
