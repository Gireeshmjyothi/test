 @Query(value = "SELECT new com.epay.transaction.dto.TokenAndOrderDto(t.merchantId, o.sbiOrderRefNumber, o.status), FROM token t " +
            "JOIN orders o ON t.order_hash = o.order_hash " +
            "WHERE t.token_type = :tokenType " +
            "AND t.token_expiry_time < :currentDate " +
            "AND (o.status != 'PAID' OR o.status IS NULL)", nativeQuery = true)
    List<TokenAndOrderDto> findTokensByTypeExpiryAndOrderStatus(@Param("tokenType") String tokenType,
                                                                @Param("currentDate") Long currentDate);
