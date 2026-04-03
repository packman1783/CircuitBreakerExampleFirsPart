package com.example.CircuitBreakerExample.controller;

import com.example.CircuitBreakerExample.dto.ExternalResponse;
import com.example.CircuitBreakerExample.service.ExternalServiceClient;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final ExternalServiceClient client;

    @GetMapping
    public ExternalResponse test() {
        return client.callExternalService();
    }
}
