package com.novabank.account.client;

import com.novabank.account.dto.ClientDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import static com.github.tomakehurst.wiremock.client.WireMock.*;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureWireMock(port = 0)
@ActiveProfiles("test")
class ClientServiceClientContractTest {

    @Autowired
    private ClientServiceClient clientServiceClient;

    @Test
    void getClientById_shouldReturnClientFromRemoteService() {
        stubFor(get(urlEqualTo("/clients/1"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                            {
                              "id": 1,
                              "firstName": "Juan",
                              "lastName": "Pérez",
                              "dni": "12345678A",
                              "email": "juan@example.com",
                              "phone": "600000000"
                            }
                            """)));

        ClientDTO client = clientServiceClient.getClientById(1L);

        assertNotNull(client);
        assertEquals(1L, client.getId());
        assertEquals("string", client.getFirstName());
    }
}
