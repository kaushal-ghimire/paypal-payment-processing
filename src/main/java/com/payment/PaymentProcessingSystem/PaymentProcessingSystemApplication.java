package com.payment.PaymentProcessingSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.payment.PaymentProcessingSystem")
public class PaymentProcessingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentProcessingSystemApplication.class, args);
	}

}
