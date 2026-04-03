package com.example.CircuitBreakerExample.service;

import com.example.CircuitBreakerExample.dto.ExternalResponse;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Slf4j
@Service
public class ExternalServiceClient {

    private static final String EXTERNAL_SERVICE = "externalService";
    private static final String BASE_URL = "http://localhost:8081/api/data";

    @CircuitBreaker(name = EXTERNAL_SERVICE, fallbackMethod = "fallback")
    public ExternalResponse callExternalService() {
        return callWithRestTemplate();
    }

    private ExternalResponse callWithRestTemplate() {
        RestTemplate restTemplate = createRestTemplateWithTimeout();
        return restTemplate.getForObject(BASE_URL, ExternalResponse.class);
    }

    private RestTemplate createRestTemplateWithTimeout() {
        // Настройка таймаута для RestTemplate
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(2));
        factory.setReadTimeout(Duration.ofSeconds(3));
        return new RestTemplate(factory);
    }

    public ExternalResponse fallback(Throwable t) {
        String reason = t.getClass().getSimpleName();
        String message = t.getMessage();

        if (t instanceof CallNotPermittedException) {
            log.warn("CircuitBreaker OPEN - вызов отклонен");
            return ExternalResponse.fallback("Сервис временно недоступен (circuit open)");
        }

        if (t instanceof ResourceAccessException) {
            log.warn("Connection failed: {}", message);
            return ExternalResponse.fallback("Внешний сервис недоступен: соединение не установлено");
        }

        if (t instanceof HttpClientErrorException) {
            log.warn("HTTP error: {}", message);
            return ExternalResponse.fallback("Ошибка внешнего сервиса");
        }

        log.error("Fallback triggered by {}: {}", reason, message, t);
        return ExternalResponse.fallback("Внешний сервис недоступен");
    }
}
