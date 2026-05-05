package com.novabank.account.mapper;

import org.springframework.stereotype.Component;

import com.novabank.account.dto.MovementDTO;
import com.novabank.account.model.Movement;

public class MovementMapper {

    public static MovementDTO toDto(Movement movement) {
        if (movement == null) {
            return null;
        }

        return new MovementDTO(
                movement.getId(),
                movement.getAccount().getIban(),
                movement.getType().name(),
                movement.getAmount(),
                movement.getCreatedAt()
        );
    }
}
