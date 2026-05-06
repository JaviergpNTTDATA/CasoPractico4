package com.novabank.account.repository;

import com.novabank.account.model.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void saveAndFindByClientId_shouldWork() {
        Account acc = new Account();
        acc.setClientId(1L);
        acc.setIban("ES123");
        acc.setBalance(BigDecimal.ZERO);

        accountRepository.save(acc);

        List<Account> accounts = accountRepository.findByClientId(1L);
        assertFalse(accounts.isEmpty());
        assertEquals("ES123", accounts.get(0).getIban());
    }

    @Test
    void saveAndFindByIban_shouldWork() {
        Account acc = new Account();
        acc.setClientId(2L);
        acc.setIban("ES999");
        acc.setBalance(BigDecimal.TEN);

        accountRepository.save(acc);

        Optional<Account> found = accountRepository.findByIban("ES999");
        assertTrue(found.isPresent());
        assertEquals(2L, found.get().getClientId());
    }
}
