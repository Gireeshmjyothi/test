@Test
void testSaveErrorLog() {
    // Arrange
    ErrorLogDto errorLogDto = ErrorLogDto.builder()
            .mID("MID123")
            .orderRefNumber("ORDER123")
            .sbiOrderRefNumber("SBI123")
            .atrn("ATRN123")
            .entityType(EntityType.USER)
            .payMode(PayMode.CREDIT_CARD)
            .failureReason(FailureReason.NETWORK_ISSUE)
            .errorCode("ERR001")
            .errorMessage("Network timeout")
            .build();

    ErrorLog errorLog = ErrorLog.builder()
            .mID("MID123")
            .orderRefNumber("ORDER123")
            .sbiOrderRefNumber("SBI123")
            .atrn("ATRN123")
            .entityType(EntityType.USER)
            .payMode(PayMode.CREDIT_CARD)
            .failureReason(FailureReason.NETWORK_ISSUE)
            .errorCode("ERR001")
            .errorMessage("Network timeout")
            .build();

    when(objectMapper.convertValue(any(ErrorLogDto.class), eq(ErrorLog.class))).thenReturn(errorLog);

    // Act
    errorLogDao.saveErrorLog(errorLogDto);

    // Assert
    verify(objectMapper, times(1)).convertValue(any(ErrorLogDto.class), eq(ErrorLog.class));
    verify(errorLogRepository, times(1)).save(any(ErrorLog.class));
}
