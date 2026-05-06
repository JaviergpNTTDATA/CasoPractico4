package com.novabank.account.controller;

import com.novabank.account.dto.AccountDTO;
import com.novabank.account.service.AccountService;
import com.novabank.account.service.InquiryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private InquiryService inquiryService;

    @Test
    void listByClient_shouldReturnAccounts() throws Exception {
        AccountDTO dto = new AccountDTO();
        dto.setClientId(1L);
        dto.setIban("ES123");
        dto.setBalance(BigDecimal.ZERO);
        dto.setCreatedAt(LocalDateTime.now());

        when(accountService.listClientAccounts(1L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/accounts/client/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].iban").value("ES123"))
                .andExpect(jsonPath("$[0].clientId").value(1))
                .andExpect(jsonPath("$[0].balance").value(0));
    }
}
