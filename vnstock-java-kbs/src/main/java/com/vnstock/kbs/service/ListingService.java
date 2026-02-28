package com.vnstock.kbs.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.vnstock.kbs.client.KbsHttpClient;
import com.vnstock.kbs.config.KbsConfig;
import com.vnstock.kbs.config.KbsConstants;
import com.vnstock.kbs.exception.KbsApiException;
import com.vnstock.kbs.model.StockSymbol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for listing and symbol operations.
 */
public class ListingService {
    
    private static final Logger logger = LoggerFactory.getLogger(ListingService.class);
    
    private final KbsHttpClient httpClient;
    private final KbsConfig config;
    
    public ListingService() {
        this(new KbsConfig());
    }
    
    public ListingService(KbsConfig config) {
        this.config = config;
        this.httpClient = new KbsHttpClient(config);
    }
    
    public ListingService(KbsHttpClient httpClient, KbsConfig config) {
        this.httpClient = httpClient;
        this.config = config;
    }
    
    /**
     * Get all stock symbols.
     * 
     * @return List of all stock symbols
     */
    public List<StockSymbol> getAllSymbols() {
        String url = config.baseUrlIis() + "/stock/search/data";
        
        try {
            List<StockSymbol> symbols = httpClient.get(
                url, 
                Map.of(),
                new TypeReference<List<StockSymbol>>() {}
            );
            
            if (symbols == null) {
                return List.of();
            }
            
            // Filter only stocks
            return symbols.stream()
                .filter(s -> "stock".equalsIgnoreCase(s.type()))
                .collect(Collectors.toList());
                
        } catch (Exception e) {
            logger.error("Failed to get all symbols", e);
            throw new KbsApiException("Failed to retrieve stock symbols", e);
        }
    }
    
    /**
     * Get symbols by exchange.
     * 
     * @param getAll If true, include all available fields
     * @return List of symbols with exchange information
     */
    public List<StockSymbol> getSymbolsByExchange(boolean getAll) {
        return getAllSymbols();
    }
    
    /**
     * Get symbols by group/index.
     * 
     * @param group Group name (e.g., "VN30", "HOSE", "ETF")
     * @return List of symbols in the group
     */
    public List<String> getSymbolsByGroup(String group) {
        String groupCode = KbsConstants.GROUP_CODES.get(group);
        if (groupCode == null) {
            throw new KbsApiException("Invalid group: " + group + 
                ". Valid groups: " + KbsConstants.GROUP_CODES.keySet());
        }
        
        String url = config.baseUrlIis() + "/index/" + groupCode + "/stocks";
        
        try {
            GroupResponse response = httpClient.get(url, Map.of(), GroupResponse.class);
            
            if (response == null || response.data == null) {
                return List.of();
            }
            
            return response.data;
            
        } catch (Exception e) {
            logger.error("Failed to get symbols for group: {}", group, e);
            throw new KbsApiException("Failed to retrieve symbols for group: " + group, e);
        }
    }
    
    /**
     * Get all supported groups.
     * 
     * @return Map of group names to descriptions
     */
    public Map<String, String> getSupportedGroups() {
        return Map.ofEntries(
            Map.entry("VN30", "30 largest and most liquid stocks on HOSE"),
            Map.entry("VN100", "100 largest stocks on HOSE"),
            Map.entry("VNMidCap", "Mid-cap index"),
            Map.entry("VNSmallCap", "Small-cap index"),
            Map.entry("VNSI", "Vietnam Small-Cap Index"),
            Map.entry("VNX50", "50 largest stocks across HOSE and HNX"),
            Map.entry("VNXALL", "All stocks on HOSE and HNX"),
            Map.entry("VNALL", "All stocks on HOSE and HNX"),
            Map.entry("HNX30", "30 largest stocks on HNX"),
            Map.entry("HOSE", "Ho Chi Minh Stock Exchange"),
            Map.entry("HNX", "Hanoi Stock Exchange"),
            Map.entry("UPCOM", "UPCOM Market"),
            Map.entry("ETF", "Exchange Traded Funds"),
            Map.entry("CW", "Covered Warrants"),
            Map.entry("BOND", "Corporate Bonds"),
            Map.entry("FU_INDEX", "Futures and Derivatives")
        );
    }
    
    /**
     * Get all industries.
     * 
     * @return List of industries with codes and names
     */
    public List<IndustryInfo> getAllIndustries() {
        String url = config.baseUrlIis() + "/sector/all";
        
        try {
            List<IndustryInfo> industries = httpClient.get(
                url, 
                Map.of(),
                new TypeReference<List<IndustryInfo>>() {}
            );
            
            return industries != null ? industries : List.of();
            
        } catch (Exception e) {
            logger.error("Failed to get industries", e);
            throw new KbsApiException("Failed to retrieve industries", e);
        }
    }
    
    /**
     * Get symbols by industry code.
     * 
     * @param industryCode Industry code
     * @return List of symbols in the industry
     */
    public List<IndustryStock> getSymbolsByIndustry(int industryCode) {
        String url = config.baseUrlIis() + "/sector/stock";
        Map<String, String> params = Map.of(
            "code", String.valueOf(industryCode),
            "l", String.valueOf(config.language())
        );
        
        try {
            IndustryResponse response = httpClient.get(url, params, IndustryResponse.class);
            
            if (response == null || response.stocks == null) {
                return List.of();
            }
            
            return response.stocks;
            
        } catch (Exception e) {
            logger.error("Failed to get symbols for industry: {}", industryCode, e);
            throw new KbsApiException("Failed to retrieve symbols for industry: " + industryCode, e);
        }
    }
    
    /**
     * Get all ETF symbols.
     */
    public List<String> getAllEtf() {
        return getSymbolsByGroup("ETF");
    }
    
    /**
     * Get all bond symbols.
     */
    public List<String> getAllBonds() {
        return getSymbolsByGroup("BOND");
    }
    
    /**
     * Get all futures/derivative symbols.
     */
    public List<String> getAllFutures() {
        return getSymbolsByGroup("FU_INDEX");
    }
    
    /**
     * Get all covered warrant symbols.
     */
    public List<String> getAllCoveredWarrants() {
        return getSymbolsByGroup("CW");
    }
    
    // Response classes
    private record GroupResponse(Integer status, List<String> data) {}
    
    public record IndustryInfo(String name, Integer code, Double change) {}
    
    public record IndustryStock(
        String sb,  // symbol
        String pr,  // price
        String ch,  // change
        String vo   // volume
    ) {}
    
    private record IndustryResponse(String name, List<IndustryStock> stocks) {}
}
