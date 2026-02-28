package com.vnstock.kbs.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.vnstock.kbs.client.KbsHttpClient;
import com.vnstock.kbs.config.KbsConfig;
import com.vnstock.kbs.config.KbsConstants;
import com.vnstock.kbs.exception.KbsApiException;
import com.vnstock.kbs.model.CompanyProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Service for company information operations.
 */
public class CompanyService {
    
    private static final Logger logger = LoggerFactory.getLogger(CompanyService.class);
    
    private final KbsHttpClient httpClient;
    private final KbsConfig config;
    private final String symbol;
    
    public CompanyService(String symbol) {
        this(symbol, new KbsConfig());
    }
    
    public CompanyService(String symbol, KbsConfig config) {
        this.symbol = symbol.toUpperCase();
        this.config = config;
        this.httpClient = new KbsHttpClient(config);
    }
    
    public CompanyService(String symbol, KbsHttpClient httpClient, KbsConfig config) {
        this.symbol = symbol.toUpperCase();
        this.httpClient = httpClient;
        this.config = config;
    }
    
    /**
     * Get company overview/profile.
     */
    public CompanyProfile getOverview() {
        String url = config.baseUrlIis() + "/stockinfo/profile/" + symbol;
        Map<String, String> params = Map.of("l", String.valueOf(config.language()));
        
        try {
            return httpClient.get(url, params, CompanyProfile.class);
        } catch (Exception e) {
            logger.error("Failed to get company profile for {}", symbol, e);
            throw new KbsApiException("Failed to retrieve company profile for " + symbol, e);
        }
    }
    
    /**
     * Get company officers/leaders.
     */
    public List<CompanyProfile.Leader> getOfficers() {
        CompanyProfile profile = getOverview();
        return profile != null && profile.leaders() != null ? profile.leaders() : List.of();
    }
    
    /**
     * Get major shareholders.
     */
    public List<CompanyProfile.Shareholder> getShareholders() {
        CompanyProfile profile = getOverview();
        return profile != null && profile.shareholders() != null ? profile.shareholders() : List.of();
    }
    
    /**
     * Get ownership structure.
     */
    public List<CompanyProfile.Ownership> getOwnership() {
        CompanyProfile profile = getOverview();
        return profile != null && profile.ownership() != null ? profile.ownership() : List.of();
    }
    
    /**
     * Get subsidiaries.
     */
    public List<CompanyProfile.Subsidiary> getSubsidiaries() {
        CompanyProfile profile = getOverview();
        return profile != null && profile.subsidiaries() != null ? profile.subsidiaries() : List.of();
    }
    
    /**
     * Get charter capital history.
     */
    public List<CompanyProfile.CharterCapitalEntry> getCapitalHistory() {
        CompanyProfile profile = getOverview();
        return profile != null && profile.charterCapitalHistory() != null 
            ? profile.charterCapitalHistory() : List.of();
    }
    
    /**
     * Get company events.
     */
    public List<Event> getEvents(Integer eventType, int page, int pageSize) {
        String url = config.baseUrlIis() + "/stockinfo/event/" + symbol;
        Map<String, String> params = new java.util.HashMap<>(Map.of(
            "l", String.valueOf(config.language()),
            "p", String.valueOf(page),
            "s", String.valueOf(pageSize)
        ));
        
        if (eventType != null) {
            params.put("eID", String.valueOf(eventType));
        }
        
        try {
            return httpClient.get(url, params, new TypeReference<List<Event>>() {});
        } catch (Exception e) {
            logger.error("Failed to get events for {}", symbol, e);
            throw new KbsApiException("Failed to retrieve events for " + symbol, e);
        }
    }
    
    /**
     * Get company news.
     */
    public List<NewsItem> getNews(int page, int pageSize) {
        String url = config.baseUrlIis() + "/stockinfo/news/" + symbol;
        Map<String, String> params = Map.of(
            "l", String.valueOf(config.language()),
            "p", String.valueOf(page),
            "s", String.valueOf(pageSize)
        );
        
        try {
            return httpClient.get(url, params, new TypeReference<List<NewsItem>>() {});
        } catch (Exception e) {
            logger.error("Failed to get news for {}", symbol, e);
            throw new KbsApiException("Failed to retrieve news for " + symbol, e);
        }
    }
    
    /**
     * Get insider trading reports.
     */
    public List<InsiderTrade> getInsiderTrading(int page, int pageSize) {
        String url = config.baseUrlIis() + "/stockinfo/news/internal-trading/" + symbol;
        Map<String, String> params = Map.of(
            "l", String.valueOf(config.language()),
            "p", String.valueOf(page),
            "s", String.valueOf(pageSize)
        );
        
        try {
            return httpClient.get(url, params, new TypeReference<List<InsiderTrade>>() {});
        } catch (Exception e) {
            logger.error("Failed to get insider trading for {}", symbol, e);
            throw new KbsApiException("Failed to retrieve insider trading for " + symbol, e);
        }
    }
    
    // Record classes for responses
    public record Event(
        @JsonProperty("EventID") Integer eventId,
        @JsonProperty("StockCode") String stockCode,
        @JsonProperty("Title") String title,
        @JsonProperty("Content") String content,
        @JsonProperty("EventDate") String eventDate,
        @JsonProperty("EventType") Integer eventType,
        @JsonProperty("EventTypeName") String eventTypeName
    ) {}
    
    public record NewsItem(
        @JsonProperty("ArticleID") Integer articleId,
        @JsonProperty("Title") String title,
        @JsonProperty("PublishTime") String publishTime,
        @JsonProperty("URL") String url,
        @JsonProperty("Head") String head
    ) {}
    
    public record InsiderTrade(
        @JsonProperty("EventID") Integer eventId,
        @JsonProperty("StockCode") String stockCode,
        @JsonProperty("Title") String title,
        @JsonProperty("Content") String content,
        @JsonProperty("TypeName") String typeName,
        @JsonProperty("RegisterBuyVolume") Long registerBuyVolume,
        @JsonProperty("BuyVolume") Long buyVolume,
        @JsonProperty("VolumeBefore") Long volumeBefore,
        @JsonProperty("VolumeAfter") Long volumeAfter
    ) {}
}
