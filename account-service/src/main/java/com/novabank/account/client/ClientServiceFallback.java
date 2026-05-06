package com.novabank.account.client;

import org.springframework.stereotype.Component;

import com.novabank.account.dto.ClientDTO;

@Component
public class ClientServiceFallback implements ClientServiceClient {

    @Override
    public ClientDTO getClientById(Long id) {
        ClientDTO dto = new ClientDTO();
        dto.setId(id);
        dto.setFirstName("Client Unavailable");
        dto.setLastName("");
        dto.setDni("");
        dto.setEmail("");
        dto.setPhone("");
        dto.setAccountCount(0);
        return dto;
    }
}
