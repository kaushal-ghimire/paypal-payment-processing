package com.payment.PaymentProcessingSystem.controller;

import com.payment.PaymentProcessingSystem.service.WebhookLoggerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;

@RestController
@RequestMapping("/webhook")
public class WebHookLogController {

    private final WebhookLoggerService webhookLoggerService;

    public WebHookLogController(WebhookLoggerService webhookLoggerService) {
        this.webhookLoggerService = webhookLoggerService;
    }

    @PostMapping("/logs")
    public ResponseEntity<String> receiveWebhook(HttpServletRequest request) throws IOException {
        // Read request body as String
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while((line = reader.readLine()) != null){
                sb.append(line);
            }
        }
        String body = sb.toString();

        // Get source IP address for logging
        String sourceIp = request.getRemoteAddr();

        // Optional: Log all headers
        StringBuilder headersBuilder = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headersBuilder.append(headerName).append(": ").append(request.getHeader(headerName)).append("; ");
        }
        String headers = headersBuilder.toString();

        // Log full details
        webhookLoggerService.logEvent("Headers: " + headers + " | Body: " + body, sourceIp);

        // Respond 200 OK (or other as required)
        return ResponseEntity.ok("Webhook received");
    }
}

