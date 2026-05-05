package com.novabank.client.mapper;

import com.novabank.client.dto.ClientDTO;
import com.novabank.client.dto.CreateClient;
import com.novabank.client.model.Client;
public class ClientMapper {
    public static ClientDTO toDTO(Client client) {
        return new ClientDTO(
                client.getId(),
                client.getFirstName(),
                client.getLastName(),
                client.getDni(),
                client.getEmail(),
                client.getPhone(),
                //client.getAccounts().size()
                0
        );
    }

    public static Client toEntity(CreateClient dto) {
        return new Client(
                dto.getFirstName(),
                dto.getLastName(),
                dto.getDni(),
                dto.getPhone(),
                dto.getEmail()
        );
    }
}