package com.payment.PaymentProcessingSystem.order;

import com.payment.PaymentProcessingSystem.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CreateOrder {
    @Value("${paypal.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final PaymentService paypalService;

    @Autowired
    public CreateOrder(PaymentService paypalService) {
        this.paypalService = paypalService;
    }

    public String createOrder() {
        String token = paypalService.getAccessToken();  // Get token from separate service
        String url = baseUrl + "/v2/checkout/orders";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("intent", "CAPTURE");

        Map<String, Object> purchaseUnit = new HashMap<>();
        purchaseUnit.put("amount", Map.of("currency_code", "USD", "value", "10.00"));
        body.put("purchase_units", List.of(purchaseUnit));

        body.put("application_context", Map.of(
                "return_url", "http://localhost:8080/paypal/success",
                "cancel_url", "http://localhost:8080/paypal/cancel"
        ));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        if (response.getStatusCode() == HttpStatus.CREATED) {
            List<Map<String, String>> links = (List<Map<String, String>>) response.getBody().get("links");
            return links.stream()
                    .filter(link -> "approve".equals(link.get("rel")))
                    .map(link -> link.get("href"))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Approval link not found"));
        } else {
            throw new RuntimeException("Failed to create PayPal order: " + response.getStatusCode());
        }
    }
}
