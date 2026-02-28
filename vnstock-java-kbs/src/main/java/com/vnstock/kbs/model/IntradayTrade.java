package com.vnstock.kbs.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an intraday trade/match record.
 * 
 * @param timestamp Full timestamp with milliseconds
 * @param tradingDate Trading date
 * @param symbol Stock symbol
 * @param time Trade time
 * @param side Trade side (B=buy, S=sell)
 * @param price Match price (in VND)
 * @param priceChange Price change from previous match
 * @param matchVolume Match volume
 * @param accumulatedVolume Accumulated volume for the day
 * @param accumulatedValue Accumulated value for the day
 */
public record IntradayTrade(
    @JsonProperty("t") String timestamp,
    @JsonProperty("TD") String tradingDate,
    @JsonProperty("SB") String symbol,
    @JsonProperty("FT") String time,
    @JsonProperty("LC") String side,
    @JsonProperty("FMP") BigDecimal price,
    @JsonProperty("FCV") BigDecimal priceChange,
    @JsonProperty("FV") Long matchVolume,
    @JsonProperty("AVO") Long accumulatedVolume,
    @JsonProperty("AVA") BigDecimal accumulatedValue
) {
    
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = 
        DateTimeFormatter.ofPattern("HH:mm:ss");
    
    /**
     * Parses the timestamp to LocalDateTime.
     * KBS format: "2026-02-27 14:45:29:84" (with milliseconds as :MS)
     */
    public LocalDateTime parsedTimestamp() {
        if (timestamp == null || timestamp.isEmpty()) {
            return null;
        }
        // Remove milliseconds part (last :XX)
        String ts = timestamp.substring(0, timestamp.lastIndexOf(':'));
        return LocalDateTime.parse(ts, TIMESTAMP_FORMATTER);
    }
    
    /**
     * Parses the trading date.
     */
    public LocalDate parsedTradingDate() {
        if (tradingDate == null || tradingDate.isEmpty()) {
            return null;
        }
        return LocalDate.parse(tradingDate, DATE_FORMATTER);
    }
    
    /**
     * Parses the trade time.
     */
    public LocalTime parsedTime() {
        if (time == null || time.isEmpty()) {
            return null;
        }
        return LocalTime.parse(time, TIME_FORMATTER);
    }
    
    /**
     * Checks if this was a buy-side trade.
     */
    public boolean isBuy() {
        return "B".equals(side);
    }
    
    /**
     * Checks if this was a sell-side trade.
     */
    public boolean isSell() {
        return "S".equals(side);
    }
    
    /**
     * Returns normalized price (divided by 1000).
     */
    public BigDecimal normalizedPrice() {
        return price != null ? price.divide(BigDecimal.valueOf(1000)) : null;
    }
}
