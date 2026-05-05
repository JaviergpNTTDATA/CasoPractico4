package com.novabank.operation.dto;

import java.math.BigDecimal;

public record TransferDTO(
        String sourceIban,
        String targetIban,
        BigDecimal amount
) {
}
