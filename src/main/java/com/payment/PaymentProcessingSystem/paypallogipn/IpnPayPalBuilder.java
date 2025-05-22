package com.payment.PaymentProcessingSystem.paypallogipn;


import java.time.LocalDateTime;

public class IpnPayPalBuilder {
    private String paymentStatus;
    private String receiverEmail;
    private String mcGross;
    private String mcCurrency;
    private String txnId;
    private String payerEmail;
    private String custom;
    private LocalDateTime receivedAt;

    public IpnPayPalBuilder paymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; return this; }
    public IpnPayPalBuilder receiverEmail(String receiverEmail) { this.receiverEmail = receiverEmail; return this; }
    public IpnPayPalBuilder mcGross(String mcGross) { this.mcGross = mcGross; return this; }
    public IpnPayPalBuilder mcCurrency(String mcCurrency) { this.mcCurrency = mcCurrency; return this; }
    public IpnPayPalBuilder txnId(String txnId) { this.txnId = txnId; return this; }
    public IpnPayPalBuilder payerEmail(String payerEmail) { this.payerEmail = payerEmail; return this; }
    public IpnPayPalBuilder custom(String custom) { this.custom = custom; return this; }
    public IpnPayPalBuilder receivedAt(LocalDateTime receivedAt) { this.receivedAt = receivedAt; return this; }

    public IpnLogPayPal build() {
        return new IpnLogPayPal(paymentStatus, receiverEmail, mcGross, mcCurrency,
                txnId, payerEmail, custom, receivedAt);
    }
}
