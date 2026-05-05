package com.novabank.account.dto;

import java.math.BigDecimal;

public record AccountBalanceDTO(
        String iban,
        BigDecimal balance
) { }
