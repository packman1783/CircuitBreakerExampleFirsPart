package com.example.CircuitBreakerExample.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExternalResponse {
    private String message;
    private boolean isFallback = false;
    private long timestamp = System.currentTimeMillis();

    public ExternalResponse(String message) {
        this.message = message;
        this.isFallback = false;
    }

    // Фабричный метод для fallback
    public static ExternalResponse fallback(String message) {
        ExternalResponse response = new ExternalResponse();
        response.setMessage(message);
        response.setFallback(true);
        return response;
    }
}
