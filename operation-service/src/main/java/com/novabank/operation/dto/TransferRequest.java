package com.novabank.operation.dto;

import java.math.BigDecimal;

public record TransferRequest(
        String sourceIban,
        String targetIban,
        BigDecimal amount
) {
}
