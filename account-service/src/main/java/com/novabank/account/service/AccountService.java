package com.novabank.account.service;

import com.novabank.account.dto.AccountBalanceDTO;
import com.novabank.account.dto.AccountDTO;
import com.novabank.account.dto.MovementDTO;
import com.novabank.account.dto.ClientDTO;
import com.novabank.account.exception.AccountNotFoundException;
import com.novabank.account.exception.ClientNotFoundException;
import com.novabank.account.exception.InsufficientBalanceException;
import com.novabank.account.mapper.AccountMapper;
import com.novabank.account.mapper.MovementMapper;
import com.novabank.account.model.Account;
import com.novabank.account.model.Movement;
import com.novabank.account.model.MovementType;
import com.novabank.account.repository.AccountRepository;
import com.novabank.account.repository.IbanGenerator;
import com.novabank.account.repository.MovementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final ClientIntegrationService clientIntegrationService;
    private final IbanGenerator ibanGenerator;
    private final MovementRepository movementRepository;

    public AccountService(AccountRepository accountRepository,
                          ClientIntegrationService clientIntegrationService,
                          IbanGenerator ibanGenerator,
                          MovementRepository movementRepository) {
        this.accountRepository = accountRepository;
        this.clientIntegrationService = clientIntegrationService;
        this.ibanGenerator = ibanGenerator;
        this.movementRepository = movementRepository;
    }

    @Transactional
    public AccountDTO createAccount(Long clientId) {
        // Llamada resiliente al servicio de clientes
        ClientDTO client = clientIntegrationService.getClient(clientId);

        // Aquí puedes decidir qué hacer si vino del fallback
        // Por ejemplo, si el nombre es "No disponible", lanzar una excepción controlada:
        if ("No disponible".equals(client.getFirstName())) {
            throw new ClientNotFoundException(
                "Client service not available or client not found: " + clientId
            );
        }

        Account account = new Account();
        account.setClientId(clientId);
        account.setIban(ibanGenerator.generateIban());

        Account saved = accountRepository.save(account);
        return AccountMapper.toDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<AccountDTO> listClientAccounts(Long clientId) {
        // Verificamos el cliente a través del servicio resiliente
        ClientDTO client = clientIntegrationService.getClient(clientId);
        if ("No disponible".equals(client.getFirstName())) {
            throw new ClientNotFoundException(
                "Client service not available or client not found: " + clientId
            );
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

    @Transactional
    public MovementDTO deposit(String iban, BigDecimal amount) {
        validateAmount(amount);

        Account account = accountRepository.findByIban(iban)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with IBAN: " + iban));

        account.setBalance(account.getBalance().add(amount));

        Movement movement = Movement.builder()
                .account(account)
                .type(MovementType.DEPOSIT)
                .amount(amount)
                .createdAt(LocalDateTime.now())
                .build();

        Movement saved = movementRepository.save(movement);

        return MovementMapper.toDto(saved);
    }

    @Transactional
    public MovementDTO withdraw(String iban, BigDecimal amount) {
        validateAmount(amount);

        Account account = accountRepository.findByIban(iban)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with IBAN: " + iban));

        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(amount));

        Movement movement = Movement.builder()
                .account(account)
                .type(MovementType.WITHDRAW)
                .amount(amount)
                .createdAt(LocalDateTime.now())
                .build();

        Movement saved = movementRepository.save(movement);

        return MovementMapper.toDto(saved);
    }

    @Transactional
    public MovementDTO transferWithdraw(String iban, BigDecimal amount) {
        validateAmount(amount);

        Account account = accountRepository.findByIban(iban)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with IBAN: " + iban));

        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(amount));

        Movement movement = Movement.builder()
                .account(account)
                .type(MovementType.OUTGOING_TRANSFER)
                .amount(amount)
                .createdAt(LocalDateTime.now())
                .build();

        Movement saved = movementRepository.save(movement);

        return MovementMapper.toDto(saved);
    }

    @Transactional
    public MovementDTO transferDeposit(String iban, BigDecimal amount) {
        validateAmount(amount);

        Account account = accountRepository.findByIban(iban)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with IBAN: " + iban));

        account.setBalance(account.getBalance().add(amount));

        Movement movement = Movement.builder()
                .account(account)
                .type(MovementType.INCOMING_TRANSFER)
                .amount(amount)
                .createdAt(LocalDateTime.now())
                .build();

        Movement saved = movementRepository.save(movement);

        return MovementMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public AccountBalanceDTO getBalanceByIban(String iban) {
        Account account = accountRepository.findByIban(iban)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with IBAN: " + iban));

        return new AccountBalanceDTO(account.getIban(), account.getBalance());
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }
}
