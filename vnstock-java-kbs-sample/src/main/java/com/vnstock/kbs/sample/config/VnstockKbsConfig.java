package com.vnstock.kbs.sample.config;

import com.vnstock.kbs.VnstockKbsClient;
import com.vnstock.kbs.config.KbsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Configuration for VNStock KBS client.
 */
@Configuration
public class VnstockKbsConfig {
    
    @Value("${vnstock.kbs.connect-timeout-seconds:10}")
    private int connectTimeoutSeconds;
    
    @Value("${vnstock.kbs.read-timeout-seconds:30}")
    private int readTimeoutSeconds;
    
    @Value("${vnstock.kbs.max-retries:3}")
    private int maxRetries;
    
    @Value("${vnstock.kbs.language:1}")
    private int language;
    
    @Bean
    public VnstockKbsClient vnstockKbsClient() {
        KbsConfig config = KbsConfig.builder()
            .connectTimeout(Duration.ofSeconds(connectTimeoutSeconds))
            .readTimeout(Duration.ofSeconds(readTimeoutSeconds))
            .maxRetries(maxRetries)
            .language(language)
            .build();
        
        return new VnstockKbsClient(config);
    }
}
