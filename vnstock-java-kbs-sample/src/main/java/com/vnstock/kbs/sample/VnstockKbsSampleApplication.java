package com.vnstock.kbs.sample;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot sample application for VNStock KBS library.
 */
@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "VNStock KBS Sample API",
        version = "1.0.0",
        description = "Sample Spring Boot application demonstrating VNStock KBS library usage"
    )
)
public class VnstockKbsSampleApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(VnstockKbsSampleApplication.class, args);
    }
}
