package com.novabank.account.service;

import com.novabank.account.client.ClientServiceClient;
import com.novabank.account.dto.AccountDTO;
import com.novabank.account.exception.AccountNotFoundException;
import com.novabank.account.exception.ClientNotFoundException;
import com.novabank.account.mapper.AccountMapper;
import com.novabank.account.model.Account;
import com.novabank.account.repository.AccountRepository;
import com.novabank.account.repository.IbanGenerator;

import feign.FeignException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final ClientServiceClient clientServiceClient;
    private final IbanGenerator ibanGenerator;

    public AccountService(AccountRepository accountRepository, ClientServiceClient clientServiceClient,
            IbanGenerator ibanGenerator) {
        this.accountRepository = accountRepository;
        this.clientServiceClient = clientServiceClient;
        this.ibanGenerator = ibanGenerator;
     
    }

    @Transactional
    public AccountDTO createAccount(Long clientId) {
        try {   // We need to use try because our AccountDTO doesnt use optional and if the
        // client is not found it will throw an exceptions
            clientServiceClient.getClientById(clientId);
        } catch (FeignException.NotFound ex) {
            throw new ClientNotFoundException("Client not found with ID: " + clientId);
        }

        Account account = new Account();
        account.setClientId(clientId);
        account.setIban(ibanGenerator.generateIban());

        Account saved = accountRepository.save(account);
        return AccountMapper.toDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<AccountDTO> listClientAccounts(Long clientId) {
        try {
            clientServiceClient.getClientById(clientId);
        } catch (FeignException.NotFound ex) {
            throw new ClientNotFoundException("Client not found: " + clientId);
        }

        var accounts = accountRepository.findByClientId(clientId);
        if (accounts.isEmpty()) {
            throw new AccountNotFoundException(
                    "No accounts found for client with ID: " + clientId);
        }

        return accounts.stream()
                .map(AccountMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public AccountDTO getAccountByIban(String iban) {
        Account account = accountRepository.findByIban(iban)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with IBAN: " + iban));

        return AccountMapper.toDTO(account);
    }
}
