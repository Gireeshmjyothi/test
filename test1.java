 @Query("SELECT new com.epay.transaction.dto.TokenAndOrderDto(t.merchantId, o.sbiOrderRefNumber, o.status) " +
            "FROM Token t " +
            "JOIN Order o " +
            "ON t.order_hash = o.order_hash" +
            "WHERE t.tokenType = :tokenType " +
            "AND t.tokenExpiryTime < :currentDate " +
            "AND (o.status != 'PAID' OR o.status IS NULL)")
    List<TokenAndOrderDto> findTokensByTypeExpiryAndOrderStatus(@Param("tokenType") String tokenType,
                                                                @Param("currentDate") Long currentDate);

Caused by: org.hibernate.query.SyntaxException: At 1:160 and token 't', mismatched input 't', expecting one of the following tokens: <EOF>, ',', '.', '[', '+', '-', '*', '/', '%', '||', AND, BY, CROSS, DAY, EPOCH, FULL, GROUP, HOUR, INNER, JOIN, LEFT, MINUTE, MONTH, NANOSECOND, OR, ORDER, OUTER, QUARTER, RIGHT, SECOND, WEEK, WHERE, YEAR [SELECT new com.epay.transaction.dto.TokenAndOrderDto(t.merchantId, o.sbiOrderRefNumber, o.status) FROM Token t JOIN Order o ON t.order_hash = o.order_hashWHERE t.tokenType = :tokenType AND t.tokenExpiryTime < :currentDate AND (o.status != 'PAID' OR o.status IS NULL)]
