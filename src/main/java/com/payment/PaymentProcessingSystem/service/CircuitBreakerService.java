package com.payment.PaymentProcessingSystem.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CircuitBreakerService {

    private final RestTemplate restTemplate;

    public CircuitBreakerService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    @CircuitBreaker(name = "myApi", fallbackMethod = "fallbackMethod")
    public String callExternalApi() {
        return restTemplate.getForObject("https://some-external-api.com/data", String.class);
    }

    public String fallbackMethod(Throwable t) {
        return "Fallback response due to: " + t.getMessage();
    }
}
