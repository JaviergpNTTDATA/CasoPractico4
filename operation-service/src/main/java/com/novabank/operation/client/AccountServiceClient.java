package com.novabank.operation.client;

import com.novabank.operation.dto.AccountDTO;
import com.novabank.operation.dto.MovementDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@FeignClient(name = "ACCOUNT-SERVICE")
public interface AccountServiceClient {

    @GetMapping("/accounts/iban/{iban}")
    AccountDTO getAccountByIban(@PathVariable("iban") String iban);

    @PostMapping("/accounts/{iban}/deposit")
    MovementDTO deposit(@PathVariable("iban") String iban,
                        @RequestParam("amount") BigDecimal amount);

    @PostMapping("/accounts/{iban}/withdraw")
    MovementDTO withdraw(@PathVariable("iban") String iban,
                         @RequestParam("amount") BigDecimal amount);

    @PostMapping("/accounts/{iban}/transfer-withdraw")
    MovementDTO transferWithdraw(@PathVariable("iban") String iban,
                                 @RequestParam("amount") BigDecimal amount);

    @PostMapping("/accounts/{iban}/transfer-deposit")
    MovementDTO transferDeposit(@PathVariable("iban") String iban,
                                @RequestParam("amount") BigDecimal amount);
}
