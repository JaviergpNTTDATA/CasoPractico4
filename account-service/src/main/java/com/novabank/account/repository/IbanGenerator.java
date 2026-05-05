package com.novabank.account.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

@Component
public class IbanGenerator {

    @PersistenceContext
    private EntityManager entityManager;

    public String generateIban() {
        Long secuencia = ((Number) entityManager
                .createNativeQuery("SELECT nextval('iban_seq')")
                .getSingleResult())
                .longValue();

        return "ES91210000" + String.format("%012d", secuencia);
    }
}
