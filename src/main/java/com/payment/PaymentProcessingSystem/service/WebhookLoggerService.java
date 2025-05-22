package com.payment.PaymentProcessingSystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WebhookLoggerService {

    private static final Logger log = LoggerFactory.getLogger(WebhookLoggerService.class);

    public void logEvent(String eventBody, String source) {
        log.info("Webhook received from {}: {}", source, eventBody);
    }
}
