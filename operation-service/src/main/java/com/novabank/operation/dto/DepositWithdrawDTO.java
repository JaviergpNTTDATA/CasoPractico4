package com.novabank.operation.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DepositWithdrawDTO(
        String type,
        String iban,
        BigDecimal amount,
        LocalDateTime timestamp
) {
}
