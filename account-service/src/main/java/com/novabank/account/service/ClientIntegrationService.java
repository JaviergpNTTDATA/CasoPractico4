package com.novabank.account.service;

import com.novabank.account.client.ClientServiceClient;
import com.novabank.account.dto.ClientDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ClientIntegrationService {

    private final ClientServiceClient clientServiceClient;

    public ClientIntegrationService(ClientServiceClient clientServiceClient) {
        this.clientServiceClient = clientServiceClient;
    }

    @Retry(name = "clienteService", fallbackMethod = "getClientFallback")
    @CircuitBreaker(name = "clienteService", fallbackMethod = "getClientFallback")
    public ClientDTO getClient(Long id) {
        return clientServiceClient.getClientById(id);
    }

    public ClientDTO getClientFallback(Long id, Throwable ex) {
        log.warn("Fallback activado para cliente {}: {}", id, ex.getMessage());
        ClientDTO dto = new ClientDTO();
        dto.setId(id);
        dto.setFirstName("No disponible");
        dto.setLastName("");
        dto.setDni("");
        dto.setEmail("");
        dto.setPhone("");
        return dto;
    }
}
