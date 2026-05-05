package com.novabank.operation.service;

import com.novabank.operation.client.AccountServiceClient;
import com.novabank.operation.dto.DepositWithdrawDTO;
import com.novabank.operation.dto.DepositWithdrawRequest;
import com.novabank.operation.dto.TransferDTO;
import com.novabank.operation.dto.TransferRequest;
import com.novabank.operation.dto.AccountDTO;
import com.novabank.operation.dto.MovementDTO;
import com.novabank.operation.exception.AccountNotFoundException;
import com.novabank.operation.exception.InsufficientBalanceException;
import feign.FeignException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OperationService {

    private final AccountServiceClient accountServiceClient;

    public OperationService(AccountServiceClient accountServiceClient) {
        this.accountServiceClient = accountServiceClient;
    }

    public DepositWithdrawDTO deposit(DepositWithdrawRequest request) {
        validateAmount(request.amount());

        getAccountOrThrow(request.iban());

        MovementDTO movement = accountServiceClient.deposit(
                request.iban(),
                request.amount());

        return new DepositWithdrawDTO(
                "DEPOSIT",
                request.iban(),
                request.amount(),
                movement.timestamp());
    }

    public DepositWithdrawDTO withdraw(DepositWithdrawRequest request) {
        validateAmount(request.amount());

        AccountDTO account = getAccountOrThrow(request.iban());

        ensureSufficientBalance(account.balance(), request.amount());

        MovementDTO movement = accountServiceClient.withdraw(
                request.iban(),
                request.amount());


        return new DepositWithdrawDTO(
                "WITHDRAW",
                request.iban(),
                request.amount(),
                movement.timestamp());
    }

    public TransferDTO transfer(TransferRequest request) {
        validateAmount(request.amount());

        if (request.sourceIban().equalsIgnoreCase(request.targetIban())) {
            throw new IllegalArgumentException("Source and target accounts must be different");
        }

        AccountDTO source = getAccountOrThrow(request.sourceIban());
        getAccountOrThrow(request.targetIban());

        ensureSufficientBalance(source.balance(), request.amount());

        return new TransferDTO(
                request.sourceIban(),
                request.targetIban(),
                request.amount());
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }

    private void ensureSufficientBalance(BigDecimal currentBalance, BigDecimal amount) {
        if (currentBalance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }
    }

    private AccountDTO getAccountOrThrow(String iban) {
        try {
            return accountServiceClient.getAccountByIban(iban);
        } catch (FeignException.NotFound ex) {
            throw new AccountNotFoundException("Account not found with IBAN: " + iban);
        }
    }
}
