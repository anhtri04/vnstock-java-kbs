package com.vnstock.kbs.exception;

/**
 * Exception thrown when KBS API calls fail.
 */
public class KbsApiException extends RuntimeException {
    
    private final int statusCode;
    private final String responseBody;
    
    public KbsApiException(String message) {
        super(message);
        this.statusCode = -1;
        this.responseBody = null;
    }
    
    public KbsApiException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = -1;
        this.responseBody = null;
    }
    
    public KbsApiException(String message, int statusCode, String responseBody) {
        super(message);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }
    
    public KbsApiException(String message, int statusCode, String responseBody, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
    
    public String getResponseBody() {
        return responseBody;
    }
}
