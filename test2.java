@Test
void testInvalidateToken_Success() {
    // Arrange
    when(authentication.getCredentials()).thenReturn("mock-token");
    tokenValidator.validateEmptyToken("mock-token");

    mockPrincipal = new EPayPrincipal("mock-mid", "mock-token");
    when(EPayIdentityUtil.getUserPrincipal()).thenReturn(mockPrincipal);

    TokenDto mockTokenDto = new TokenDto();
    when(tokenDao.getActiveTokenByMID("mock-mid", "mock-token", TokenStatus.ACTIVE))
            .thenReturn(Optional.of(mockTokenDto));

    when(tokenDao.saveToken(any(TokenDto.class))).thenReturn(mockTokenDto); // Mock saveToken behavior

    // Act
    TransactionResponse<String> response = tokenService.invalidateToken();

    // Assert
    assertNotNull(response);
    assertEquals(1, response.getStatus());
    assertEquals(List.of("Token invalidated successfully"), response.getData());

    // Verify interactions
    verify(tokenValidator).validateEmptyToken("mock-token");
    verify(tokenDao).getActiveTokenByMID("mock-mid", "mock-token", TokenStatus.ACTIVE);
    verify(tokenDao).saveToken(mockTokenDto); // Ensure saveToken is invoked
}

@Query(value = """ SELECT t.*, -- Select all columns from Transaction table o.ORDER_AMOUNT, o.CUSTOMER_ID, o.CURRENCY_CODE FROM Transaction t LEFT JOIN Orders o ON t.ORDER_REF_NUM = o.ORDER_REF_NUMBER WHERE t.ATRN_NUM = :atrnNum AND (:orderRefNum IS NULL OR t.ORDER_REF_NUM = :orderRefNum) AND (:sbiOrderRefNum IS NULL OR t.SBI_ORDER_REF_NUM = :sbiOrderRefNum) AND (:orderAmount IS NULL OR o.ORDER_AMOUNT = :orderAmount) """, nativeQuery = true) List<Object[]> findTransactionWithOrderDetails( @Param("atrnNum") String atrnNum, @Param("orderRefNum") String orderRefNum, @Param("sbiOrderRefNum") String sbiOrderRefNum, @Param("orderAmount") BigDecimal orderAmount );



@Repository
public interface TransactionOrderRepository extends JpaRepository<Transaction, Long> {

    @Query(value = """
        SELECT t, o
        FROM Transaction t 
        LEFT JOIN Orders o 
        ON t.orderRefNum = o.orderRefNumber
        WHERE t.atrnNum = :atrnNum 
          AND (:orderRefNum IS NULL OR t.orderRefNum = :orderRefNum) 
          AND (:sbiOrderRefNum IS NULL OR t.sbiOrderRefNum = :sbiOrderRefNum) 
          AND (:orderAmount IS NULL OR o.orderAmount = :orderAmount)
        """)
    List<Object[]> fetchTransactionAndOrderData(
            @Param("atrnNum") String atrnNum,
            @Param("orderRefNum") String orderRefNum,
            @Param("sbiOrderRefNum") String sbiOrderRefNum,
            @Param("orderAmount") BigDecimal orderAmount);
}



        @Query(value = """
    SELECT t, o
    FROM Transaction t 
    INNER JOIN Orders o 
    ON t.orderRefNum = o.orderRefNumber
    WHERE t.atrnNum = :atrnNum 
      AND (:orderRefNum IS NULL OR t.orderRefNum = :orderRefNum) 
      AND (:sbiOrderRefNum IS NULL OR t.sbiOrderRefNum = :sbiOrderRefNum) 
      AND (:orderAmount IS NULL OR o.orderAmount = :orderAmount)
    """)
List<Object[]> fetchTransactionAndOrderData(
        @Param("atrnNum") String atrnNum,
        @Param("orderRefNum") String orderRefNum,
        @Param("sbiOrderRefNum") String sbiOrderRefNum,
        @Param("orderAmount") BigDecimal orderAmount);



        @Query(value = """
    SELECT t, o
    FROM Transaction t 
    LEFT JOIN Orders o 
    ON t.orderRefNum = o.orderRefNumber
    WHERE t.atrnNum = :atrnNum
    AND (:orderRefNum IS NULL OR t.orderRefNum = :orderRefNum)
    AND (:sbiOrderRefNum IS NULL OR t.sbiOrderRefNum = :sbiOrderRefNum)
    AND (:orderAmount IS NULL OR o.orderAmount = :orderAmount)
    """)
List<Object[]> fetchTransactionAndOrderData(
        @Param("atrnNum") String atrnNum,
        @Param("orderRefNum") String orderRefNum,
        @Param("sbiOrderRefNum") String sbiOrderRefNum,
        @Param("orderAmount") BigDecimal orderAmount);


      @Query(value = """
    SELECT t, o
    FROM Transaction t 
    LEFT JOIN Orders o 
    ON t.orderRefNum = o.orderRefNumber
    WHERE t.atrnNum = :atrnNum
    AND (:orderRefNum IS NULL OR t.orderRefNum = :orderRefNum)
    AND (:sbiOrderRefNum IS NULL OR t.sbiOrderRefNum = :sbiOrderRefNum)
    AND (:orderAmount IS NULL OR o.orderAmount = :orderAmount)
    """)
List<Object[]> fetchTransactionAndOrderData(
        @Param("atrnNum") String atrnNum,
        @Param("orderRefNum") String orderRefNum,
        @Param("sbiOrderRefNum") String sbiOrderRefNum,
        @Param("orderAmount") BigDecimal orderAmount);  

            @Query(value = """
    SELECT t.*, o.*
    FROM transaction t 
    LEFT JOIN orders o 
    ON t.order_ref_num = o.order_ref_number
    WHERE t.atrn_num IN :atrnNums
    AND (:orderRefNum IS NULL OR t.order_ref_num = :orderRefNum)
    AND (:sbiOrderRefNum IS NULL OR t.sbi_order_ref_num = :sbiOrderRefNum)
    AND (:orderAmount IS NULL OR o.order_amount = :orderAmount)
    """, nativeQuery = true)
List<Object[]> fetchTransactionAndOrderData(
        @Param("atrnNums") List<String> atrnNums,
        @Param("orderRefNum") String orderRefNum,
        @Param("sbiOrderRefNum") String sbiOrderRefNum,
        @Param("orderAmount") BigDecimal orderAmount);
