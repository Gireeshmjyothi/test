List<Token> findByTokenTypeAndTokenExpiryTimeLessThan(TokenType tokenType, Long currentDate);

@Query("SELECT t FROM Token t JOIN Order o ON t.mid = o.mid " +
       "WHERE t.tokenType = :tokenType " +
       "AND t.tokenExpiryTime < :currentDate " +
       "AND (o.orderStatus != 'PAID' OR o.orderStatus IS NULL)")
List<Token> findTokensByTypeExpiryAndOrderStatus(@Param("tokenType") TokenType tokenType,
                                                 @Param("currentDate") Long currentDate);
