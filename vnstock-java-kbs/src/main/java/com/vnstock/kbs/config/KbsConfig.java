package com.vnstock.kbs.config;

import java.time.Duration;
import java.util.Objects;

/**
 * Configuration class for KBS API client.
 * 
 * @param baseUrlIis Base URL for IIS server endpoints
 * @param baseUrlSas Base URL for SAS server endpoints
 * @param connectTimeout Connection timeout duration
 * @param readTimeout Read timeout duration
 * @param maxRetries Maximum number of retry attempts
 * @param userAgent User agent string
 * @param language Language code (1 for Vietnamese, 2 for English)
 */
public record KbsConfig(
    String baseUrlIis,
    String baseUrlSas,
    Duration connectTimeout,
    Duration readTimeout,
    int maxRetries,
    String userAgent,
    int language
) {
    
    // Default values
    public static final String DEFAULT_BASE_URL_IIS = "https://kbbuddywts.kbsec.com.vn/iis-server/investment";
    public static final String DEFAULT_BASE_URL_SAS = "https://kbbuddywts.kbsec.com.vn/sas";
    public static final Duration DEFAULT_CONNECT_TIMEOUT = Duration.ofSeconds(10);
    public static final Duration DEFAULT_READ_TIMEOUT = Duration.ofSeconds(30);
    public static final int DEFAULT_MAX_RETRIES = 3;
    public static final String DEFAULT_USER_AGENT = "VNStock-Java-KBS/1.0.0";
    public static final int DEFAULT_LANGUAGE = 1; // Vietnamese
    
    /**
     * Creates a default configuration.
     */
    public KbsConfig() {
        this(
            DEFAULT_BASE_URL_IIS,
            DEFAULT_BASE_URL_SAS,
            DEFAULT_CONNECT_TIMEOUT,
            DEFAULT_READ_TIMEOUT,
            DEFAULT_MAX_RETRIES,
            DEFAULT_USER_AGENT,
            DEFAULT_LANGUAGE
        );
    }
    
    /**
     * Creates a builder for custom configuration.
     */
    public static Builder builder() {
        return new Builder();
    }
    
    /**
     * Builder pattern for KbsConfig.
     */
    public static class Builder {
        private String baseUrlIis = DEFAULT_BASE_URL_IIS;
        private String baseUrlSas = DEFAULT_BASE_URL_SAS;
        private Duration connectTimeout = DEFAULT_CONNECT_TIMEOUT;
        private Duration readTimeout = DEFAULT_READ_TIMEOUT;
        private int maxRetries = DEFAULT_MAX_RETRIES;
        private String userAgent = DEFAULT_USER_AGENT;
        private int language = DEFAULT_LANGUAGE;
        
        public Builder baseUrlIis(String baseUrlIis) {
            this.baseUrlIis = Objects.requireNonNull(baseUrlIis, "baseUrlIis cannot be null");
            return this;
        }
        
        public Builder baseUrlSas(String baseUrlSas) {
            this.baseUrlSas = Objects.requireNonNull(baseUrlSas, "baseUrlSas cannot be null");
            return this;
        }
        
        public Builder connectTimeout(Duration connectTimeout) {
            this.connectTimeout = Objects.requireNonNull(connectTimeout, "connectTimeout cannot be null");
            return this;
        }
        
        public Builder readTimeout(Duration readTimeout) {
            this.readTimeout = Objects.requireNonNull(readTimeout, "readTimeout cannot be null");
            return this;
        }
        
        public Builder maxRetries(int maxRetries) {
            if (maxRetries < 0) {
                throw new IllegalArgumentException("maxRetries must be non-negative");
            }
            this.maxRetries = maxRetries;
            return this;
        }
        
        public Builder userAgent(String userAgent) {
            this.userAgent = Objects.requireNonNull(userAgent, "userAgent cannot be null");
            return this;
        }
        
        public Builder language(int language) {
            this.language = language;
            return this;
        }
        
        public KbsConfig build() {
            return new KbsConfig(
                baseUrlIis, baseUrlSas, connectTimeout, readTimeout, 
                maxRetries, userAgent, language
            );
        }
    }
}
