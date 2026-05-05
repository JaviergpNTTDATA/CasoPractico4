package com.novabank.operation.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovementDTO(
        Long id,
        String iban,
        String type,
        BigDecimal amount,
        LocalDateTime timestamp
) {
}
