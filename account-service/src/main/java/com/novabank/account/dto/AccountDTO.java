package com.novabank.account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class AccountDTO {

    private String iban;
    private BigDecimal balance;
    private LocalDateTime createdAt;
    private Long clientId;

    public AccountDTO() {

    }
}
