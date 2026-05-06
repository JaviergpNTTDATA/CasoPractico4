package com.novabank.operation.service;

import com.novabank.operation.client.AccountServiceClient;
import com.novabank.operation.dto.AccountDTO;
import com.novabank.operation.dto.TransferDTO;
import com.novabank.operation.dto.TransferRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OperationServiceTest {

    @Mock
    private AccountServiceClient accountServiceClient;

    @InjectMocks
    private OperationService operationService;

    @Test
    void transfer_withEnoughBalance_shouldReturnTransferDto() {
        TransferRequest request = new TransferRequest(
                "ESFROM",
                "ESTO",
                BigDecimal.TEN
        );

        AccountDTO source = new AccountDTO(
                1L,
                "ESFROM",
                BigDecimal.valueOf(100)
        );

        AccountDTO target = new AccountDTO(
                2L,
                "ESTO",
                BigDecimal.ZERO
        );

        when(accountServiceClient.getAccountByIban("ESFROM")).thenReturn(source);
        when(accountServiceClient.getAccountByIban("ESTO")).thenReturn(target);

        TransferDTO result = operationService.transfer(request);

        assertNotNull(result);
        assertEquals("ESFROM", result.sourceIban());
        assertEquals("ESTO", result.targetIban());
        assertEquals(0, BigDecimal.TEN.compareTo(result.amount()));

        verify(accountServiceClient).getAccountByIban("ESFROM");
        verify(accountServiceClient).getAccountByIban("ESTO");
        verify(accountServiceClient, never()).transferWithdraw(anyString(), any());
        verify(accountServiceClient, never()).transferDeposit(anyString(), any());
    }
}
