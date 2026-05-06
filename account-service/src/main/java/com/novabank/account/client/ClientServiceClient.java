package com.novabank.account.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.novabank.account.dto.ClientDTO;

@FeignClient(name = "CLIENT-SERVICE", url = "${CLIENT-SERVICE.url:}", fallback = ClientServiceFallback.class)
public interface ClientServiceClient {

    @GetMapping("/clients/getById/{id}")
    ClientDTO getClientById(@PathVariable("id") Long id);
}
