package com.novabank.client.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.novabank.client.dto.ClientDTO;
import com.novabank.client.service.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientController.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listClients_shouldReturnJsonArray() throws Exception {
        ClientDTO dto = ClientDTO.builder()
                .id(1L)
                .firstName("Juan")
                .lastName("Pérez")
                .dni("12345678A")
                .email("juan@example.com")
                .phone("600000000")
                .accountCount(0)
                .build();

        when(clientService.listClients()).thenReturn(List.of(dto));

        mockMvc.perform(get("/clients/getAll")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Juan"));
    }
}
