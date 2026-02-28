package com.vnstock.kbs.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.vnstock.kbs.client.KbsHttpClient;
import com.vnstock.kbs.config.KbsConfig;
import com.vnstock.kbs.config.KbsConstants;
import com.vnstock.kbs.exception.KbsApiException;
import com.vnstock.kbs.model.HistoricalPrice;
import com.vnstock.kbs.model.IntradayTrade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Service for quote and price data operations.
 */
public class QuoteService {
    
    private static final Logger logger = LoggerFactory.getLogger(QuoteService.class);
    private static final DateTimeFormatter API_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    
    private final KbsHttpClient httpClient;
    private final KbsConfig config;
    private final String symbol;
    
    public QuoteService(String symbol) {
        this(symbol, new KbsConfig());
    }
    
    public QuoteService(String symbol, KbsConfig config) {
        this.symbol = symbol.toUpperCase();
        this.config = config;
        this.httpClient = new KbsHttpClient(config);
    }
    
    public QuoteService(String symbol, KbsHttpClient httpClient, KbsConfig config) {
        this.symbol = symbol.toUpperCase();
        this.httpClient = httpClient;
        this.config = config;
    }
    
    /**
     * Get historical price data.
     * 
     * @param start Start date
     * @param end End date
     * @param interval Interval (1m, 5m, 15m, 30m, 1h, 1d, 1w, 1M)
     * @return List of historical price data
     */
    public List<HistoricalPrice> getHistory(LocalDate start, LocalDate end, String interval) {
        String intervalSuffix = KbsConstants.INTERVAL_MAP.get(interval);
        if (intervalSuffix == null) {
            throw new KbsApiException("Invalid interval: " + interval);
        }
        
        String url = buildHistoricalUrl(intervalSuffix);
        Map<String, String> params = Map.of(
            "sdate", start.format(API_DATE_FORMATTER),
            "edate", end.format(API_DATE_FORMATTER)
        );
        
        try {
            HistoricalResponse response = httpClient.get(url, params, HistoricalResponse.class);
            
            if (response == null) {
                return List.of();
            }
            
            List<RawHistoricalData> rawData = response.getData(intervalSuffix);
            if (rawData == null) {
                return List.of();
            }
            
            return rawData.stream()
                .map(this::convertToHistoricalPrice)
                .toList();
                
        } catch (Exception e) {
            logger.error("Failed to get historical data for {}", symbol, e);
            throw new KbsApiException("Failed to retrieve historical data for " + symbol, e);
        }
    }
    
    /**
     * Get intraday trade history.
     * 
     * @param page Page number (default: 1)
     * @param pageSize Records per page (default: 100)
     * @return List of intraday trades
     */
    public List<IntradayTrade> getIntraday(int page, int pageSize) {
        String url = config.baseUrlIis() + "/trade/history/" + symbol;
        Map<String, String> params = Map.of(
            "page", String.valueOf(page),
            "limit", String.valueOf(pageSize)
        );
        
        try {
            IntradayResponse response = httpClient.get(url, params, IntradayResponse.class);
            
            if (response == null || response.data == null) {
                return List.of();
            }
            
            return response.data;
            
        } catch (Exception e) {
            logger.error("Failed to get intraday data for {}", symbol, e);
            throw new KbsApiException("Failed to retrieve intraday data for " + symbol, e);
        }
    }
    
    private String buildHistoricalUrl(String intervalSuffix) {
        String endpoint = isIndexSymbol() ? "index" : "stocks";
        return config.baseUrlIis() + "/" + endpoint + "/" + symbol + "/data_" + intervalSuffix;
    }
    
    private boolean isIndexSymbol() {
        return KbsConstants.SUPPORTED_INDICES.contains(symbol);
    }
    
    private static final DateTimeFormatter KBS_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    private HistoricalPrice convertToHistoricalPrice(RawHistoricalData raw) {
        LocalDateTime dateTime = LocalDateTime.parse(raw.t, KBS_DATE_FORMATTER);
        return HistoricalPrice.fromRaw(
            dateTime,
            raw.o,
            raw.h,
            raw.l,
            raw.c,
            raw.v
        );
    }
    
    // Response classes
    private static class HistoricalResponse {
        public String symbol;
        @JsonProperty("data_1P") public List<RawHistoricalData> data1P;
        @JsonProperty("data_5P") public List<RawHistoricalData> data5P;
        @JsonProperty("data_15P") public List<RawHistoricalData> data15P;
        @JsonProperty("data_30P") public List<RawHistoricalData> data30P;
        @JsonProperty("data_60P") public List<RawHistoricalData> data60P;
        @JsonProperty("data_day") public List<RawHistoricalData> dataDay;
        @JsonProperty("data_week") public List<RawHistoricalData> dataWeek;
        @JsonProperty("data_month") public List<RawHistoricalData> dataMonth;
        
        public List<RawHistoricalData> getData(String interval) {
            return switch (interval) {
                case "1P" -> data1P;
                case "5P" -> data5P;
                case "15P" -> data15P;
                case "30P" -> data30P;
                case "60P" -> data60P;
                case "day" -> dataDay;
                case "week" -> dataWeek;
                case "month" -> dataMonth;
                default -> null;
            };
        }
    }
    
    private static class RawHistoricalData {
        public String t;  // Format: "2024-01-31 07:00" (no seconds)
        public long o;
        public long h;
        public long l;
        public long c;
        public long v;
    }
    
    private record IntradayResponse(List<IntradayTrade> data) {}
}
