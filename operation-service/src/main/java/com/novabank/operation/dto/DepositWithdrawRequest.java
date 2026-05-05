package com.novabank.operation.dto;

import java.math.BigDecimal;

public record DepositWithdrawRequest(
        String iban,
        BigDecimal amount
) {
}
