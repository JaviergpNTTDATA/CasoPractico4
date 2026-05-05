package com.novabank.client.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.novabank.client.dto.ClientDTO;
import com.novabank.client.dto.CreateClient;
import com.novabank.client.exception.ClientNotFoundException;
import com.novabank.client.mapper.ClientMapper;
import com.novabank.client.model.Client;
import com.novabank.client.repository.ClientRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Transactional(readOnly = true)
    public List<ClientDTO> listClients() {
        return clientRepository.findAll().stream()
                .map(ClientMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ClientDTO getsClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client not found: " + id));
        return ClientMapper.toDTO(client);
    }

    @Transactional(readOnly = true)
    public ClientDTO getClientDNI(String dni) {
        Client client = clientRepository.findByDni(dni)
                .orElseThrow(() -> new ClientNotFoundException("Client not found: " + dni));
        return ClientMapper.toDTO(client);
    }

    public ClientDTO createClient(CreateClient dto) {
        if (clientRepository.existsByDni(dto.getDni()))
            throw new IllegalArgumentException("A client with that DNI already exists: " +
                    dto.getDni());
        if (clientRepository.existsByEmail(dto.getEmail()))
            throw new IllegalArgumentException("A client with that email already exists: " +
                    dto.getEmail());
        if (clientRepository.existsByPhone(dto.getPhone()))
            throw new IllegalArgumentException("A client with that telephone number already exists: " + dto.getPhone());
        if (dto.getFirstName().isBlank())
            throw new IllegalArgumentException("First name is needed");
        if (dto.getLastName().isBlank())
            throw new IllegalArgumentException("Last name is needed");
        if (dto.getDni().isBlank())
            throw new IllegalArgumentException("DNI is needed");
        if (dto.getEmail().isBlank())
            throw new IllegalArgumentException("Email is needed");
        if (dto.getPhone().isBlank())
            throw new IllegalArgumentException("Phone is needed");

        Client save = clientRepository.save(ClientMapper.toEntity(dto));
        return ClientMapper.toDTO(save);
    }
}