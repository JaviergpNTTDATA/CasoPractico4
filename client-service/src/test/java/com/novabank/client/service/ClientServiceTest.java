package com.novabank.client.service;

import com.novabank.client.dto.ClientDTO;
import com.novabank.client.dto.CreateClient;
import com.novabank.client.model.Client;
import com.novabank.client.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @Test
    void createClient_shouldPersistAndReturnDto() {
        CreateClient createClient = new CreateClient();
        createClient.setFirstName("Juan");
        createClient.setLastName("Pérez");
        createClient.setDni("12345678A");
        createClient.setEmail("juan@example.com");
        createClient.setPhone("600000000");

        Client client = new Client();
        client.setId(1L);
        client.setFirstName("Juan");
        client.setLastName("Pérez");
        client.setDni("12345678A");
        client.setEmail("juan@example.com");
        client.setPhone("600000000");

        when(clientRepository.existsByDni("12345678A")).thenReturn(false);
        when(clientRepository.existsByEmail("juan@example.com")).thenReturn(false);
        when(clientRepository.existsByPhone("600000000")).thenReturn(false);
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        ClientDTO dto = clientService.createClient(createClient);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Juan", dto.getFirstName());

        ArgumentCaptor<Client> captor = ArgumentCaptor.forClass(Client.class);
        verify(clientRepository).save(captor.capture());
        assertEquals("12345678A", captor.getValue().getDni());
    }

    @Test
    void getsClient_shouldReturnClient() {
        Client client = new Client();
        client.setId(1L);
        client.setFirstName("Juan");
        client.setLastName("Pérez");
        client.setDni("12345678A");
        client.setEmail("juan@example.com");
        client.setPhone("600000000");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        ClientDTO dto = clientService.getsClient(1L);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Juan", dto.getFirstName());
    }
}
