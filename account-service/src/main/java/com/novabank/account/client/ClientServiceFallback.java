package com.novabank.account.client;

import org.springframework.stereotype.Component;

import com.novabank.account.dto.ClientDTO;

@Component
public class ClientServiceFallback implements ClientServiceClient {

    @Override
    public ClientDTO getClientById(Long id) {
        // Respuesta degradada cuando client-service no está disponible
        ClientDTO dto = new ClientDTO();
        dto.setId(id);
        dto.setFirstName("Cliente no disponible");
        dto.setLastName("");
        dto.setDni("");
        dto.setEmail("");
        dto.setPhone("");
        dto.setAccountCount(0);
        return dto;
    }
}
