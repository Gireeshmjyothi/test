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
