package com.devsu.hackerearth.backend.account.client;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.devsu.hackerearth.backend.account.client.dto.ClientDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientHttpAsync {

    private final RestTemplate restTemplate;

    @Value("${app.ms.customer-service.url}")
    private String customerBaseUrl;

    @Async("asyncExecutor")
    public CompletableFuture<ClientDto> getClientById(Long id) {
        String url = customerBaseUrl + "/api/clients/{id}";
        ClientDto dto = restTemplate.getForObject(url, ClientDto.class, id);
        return CompletableFuture.completedFuture(dto);
    }
}