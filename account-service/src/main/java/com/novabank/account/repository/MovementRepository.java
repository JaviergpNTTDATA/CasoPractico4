package com.novabank.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.novabank.account.model.Movement;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {

    List<Movement> findByAccount_ibanOrderByCreatedAtDesc(String iban);

    List<Movement> findByAccount_ibanAndCreatedAtBetweenOrderByCreatedAtDesc(String iban, LocalDateTime start, LocalDateTime end
    );

}