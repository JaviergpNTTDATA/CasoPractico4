package com.novabank.client.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.novabank.client.model.Client;

@DataJpaTest
class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    @Test
    void saveAndFindByDni_shouldWork() {
        Client client = new Client();
        client.setFirstName("Ana");
        client.setLastName("López");
        client.setDni("87654321B");
        client.setEmail("ana@example.com");
        client.setPhone("600111111");

        clientRepository.save(client);

        Optional<Client> found = clientRepository.findByDni("87654321B");
        assertTrue(found.isPresent());
        assertEquals("Ana", found.get().getFirstName());
    }
}
