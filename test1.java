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

    // Mock behavior
    when(objectMapper.convertValue(any(ErrorLogDto.class), eq(ErrorLog.class))).thenReturn(errorLog);

    // Act
    errorLogDao.saveErrorLog(errorLogDto);

    // Assert
    ArgumentCaptor<ErrorLog> captor = ArgumentCaptor.forClass(ErrorLog.class);
    verify(objectMapper, times(1)).convertValue(any(ErrorLogDto.class), eq(ErrorLog.class));
    verify(errorLogRepository, times(1)).save(captor.capture());

    // Verify captured value
    ErrorLog capturedErrorLog = captor.getValue();
    assertEquals(errorLog.getMID(), capturedErrorLog.getMID());
    assertEquals(errorLog.getOrderRefNumber(), capturedErrorLog.getOrderRefNumber());
    assertEquals(errorLog.getEntityType(), capturedErrorLog.getEntityType());
}


Wanted but not invoked:
objectMapper.convertValue(
    <any com.epay.transaction.dto.ErrorLogDto>,
    class com.epay.transaction.entity.ErrorLog
);
-> at com.fasterxml.jackson.databind.ObjectMapper.convertValue(ObjectMapper.java:4475)
Actually, there were zero interactions with this mock.

Wanted but not invoked:
objectMapper.convertValue(
    <any com.epay.transaction.dto.ErrorLogDto>,
    class com.epay.transaction.entity.ErrorLog
);
-> at com.fasterxml.jackson.databind.ObjectMapper.convertValue(ObjectMapper.java:4475)
Actually, there were zero interactions with this mock.

	at com.fasterxml.jackson.databind.ObjectMapper.convertValue(ObjectMapper.java:4475)
	at com.epay.transaction.dao.ErrorLogDaoTest.testSaveErrorLog(ErrorLogDaoTest.java:71)
	at java.base/java.lang.reflect.Method.invoke(Method.java:580)
	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
