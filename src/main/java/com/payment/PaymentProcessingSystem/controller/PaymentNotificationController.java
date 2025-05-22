package com.payment.PaymentProcessingSystem.controller;

import com.payment.PaymentProcessingSystem.paypallogipn.IpnLogPayPal;
import com.payment.PaymentProcessingSystem.repository.PaypalIpnLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/paypal")
public class PaymentNotificationController {

    @Autowired
    private PaypalIpnLogRepository ipnLogRepository;


    @PostMapping("/ipn")
    public ResponseEntity<String> handleIPN(HttpServletRequest request, @RequestBody Map<String, Object> payload) throws IOException {
        String ipnContent = new BufferedReader(new InputStreamReader(request.getInputStream()))
                .lines().collect(Collectors.joining("\n"));

        System.out.println("Received IPN: " + payload);

        // For testing, set to true; in prod, use verifyWithPaypal(ipnContent)
        boolean isVerified = true;

        if (isVerified) {
            Map<String, String> ipnParams = parseQueryString(ipnContent);

            // Extract fields (cast carefully)
            String paymentStatus = (String) payload.get("payment_status");
            String receiverEmail = (String) payload.get("receiver_email");
            String mcGross = String.valueOf(payload.get("mc_gross"));
            String mcCurrency = (String) payload.get("mc_currency");
            String txnId = (String) payload.get("txn_id");
            String payerEmail = (String) payload.get("payer_email");
            String custom = (String) payload.get("custom");

            // Build your entity (assuming IpnLogPayPal has a builder)
            IpnLogPayPal log = IpnLogPayPal.builder()
                    .paymentStatus(paymentStatus)
                    .receiverEmail(receiverEmail)
                    .mcGross(mcGross)
                    .mcCurrency(mcCurrency)
                    .txnId(txnId)
                    .payerEmail(payerEmail)
                    .custom(custom)
                    .receivedAt(LocalDateTime.now())
                    .build();

            ipnLogRepository.save(log);

            return ResponseEntity.ok("Verified & Logged");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid IPN");
    }

    private Map<String, String> parseQueryString(String query) {
        Map<String, String> map = new HashMap<>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2) {
                map.put(URLDecoder.decode(kv[0], StandardCharsets.UTF_8),
                        URLDecoder.decode(kv[1], StandardCharsets.UTF_8));
            }
        }
        return map;
    }

    private boolean verifyWithPaypal(String ipnContent) {
        try {
            URL url = new URL("https://ipnpb.paypal.com/cgi-bin/webscr");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            String payload = "cmd=_notify-validate&" + ipnContent;
            OutputStream os = conn.getOutputStream();
            os.write(payload.getBytes(StandardCharsets.UTF_8));
            os.flush();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = in.readLine();
            return "VERIFIED".equals(response);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
