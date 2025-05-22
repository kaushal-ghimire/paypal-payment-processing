package com.payment.PaymentProcessingSystem.repository;

import com.payment.PaymentProcessingSystem.entity.Transaction;
import com.payment.PaymentProcessingSystem.entity.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByStatus(TransactionStatus status);
}