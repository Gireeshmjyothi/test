List<Token> findByTokenTypeAndTokenExpiryTimeLessThan(TokenType tokenType, Long currentDate);

@Query("SELECT t FROM Token t JOIN Order o ON t.mid = o.mid " +
       "WHERE t.tokenType = :tokenType " +
       "AND t.tokenExpiryTime < :currentDate " +
       "AND (o.orderStatus != 'PAID' OR o.orderStatus IS NULL)")
List<Token> findTokensByTypeExpiryAndOrderStatus(@Param("tokenType") TokenType tokenType,
                                                 @Param("currentDate") Long currentDate);
@Query(value = "SELECT t.* FROM token t " +
               "JOIN orders o ON t.mid = o.mid " +
               "WHERE t.token_type = :tokenType " +
               "AND t.token_expiry_time < :currentDate " +
               "AND (o.order_status != 'PAID' OR o.order_status IS NULL)", 
       nativeQuery = true)
List<Token> findTokensByTypeExpiryAndOrderStatus(@Param("tokenType") TokenType tokenType,
                                                 @Param("currentDate") Long currentDate);
