package com.payment.PaymentProcessingSystem.transactionscheduler;

import com.payment.PaymentProcessingSystem.entity.Transaction;
import com.payment.PaymentProcessingSystem.entity.TransactionStatus;
import com.payment.PaymentProcessingSystem.repository.TransactionRepository;
import com.payment.PaymentProcessingSystem.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RetryScheduler {

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private PaymentService paymentService;

    @Scheduled(fixedDelay = 60000) // every 60 seconds
    public void retryFailedTransactions() {
        List<Transaction> failed = repository.findByStatus(TransactionStatus.FAILED);
        for (Transaction tx : failed) {
            if (tx.getRetries() < 3) {
                try {
                    paymentService.processPayment(tx.getUserId(), tx.getAmount(), tx.getStatus());
                } catch (Exception ignored) {}
            }
        }
    }
}
