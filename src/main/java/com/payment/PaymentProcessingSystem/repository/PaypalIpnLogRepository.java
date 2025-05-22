package com.payment.PaymentProcessingSystem.repository;

import com.payment.PaymentProcessingSystem.paypallogipn.IpnLogPayPal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaypalIpnLogRepository extends JpaRepository<IpnLogPayPal, Long> {
}