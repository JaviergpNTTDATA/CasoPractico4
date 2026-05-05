package com.novabank.account.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.novabank.account.dto.AccountDTO;
import com.novabank.account.service.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Accounts", description = "Operations related to accounts")
@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService service;

    @Operation(
            summary = "Create account",
            description = "Creates a new account for a given client"
    )
    @ApiResponse(responseCode = "200", description = "Account created successfully")
    @ApiResponse(responseCode = "404", description = "Client not found")
    @PostMapping("/create/{clientId}")
    public AccountDTO create(@PathVariable Long clientId) {
        return service.createAccount(clientId);
    }

    @Operation(
            summary = "List accounts by client",
            description = "Returns all accounts associated with a client"
    )
    @ApiResponse(responseCode = "200", description = "Accounts found")
    @ApiResponse(responseCode = "404", description = "Client not found")
    @GetMapping("/client/{clientId}")
    public List<AccountDTO> listByClient(@PathVariable Long clientId) {
        return service.listClientAccounts(clientId);
    }

    @Operation(
            summary = "Get account by IBAN",
            description = "Returns an account by its IBAN"
    )
    @ApiResponse(responseCode = "200", description = "Account found")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @GetMapping("/iban/{iban}")
    public AccountDTO getByIban(@PathVariable String iban) {
        return service.getAccountByIban(iban.toUpperCase());
    }
}
