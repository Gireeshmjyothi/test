@Query("SELECT new com.epay.transaction.dto.TokenAndOrderDto(t.merchantId, o.sbiOrderRefNumber, o.status) " +
            "FROM Token t " +
            "JOIN Order o " +
            "ON t.orderHash = o.orderHash " +
            "WHERE t.tokenType = :tokenType " +
            "AND t.tokenExpiryTime < :currentDate " +
            "AND (o.status != 'PAID' OR o.status IS NULL)")
    List<TokenAndOrderDto> findTokensByTypeExpiryAndOrderStatus(@Param("tokenType") TokenType tokenType,
                                                                @Param("currentDate") Long currentDate);
