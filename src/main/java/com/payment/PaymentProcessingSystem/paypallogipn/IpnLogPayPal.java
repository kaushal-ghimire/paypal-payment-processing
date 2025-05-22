package com.payment.PaymentProcessingSystem.paypallogipn;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ipn_log_paypal")
public class IpnLogPayPal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String paymentStatus;
    private String receiverEmail;
    private String mcGross;
    private String mcCurrency;
    private String txnId;
    private String payerEmail;
    private String custom;
    private LocalDateTime receivedAt;

    public String getMcCurrency() {
        return mcCurrency;
    }

    public void setMcCurrency(String mcCurrency) {
        this.mcCurrency = mcCurrency;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getMcGross() {
        return mcGross;
    }

    public void setMcGross(String mcGross) {
        this.mcGross = mcGross;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public String getPayerEmail() {
        return payerEmail;
    }

    public void setPayerEmail(String payerEmail) {
        this.payerEmail = payerEmail;
    }

    public String getCustom() {
        return custom;
    }

    public void setCustom(String custom) {
        this.custom = custom;
    }

    public LocalDateTime getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(LocalDateTime receivedAt) {
        this.receivedAt = receivedAt;
    }

    // Constructor
    public IpnLogPayPal(String paymentStatus, String receiverEmail, String mcGross, String mcCurrency,
                        String txnId, String payerEmail, String custom, LocalDateTime receivedAt) {
        this.paymentStatus = paymentStatus;
        this.receiverEmail = receiverEmail;
        this.mcGross = mcGross;
        this.mcCurrency = mcCurrency;
        this.txnId = txnId;
        this.payerEmail = payerEmail;
        this.custom = custom;
        this.receivedAt = receivedAt;
    }

    // Static builder
    public static IpnPayPalBuilder builder() {
        return new IpnPayPalBuilder();
    }
}
