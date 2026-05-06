package com.novabank.account.service;

import com.novabank.account.dto.AccountDTO;
import com.novabank.account.dto.ClientDTO;
import com.novabank.account.exception.ClientNotFoundException;
import com.novabank.account.model.Account;
import com.novabank.account.repository.AccountRepository;
import com.novabank.account.repository.IbanGenerator;
import com.novabank.account.repository.MovementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ClientIntegrationService clientIntegrationService;

    @Mock
    private IbanGenerator ibanGenerator;

    @Mock
    private MovementRepository movementRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    void createAccount_withExistingClient_shouldCreateAccount() {
        Long clientId = 1L;

        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(clientId);
        clientDTO.setFirstName("Juan");
        clientDTO.setLastName("Pérez");

        when(clientIntegrationService.getClient(clientId)).thenReturn(clientDTO);
        when(ibanGenerator.generateIban()).thenReturn("ES123");
        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AccountDTO result = accountService.createAccount(clientId);

        assertNotNull(result);
        assertEquals("ES123", result.getIban());
        assertEquals(clientId, result.getClientId());
        assertNotNull(result.getBalance());
        assertEquals(0, BigDecimal.ZERO.compareTo(result.getBalance()));

        verify(clientIntegrationService).getClient(clientId);
        verify(ibanGenerator).generateIban();
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void createAccount_withFallbackClient_shouldThrowClientNotFound() {
        Long clientId = 99L;

        ClientDTO fallback = new ClientDTO();
        fallback.setId(clientId);
        fallback.setFirstName("No disponible");

        when(clientIntegrationService.getClient(clientId)).thenReturn(fallback);

        assertThrows(ClientNotFoundException.class,
                () -> accountService.createAccount(clientId));

        verify(clientIntegrationService).getClient(clientId);
        verify(accountRepository, never()).save(any(Account.class));
    }
}
