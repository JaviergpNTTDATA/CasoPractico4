package com.novabank.account.controller;

import com.novabank.account.dto.AccountBalanceDTO;
import com.novabank.account.dto.AccountDTO;
import com.novabank.account.dto.MovementDTO;
import com.novabank.account.service.AccountService;
import com.novabank.account.service.InquiryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Tag(name = "Accounts", description = "Operations related to accounts")
@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService service;
    private final InquiryService inquiryService;

    @Operation(summary = "Create account", description = "Creates a new account for a given client")
    @ApiResponse(responseCode = "200", description = "Account created successfully")
    @ApiResponse(responseCode = "404", description = "Client not found")
    @PostMapping("/create/{clientId}")
    public AccountDTO create(@PathVariable Long clientId) {
        return service.createAccount(clientId);
    }

    @Operation(summary = "List accounts by client", description = "Returns all accounts associated with a client")
    @ApiResponse(responseCode = "200", description = "Accounts found")
    @ApiResponse(responseCode = "404", description = "Client not found")
    @GetMapping("/client/{clientId}")
    public List<AccountDTO> listByClient(@PathVariable Long clientId) {
        return service.listClientAccounts(clientId);
    }

    @Operation(summary = "Get account by IBAN", description = "Returns an account by its IBAN")
    @ApiResponse(responseCode = "200", description = "Account found")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @GetMapping("/iban/{iban}")
    public AccountDTO getByIban(@PathVariable String iban) {
        return service.getAccountByIban(iban.toUpperCase());
    }

    @PostMapping("/{iban}/deposit")
    public MovementDTO deposit(@PathVariable String iban,
            @RequestParam BigDecimal amount) {
        return service.deposit(iban.toUpperCase(), amount);
    }

    @PostMapping("/{iban}/withdraw")
    public MovementDTO withdraw(@PathVariable String iban,
            @RequestParam BigDecimal amount) {
        return service.withdraw(iban.toUpperCase(), amount);
    }

    @PostMapping("/{iban}/transfer-withdraw")
    public MovementDTO transferWithdraw(@PathVariable String iban,
            @RequestParam BigDecimal amount) {
        return service.transferWithdraw(iban.toUpperCase(), amount);
    }

    @PostMapping("/{iban}/transfer-deposit")
    public MovementDTO transferDeposit(@PathVariable String iban,
            @RequestParam BigDecimal amount) {
        return service.transferDeposit(iban.toUpperCase(), amount);
    }

    @Operation(summary = "Get movements of an account", description = "Returns all movements of an account ordered by date (desc)")
    @ApiResponse(responseCode = "200", description = "Movements found")
    @ApiResponse(responseCode = "404", description = "Account or movements not found")
    @GetMapping("/{iban}/movements")
    public List<MovementDTO> getMovementsByAccount(
            @PathVariable String iban) {
        return inquiryService.getByAccount(iban.toUpperCase());
    }

    @Operation(summary = "Get movements of an account between dates", description = "Returns movements of an account between two dates (inclusive), ordered by date (desc)")
    @ApiResponse(responseCode = "200", description = "Movements found")
    @ApiResponse(responseCode = "404", description = "Account or movements not found")
    @GetMapping("/{iban}/movements/by-date")
    public List<MovementDTO> getMovementsByAccountAndDates(
            @PathVariable String iban,
            @Parameter(description = "Start date (yyyy-MM-dd)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @Parameter(description = "End date (yyyy-MM-dd)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return inquiryService.getByAccountAndDates(iban.toUpperCase(), start, end);
    }

    @Operation(summary = "Get account balance by IBAN", description = "Returns the current balance of an account by its IBAN")
    @ApiResponse(responseCode = "200", description = "Balance retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @GetMapping("/iban/{iban}/balance")
    public AccountBalanceDTO getBalanceByIban(@PathVariable String iban) {
        return service.getBalanceByIban(iban.toUpperCase());
    }

}
