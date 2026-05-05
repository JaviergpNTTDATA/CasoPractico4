package com.novabank.operation.dto;

import java.math.BigDecimal;

public record AccountDTO(
        Long id,
        String iban,
        BigDecimal balance
) {
}
