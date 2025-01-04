@ExtendWith(MockitoExtension.class)
class ErrorLogDaoTest {

    @InjectMocks
    private ErrorLogDao errorLogDao;

    @Mock
    private ErrorLogRepository errorLogRepository;

    @Mock
    private ObjectMapper objectMapper;

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
        verify(objectMapper, times(1)).convertValue(any(ErrorLogDto.class), eq(ErrorLog.class));
        verify(errorLogRepository, times(1)).save(errorLog);
    }
}
@Test
void testSaveErrorLog_NullErrorLogDto() {
    // Act & Assert
    assertThrows(NullPointerException.class, () -> errorLogDao.saveErrorLog(null));
    verifyNoInteractions(objectMapper, errorLogRepository); // Ensure no interaction occurred
}
@Test
void testSaveErrorLog_ObjectMapperThrowsException() {
    // Arrange
    ErrorLogDto errorLogDto = ErrorLogDto.builder()
            .mID("MID123")
            .build();

    when(objectMapper.convertValue(any(ErrorLogDto.class), eq(ErrorLog.class)))
            .thenThrow(new IllegalArgumentException("Invalid data"));

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> errorLogDao.saveErrorLog(errorLogDto));

    // Verify interactions
    verify(objectMapper, times(1)).convertValue(any(ErrorLogDto.class), eq(ErrorLog.class));
    verifyNoInteractions(errorLogRepository); // Save method should not be called
}
@Test
void testSaveErrorLog_RepositoryThrowsException() {
    // Arrange
    ErrorLogDto errorLogDto = ErrorLogDto.builder()
            .mID("MID123")
            .build();

    ErrorLog errorLog = ErrorLog.builder()
            .mID("MID123")
            .build();

    when(objectMapper.convertValue(any(ErrorLogDto.class), eq(ErrorLog.class))).thenReturn(errorLog);
    doThrow(new RuntimeException("Database error")).when(errorLogRepository).save(any(ErrorLog.class));

    // Act & Assert
    assertThrows(RuntimeException.class, () -> errorLogDao.saveErrorLog(errorLogDto));

    // Verify interactions
    verify(objectMapper, times(1)).convertValue(any(ErrorLogDto.class), eq(ErrorLog.class));
    verify(errorLogRepository, times(1)).save(any(ErrorLog.class));
}

@Test
void testSaveErrorLog_InvalidData() {
    // Arrange
    ErrorLogDto errorLogDto = ErrorLogDto.builder()
            .mID("") // Invalid mID (empty string)
            .build();

    ErrorLog errorLog = ErrorLog.builder()
            .mID("") // Invalid data
            .build();

    when(objectMapper.convertValue(any(ErrorLogDto.class), eq(ErrorLog.class))).thenReturn(errorLog);

    // Act
    errorLogDao.saveErrorLog(errorLogDto);

    // Assert
    verify(objectMapper, times(1)).convertValue(any(ErrorLogDto.class), eq(ErrorLog.class));
    verify(errorLogRepository, times(1)).save(any(ErrorLog.class));
}
@Test
void testSaveErrorLog_UnhandledException() {
    // Arrange
    ErrorLogDto errorLogDto = ErrorLogDto.builder()
            .mID("MID123")
            .build();

    doThrow(new RuntimeException("Unhandled exception")).when(objectMapper).convertValue(any(ErrorLogDto.class), eq(ErrorLog.class));

    // Act & Assert
    assertThrows(RuntimeException.class, () -> errorLogDao.saveErrorLog(errorLogDto));

    // Verify no save is attempted
    verifyNoInteractions(errorLogRepository);
}
                 

