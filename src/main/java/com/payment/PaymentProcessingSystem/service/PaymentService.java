package com.payment.PaymentProcessingSystem.service;


import com.payment.PaymentProcessingSystem.entity.Transaction;
import com.payment.PaymentProcessingSystem.entity.TransactionStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import com.payment.PaymentProcessingSystem.exception.NetworkException;
import com.payment.PaymentProcessingSystem.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentService {
    @Value("${paypal.client-id}")
    private String clientId;

    @Value("${paypal.client-secret}")
    private String clientSecret;

    @Value("${paypal.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private TransactionRepository transactionRepository;

    public String getAccessToken() {
        String url = baseUrl + "/v1/oauth2/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(clientId, clientSecret);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>("grant_type=client_credentials", headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
        return (String) response.getBody().get("access_token");
    }

    @Retryable(
            value = { NetworkException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 5000)
    )

    public void processPayment(UUID userId, BigDecimal amount, TransactionStatus status) throws NetworkException {
        Transaction tx = new Transaction();
        tx.setUserId(userId);
        tx.setAmount(amount);
        tx.setStatus(status);
        transactionRepository.save(tx);

        try {
            // Simulate external payment API
            externalPaymentApi(userId, amount);

            tx.setStatus(TransactionStatus.SUCCESS);
        } catch (NetworkException e) {
            tx.setStatus(TransactionStatus.FAILED);
            tx.setFailureReason("Network error");
            throw e;
        } catch (Exception e) {
            tx.setStatus(TransactionStatus.FAILED);
            tx.setFailureReason(e.getMessage());
        } finally {
            tx.setUpdatedAt(LocalDateTime.now());
            tx.setRetries(tx.getRetries() + 1);
            transactionRepository.save(tx);
        }
    }

    private void externalPaymentApi(UUID userId, BigDecimal amount) throws NetworkException {
        // Simulated network call
        if (Math.random() < 0.5) {
            throw new NetworkException("Simulated network failure");
        }
    }

}
