package com.vnstock.kbs.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents historical OHLCV price data.
 * 
 * @param time Trading timestamp
 * @param open Opening price (in VND)
 * @param high High price (in VND)
 * @param low Low price (in VND)
 * @param close Closing price (in VND)
 * @param volume Trading volume
 */
public record HistoricalPrice(
    @JsonProperty("t") LocalDateTime time,
    @JsonProperty("o") BigDecimal open,
    @JsonProperty("h") BigDecimal high,
    @JsonProperty("l") BigDecimal low,
    @JsonProperty("c") BigDecimal close,
    @JsonProperty("v") Long volume
) {
    
    /**
     * Creates a normalized HistoricalPrice with prices divided by 1000.
     * KBS API returns prices multiplied by 1000.
     */
    public static HistoricalPrice fromRaw(
        LocalDateTime time,
        long openRaw,
        long highRaw,
        long lowRaw,
        long closeRaw,
        long volume
    ) {
        return new HistoricalPrice(
            time,
            BigDecimal.valueOf(openRaw).divide(BigDecimal.valueOf(1000)),
            BigDecimal.valueOf(highRaw).divide(BigDecimal.valueOf(1000)),
            BigDecimal.valueOf(lowRaw).divide(BigDecimal.valueOf(1000)),
            BigDecimal.valueOf(closeRaw).divide(BigDecimal.valueOf(1000)),
            volume
        );
    }
    
    /**
     * Calculates the price change from previous close.
     */
    public BigDecimal priceChange() {
        return close.subtract(open);
    }
    
    /**
     * Calculates the price change percentage.
     */
    public BigDecimal priceChangePercent() {
        if (open.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return priceChange()
            .multiply(BigDecimal.valueOf(100))
            .divide(open, 2, BigDecimal.ROUND_HALF_UP);
    }
}
