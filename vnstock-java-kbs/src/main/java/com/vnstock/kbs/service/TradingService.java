package com.vnstock.kbs.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.vnstock.kbs.client.KbsHttpClient;
import com.vnstock.kbs.config.KbsConfig;
import com.vnstock.kbs.exception.KbsApiException;
import com.vnstock.kbs.model.PriceBoardEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for trading and real-time data operations.
 */
public class TradingService {
    
    private static final Logger logger = LoggerFactory.getLogger(TradingService.class);
    
    private final KbsHttpClient httpClient;
    private final KbsConfig config;
    
    public TradingService() {
        this(new KbsConfig());
    }
    
    public TradingService(KbsConfig config) {
        this.config = config;
        this.httpClient = new KbsHttpClient(config);
    }
    
    public TradingService(KbsHttpClient httpClient, KbsConfig config) {
        this.httpClient = httpClient;
        this.config = config;
    }
    
    /**
     * Get real-time price board for multiple symbols.
     * 
     * @param symbols List of stock symbols
     * @param exchange Exchange filter (default: HOSE)
     * @param getAll If true, return all available fields
     * @return List of price board entries
     */
    public List<PriceBoardEntry> getPriceBoard(List<String> symbols, String exchange, boolean getAll) {
        if (symbols == null || symbols.isEmpty()) {
            throw new KbsApiException("Symbols list cannot be empty");
        }
        
        String url = config.baseUrlIis() + "/stock/iss";
        String symbolsStr = symbols.stream()
            .map(String::toUpperCase)
            .collect(Collectors.joining(","));
        
        Map<String, String> body = Map.of("code", symbolsStr);
        
        try {
            return httpClient.post(url, body, new TypeReference<List<PriceBoardEntry>>() {});
        } catch (Exception e) {
            logger.error("Failed to get price board for symbols: {}", symbolsStr, e);
            throw new KbsApiException("Failed to retrieve price board", e);
        }
    }
    
    /**
     * Get real-time price board for multiple symbols (standard columns).
     * 
     * @param symbols List of stock symbols
     * @return List of price board entries
     */
    public List<PriceBoardEntry> getPriceBoard(List<String> symbols) {
        return getPriceBoard(symbols, "HOSE", false);
    }
    
    /**
     * Get real-time price for a single symbol.
     * 
     * @param symbol Stock symbol
     * @return Price board entry or null if not found
     */
    public PriceBoardEntry getPrice(String symbol) {
        List<PriceBoardEntry> entries = getPriceBoard(List.of(symbol));
        return entries.isEmpty() ? null : entries.get(0);
    }
}
