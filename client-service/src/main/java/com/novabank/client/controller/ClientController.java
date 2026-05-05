package com.novabank.client.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.novabank.client.dto.ClientDTO;
import com.novabank.client.dto.CreateClient;
import com.novabank.client.service.ClientService;

import java.util.List;

@Tag(name = "Clients", description = "Operations related to clients")
@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService service;

    @PostMapping
    public ClientDTO create(@Valid @RequestBody CreateClient dto) {
        return service.createClient(dto);
    }

    @Operation(
            summary = "Search client by ID",
            description = "Returns a client based on the provided ID"
    )
    @ApiResponse(responseCode = "200", description = "Client found")
    @ApiResponse(responseCode = "404", description = "Client not found")
    @GetMapping("/getById/{id}")
    public ClientDTO searchById(@PathVariable Long id) {
        return service.getsClient(id);
    }

    @Operation(
            summary = "Search client by DNI",
            description = "Returns a client based on the provided DNI"
    )
    @ApiResponse(responseCode = "200", description = "Client found")
    @ApiResponse(responseCode = "404", description = "Client not found")
    @GetMapping("/getByDni/{dni}")
    public ClientDTO searchByDNI(@PathVariable String dni) {
        return service.getClientDNI(dni.toUpperCase());
    }

    @Operation(
            summary = "List all clients",
            description = "Returns all registered clients"
    )
    @ApiResponse(responseCode = "200", description = "Clients retrieved successfully")
    @ApiResponse(responseCode = "404", description = "No clients found")
    @GetMapping("/getAll")
    public List<ClientDTO> getAll() {
        return service.listClients();
    }
}
