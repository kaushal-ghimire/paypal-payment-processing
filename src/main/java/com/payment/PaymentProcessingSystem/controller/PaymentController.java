package com.payment.PaymentProcessingSystem.controller;

import com.payment.PaymentProcessingSystem.entity.Transaction;
import com.payment.PaymentProcessingSystem.entity.TransactionStatus;
import com.payment.PaymentProcessingSystem.exception.NetworkException;
import com.payment.PaymentProcessingSystem.order.CreateOrder;
import com.payment.PaymentProcessingSystem.repository.TransactionRepository;
import com.payment.PaymentProcessingSystem.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private CreateOrder createOrder;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/create")
    public ResponseEntity<?> createOrder() {
        String approvalUrl = createOrder.createOrder(); // parse and return redirect URL
        return ResponseEntity.ok("Approve Url: " + approvalUrl);
    }

    @GetMapping("/success")
    public String success(@RequestParam String token) {
        // capture order here
        return "Payment Approved: " + token;
    }

    @GetMapping("/cancel")
    public String cancel() {
        return "Payment Cancelled";
    }

//    @PostMapping("/store")
//    public ResponseEntity<String> createPayment(@RequestParam UUID userId,
//                                                @RequestParam BigDecimal amount) throws NetworkException {
//        paymentService.processPayment(userId, amount);
//        return ResponseEntity.ok("Payment stored");
//    }

    @PostMapping("/store")
    public ResponseEntity<String> createPayment(@RequestBody Transaction tx) {
        tx.setCreatedAt(LocalDateTime.now());
        tx.setUpdatedAt(LocalDateTime.now());
        tx.setRetries(tx.getRetries());
        tx.setFailureReason(tx.getFailureReason());

        transactionRepository.save(tx);
        return ResponseEntity.ok("Payment stored");
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionRepository.findAll());
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable UUID id) {
        return transactionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
