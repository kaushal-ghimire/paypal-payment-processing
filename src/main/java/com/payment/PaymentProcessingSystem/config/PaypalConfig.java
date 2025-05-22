package com.payment.PaymentProcessingSystem.config;

//import com.paypal.base.rest.APIContext;
//import com.paypal.base.rest.OAuthTokenCredential;
//import com.paypal.base.rest.PayPalRESTException;
//import org.springframework.beans.factory.annotation.Value;
//
//import java.util.HashMap;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;

public class PaypalConfig {
    @Value("${paypal.client-id}")
    private String clientId;

    @Value("${paypal.client-secret}")
    private String clientSecret;

    @Value("${paypal.mode}")
    private String mode;


    @Bean
    public Map<String, String> paypalSdkConfig() {
        Map<String, String> configMap = new HashMap<>();
        configMap.put("mode", mode);
        return configMap;
    }

    @Bean
    public OAuthTokenCredential oAuthTokenCredential() {
        return new OAuthTokenCredential(clientId, clientSecret, paypalSdkConfig());
    }

    @Bean
    public APIContext apiContext() throws PayPalRESTException {
        APIContext context = new APIContext(oAuthTokenCredential().getAccessToken());
        context.setConfigurationMap(paypalSdkConfig());
        return context;
    }

//    public APIContext getAPIContext() throws PayPalRESTException {
//        OAuthTokenCredential tokenCredential = Payment.initConfig(new HashMap<String, String>() {{
//            put("mode", mode);
//            put("clientId", clientId);
//            put("clientSecret", clientSecret);
//        }});
//        String accessToken = tokenCredential.getAccessToken();
//        APIContext context = new APIContext(accessToken);
//        context.setConfigurationMap(new HashMap<String, String>() {{
//            put("mode", mode);
//        }});
//        return context;
//    }
}
