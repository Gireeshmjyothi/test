 @Query(value = "SELECT new com.epay.transaction.dto.TokenAndOrderDto(t.merchantId, o.sbiOrderRefNumber, o.status), FROM token t " +
            "JOIN orders o ON t.order_hash = o.order_hash " +
            "WHERE t.token_type = :tokenType " +
            "AND t.token_expiry_time < :currentDate " +
            "AND (o.status != 'PAID' OR o.status IS NULL)", nativeQuery = true)
    List<TokenAndOrderDto> findTokensByTypeExpiryAndOrderStatus(@Param("tokenType") String tokenType,
                                                                @Param("currentDate") Long currentDate);

@Query("SELECT new com.epay.transaction.dto.TokenAndOrderDto(t.merchantId, o.sbiOrderRefNumber, o.status) " +
       "FROM Token t " +
       "JOIN t.orders o " +
       "WHERE t.tokenType = :tokenType " +
       "AND t.tokenExpiryTime < :currentDate " +
       "AND (o.status != 'PAID' OR o.status IS NULL)")
List<TokenAndOrderDto> findTokensByTypeExpiryAndOrderStatus(@Param("tokenType") String tokenType,
                                                            @Param("currentDate") Long currentDate);


@Query(value = "SELECT t.merchant_id as merchantId, o.sbi_order_ref_number as sbiOrderRefNumber, o.status as status " +
               "FROM token t " +
               "JOIN orders o ON t.order_hash = o.order_hash " +
               "WHERE t.token_type = :tokenType " +
               "AND t.token_expiry_time < :currentDate " +
               "AND (o.status != 'PAID' OR o.status IS NULL)", nativeQuery = true)
List<Object[]> findTokensByTypeExpiryAndOrderStatus(@Param("tokenType") String tokenType,
                                                    @Param("currentDate") Long currentDate);
