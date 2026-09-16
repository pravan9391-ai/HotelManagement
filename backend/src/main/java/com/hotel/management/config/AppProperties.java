package com.hotel.management.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private final Jwt jwt = new Jwt();
    private final Gemini gemini = new Gemini();
    private final Billing billing = new Billing();

    public Jwt getJwt() {
        return jwt;
    }

    public Gemini getGemini() {
        return gemini;
    }

    public Billing getBilling() {
        return billing;
    }

    public static class Jwt {
        private String secret = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
        private long accessExpirationMs = 3600000;
        private long refreshExpirationMs = 604800000;

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public long getAccessExpirationMs() {
            return accessExpirationMs;
        }

        public void setAccessExpirationMs(long accessExpirationMs) {
            this.accessExpirationMs = accessExpirationMs;
        }

        public long getRefreshExpirationMs() {
            return refreshExpirationMs;
        }

        public void setRefreshExpirationMs(long refreshExpirationMs) {
            this.refreshExpirationMs = refreshExpirationMs;
        }
    }

    public static class Gemini {
        private String apiKey = "";
        private String model = "gemini-1.5-flash";

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }
    }

    public static class Billing {
        private BigDecimal taxRate = new BigDecimal("12.00");

        public BigDecimal getTaxRate() {
            return taxRate;
        }

        public void setTaxRate(BigDecimal taxRate) {
            this.taxRate = taxRate;
        }
    }
}
