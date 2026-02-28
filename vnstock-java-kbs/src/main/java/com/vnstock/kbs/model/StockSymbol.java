package com.vnstock.kbs.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a stock symbol with basic information.
 * 
 * @param symbol Stock ticker symbol
 * @param name Company name in Vietnamese
 * @param nameEn Company name in English
 * @param exchange Stock exchange (HOSE, HNX, UPCOM)
 * @param type Asset type (stock, index, etc.)
 * @param index Internal index ID
 * @param referencePrice Reference price
 * @param ceilingPrice Ceiling price
 * @param floorPrice Floor price
 */
public record StockSymbol(
    String symbol,
    String name,
    @JsonProperty("nameEn") String nameEn,
    String exchange,
    String type,
    Integer index,
    @JsonProperty("re") Long referencePrice,
    @JsonProperty("ceiling") Long ceilingPrice,
    @JsonProperty("floor") Long floorPrice
) {
    
    /**
     * Returns the display name (Vietnamese name if available, otherwise English).
     */
    public String displayName() {
        return name != null && !name.isEmpty() ? name : nameEn;
    }
    
    /**
     * Checks if this is a stock symbol.
     */
    public boolean isStock() {
        return "stock".equalsIgnoreCase(type);
    }
}
