package com.vnstock.kbs.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vnstock.kbs.config.KbsConfig;
import com.vnstock.kbs.exception.KbsApiException;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * HTTP client for KBS API.
 */
public class KbsHttpClient {
    
    private static final Logger logger = LoggerFactory.getLogger(KbsHttpClient.class);
    
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final KbsConfig config;
    
    public KbsHttpClient() {
        this(new KbsConfig());
    }
    
    public KbsHttpClient(KbsConfig config) {
        this.config = Objects.requireNonNull(config, "config cannot be null");
        
        this.httpClient = new OkHttpClient.Builder()
            .connectTimeout(config.connectTimeout())
            .readTimeout(config.readTimeout())
            .build();
        
        this.objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    }
    
    /**
     * Executes a GET request.
     */
    public <T> T get(String url, Map<String, String> params, Class<T> responseType) {
        String fullUrl = buildUrlWithParams(url, params);
        Request request = buildRequest(fullUrl);
        return executeRequest(request, responseType);
    }
    
    /**
     * Executes a POST request with JSON body.
     */
    public <T> T post(String url, Object body, Class<T> responseType) {
        try {
            String jsonBody = objectMapper.writeValueAsString(body);
            RequestBody requestBody = RequestBody.create(
                jsonBody, 
                MediaType.parse("application/json; charset=utf-8")
            );
            
            Request request = buildRequest(url).newBuilder()
                .post(requestBody)
                .build();
            
            return executeRequest(request, responseType);
        } catch (Exception e) {
            throw new KbsApiException("Failed to serialize request body", e);
        }
    }
    
    /**
     * Executes a POST request with form data.
     */
    public <T> T postForm(String url, Map<String, String> formData, Class<T> responseType) {
        FormBody.Builder formBuilder = new FormBody.Builder();
        formData.forEach(formBuilder::add);
        
        Request request = buildRequest(url).newBuilder()
            .post(formBuilder.build())
            .build();
        
        return executeRequest(request, responseType);
    }
    
    private Request buildRequest(String url) {
        return new Request.Builder()
            .url(url)
            .header("Accept", "application/json")
            .header("Accept-Language", "en-US,en;q=0.9,vi;q=0.8")
            .header("User-Agent", config.userAgent())
            .header("x-lang", config.language() == 1 ? "vi" : "en")
            .build();
    }
    
    private String buildUrlWithParams(String baseUrl, Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return baseUrl;
        }
        
        StringBuilder url = new StringBuilder(baseUrl);
        url.append("?");
        
        params.forEach((key, value) -> {
            if (value != null) {
                url.append(URLEncoder.encode(key, StandardCharsets.UTF_8))
                   .append("=")
                   .append(URLEncoder.encode(value, StandardCharsets.UTF_8))
                   .append("&");
            }
        });
        
        // Remove trailing &
        if (url.charAt(url.length() - 1) == '&') {
            url.setLength(url.length() - 1);
        }
        
        return url.toString();
    }
    
    private <T> T executeRequest(Request request, Class<T> responseType) {
        int attempts = 0;
        Exception lastException = null;
        
        while (attempts < config.maxRetries()) {
            attempts++;
            
            try (Response response = httpClient.newCall(request).execute()) {
                String responseBody = response.body() != null ? response.body().string() : null;
                
                if (response.isSuccessful()) {
                    if (responseBody == null || responseBody.isEmpty()) {
                        return null;
                    }
                    return objectMapper.readValue(responseBody, responseType);
                } else {
                    throw new KbsApiException(
                        "API request failed: " + response.code() + " " + response.message(),
                        response.code(),
                        responseBody
                    );
                }
            } catch (IOException e) {
                lastException = e;
                logger.warn("Request attempt {} failed: {}", attempts, e.getMessage());
                
                if (attempts < config.maxRetries()) {
                    try {
                        Thread.sleep(1000 * attempts); // Exponential backoff
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new KbsApiException("Request interrupted", ie);
                    }
                }
            }
        }
        
        throw new KbsApiException(
            "Request failed after " + attempts + " attempts", 
            lastException
        );
    }
    
    /**
     * Executes a GET request with TypeReference for generic types.
     */
    public <T> T get(String url, Map<String, String> params, TypeReference<T> typeReference) {
        String fullUrl = buildUrlWithParams(url, params);
        Request request = buildRequest(fullUrl);
        return executeRequestWithTypeRef(request, typeReference);
    }
    
    /**
     * Executes a POST request with JSON body and TypeReference.
     */
    public <T> T post(String url, Object body, TypeReference<T> typeReference) {
        try {
            String jsonBody = objectMapper.writeValueAsString(body);
            RequestBody requestBody = RequestBody.create(
                jsonBody, 
                MediaType.parse("application/json; charset=utf-8")
            );
            
            Request request = buildRequest(url).newBuilder()
                .post(requestBody)
                .build();
            
            return executeRequestWithTypeRef(request, typeReference);
        } catch (Exception e) {
            throw new KbsApiException("Failed to serialize request body", e);
        }
    }
    
    private <T> T executeRequestWithTypeRef(Request request, TypeReference<T> typeReference) {
        int attempts = 0;
        Exception lastException = null;
        
        while (attempts < config.maxRetries()) {
            attempts++;
            
            try (Response response = httpClient.newCall(request).execute()) {
                String responseBody = response.body() != null ? response.body().string() : null;
                
                if (response.isSuccessful()) {
                    if (responseBody == null || responseBody.isEmpty()) {
                        return null;
                    }
                    return objectMapper.readValue(responseBody, typeReference);
                } else {
                    throw new KbsApiException(
                        "API request failed: " + response.code() + " " + response.message(),
                        response.code(),
                        responseBody
                    );
                }
            } catch (IOException e) {
                lastException = e;
                logger.warn("Request attempt {} failed: {}", attempts, e.getMessage());
                
                if (attempts < config.maxRetries()) {
                    try {
                        Thread.sleep(1000 * attempts);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new KbsApiException("Request interrupted", ie);
                    }
                }
            }
        }
        
        throw new KbsApiException(
            "Request failed after " + attempts + " attempts", 
            lastException
        );
    }
    
    public void close() {
        httpClient.dispatcher().executorService().shutdown();
        httpClient.connectionPool().evictAll();
    }
}
