package com.example.dao;

import com.example.dto.ErrorLogDto;
import com.example.entity.ErrorLog;
import com.example.enums.EntityType;
import com.example.enums.FailureReason;
import com.example.enums.PayMode;
import com.example.repository.ErrorLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class ErrorLogDaoTest {

    @Mock
    private ErrorLogRepository errorLogRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ErrorLogDao errorLogDao;

    ErrorLogDaoTest() {
        MockitoAnnotations.openMocks(this);
    }

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

        when(objectMapper.convertValue(errorLogDto, ErrorLog.class)).thenReturn(errorLog);

        // Act
        errorLogDao.saveErrorLog(errorLogDto);

        // Assert
        verify(objectMapper, times(1)).convertValue(errorLogDto, ErrorLog.class);
        verify(errorLogRepository, times(1)).save(errorLog);
    }
}
