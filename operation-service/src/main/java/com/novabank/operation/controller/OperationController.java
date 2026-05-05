package com.novabank.operation.controller;

import com.novabank.operation.dto.DepositWithdrawDTO;
import com.novabank.operation.dto.DepositWithdrawRequest;
import com.novabank.operation.dto.TransferDTO;
import com.novabank.operation.dto.TransferRequest;
import com.novabank.operation.service.OperationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Tag(name = "Operations", description = "Deposit, withdraw and transfer operations")
@RestController
@RequestMapping("/operations")
@RequiredArgsConstructor
public class OperationController {

    private final OperationService service;

    @Operation(
            summary = "Deposit money",
            description = "Deposits a specified amount into an account"
    )
    @ApiResponse(responseCode = "200", description = "Deposit completed successfully")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @PostMapping("/deposit")
    public DepositWithdrawDTO deposit(@RequestParam String iban,
                                      @RequestParam BigDecimal amount) {

        DepositWithdrawRequest request = new DepositWithdrawRequest(
                iban.toUpperCase(),
                amount
        );

        return service.deposit(request);
    }

    @Operation(
            summary = "Withdraw money",
            description = "Withdraws a specified amount from an account"
    )
    @ApiResponse(responseCode = "200", description = "Withdrawal completed successfully")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @PostMapping("/withdraw")
    public DepositWithdrawDTO withdraw(@RequestParam String iban,
                                       @RequestParam BigDecimal amount) {

        DepositWithdrawRequest request = new DepositWithdrawRequest(
                iban.toUpperCase(),
                amount
        );

        return service.withdraw(request);
    }

    @Operation(
            summary = "Transfer money between accounts",
            description = "Transfers a specified amount from one account to another"
    )
    @ApiResponse(responseCode = "200", description = "Transfer completed successfully")
    @ApiResponse(responseCode = "404", description = "One or both accounts not found")
    @PostMapping("/transfer")
    public TransferDTO transfer(@RequestParam String sourceIban,
                                @RequestParam String targetIban,
                                @RequestParam BigDecimal amount) {

        TransferRequest request = new TransferRequest(
                sourceIban.toUpperCase(),
                targetIban.toUpperCase(),
                amount
        );

        return service.transfer(request);
    }
}
