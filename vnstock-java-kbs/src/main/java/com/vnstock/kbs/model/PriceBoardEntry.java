package com.vnstock.kbs.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Real-time price board entry.
 * 
 * @param symbol Stock symbol
 * @param timestamp Last update timestamp (Unix milliseconds)
 * @param exchange Stock exchange
 * @param ceilingPrice Ceiling price
 * @param floorPrice Floor price
 * @param referencePrice Reference price
 * @param openPrice Opening price
 * @param highPrice High price
 * @param lowPrice Low price
 * @param closePrice Close/match price
 * @param averagePrice Average price
 * @param totalTrades Total number of trades
 * @param totalValue Total trading value
 * @param priceChange Price change
 * @param percentChange Percentage change
 * @param bidPrice1 Bid price level 1
 * @param bidVol1 Bid volume level 1
 * @param bidPrice2 Bid price level 2
 * @param bidVol2 Bid volume level 2
 * @param bidPrice3 Bid price level 3
 * @param bidVol3 Bid volume level 3
 * @param askPrice1 Ask price level 1
 * @param askVol1 Ask volume level 1
 * @param askPrice2 Ask price level 2
 * @param askVol2 Ask volume level 2
 * @param askPrice3 Ask price level 3
 * @param askVol3 Ask volume level 3
 * @param foreignBuyVolume Foreign buy volume
 * @param foreignSellVolume Foreign sell volume
 * @param foreignBuyCount Foreign buy transaction count
 * @param foreignSellCount Foreign sell transaction count
 * @param putThroughQty Put-through quantity
 * @param putThroughValue Put-through value
 * @param totalListedQty Total listed quantity
 * @param listedShares Listed shares
 * @param foreignOwnershipRatio Foreign ownership ratio
 */
public record PriceBoardEntry(
    @JsonProperty("SB") String symbol,
    @JsonProperty("t") Long timestamp,
    @JsonProperty("EX") String exchange,
    @JsonProperty("CL") Long ceilingPrice,
    @JsonProperty("FL") Long floorPrice,
    @JsonProperty("RE") Long referencePrice,
    @JsonProperty("OP") Long openPrice,
    @JsonProperty("HI") Long highPrice,
    @JsonProperty("LO") Long lowPrice,
    @JsonProperty("CP") Long closePrice,
    @JsonProperty("AP") Long averagePrice,
    @JsonProperty("TT") Long totalTrades,
    @JsonProperty("TV") BigDecimal totalValue,
    @JsonProperty("CH") Long priceChange,
    @JsonProperty("CHP") BigDecimal percentChange,
    @JsonProperty("B1") String bidPrice1,
    @JsonProperty("V1") Long bidVol1,
    @JsonProperty("B2") String bidPrice2,
    @JsonProperty("V2") Long bidVol2,
    @JsonProperty("B3") String bidPrice3,
    @JsonProperty("V3") Long bidVol3,
    @JsonProperty("S1") String askPrice1,
    @JsonProperty("U1") Long askVol1,
    @JsonProperty("S2") String askPrice2,
    @JsonProperty("U2") Long askVol2,
    @JsonProperty("S3") String askPrice3,
    @JsonProperty("U3") Long askVol3,
    @JsonProperty("FB") Long foreignBuyVolume,
    @JsonProperty("FR") Long foreignSellVolume,
    @JsonProperty("FC") Long foreignBuyCount,
    @JsonProperty("FS") Long foreignSellCount,
    @JsonProperty("PTQ") Long putThroughQty,
    @JsonProperty("PTV") BigDecimal putThroughValue,
    @JsonProperty("TLQ") Long totalListedQty,
    @JsonProperty("LS") Long listedShares,
    @JsonProperty("FO") BigDecimal foreignOwnershipRatio
) {
    
    /**
     * Converts timestamp to LocalDateTime.
     */
    public LocalDateTime lastUpdateTime() {
        if (timestamp == null) {
            return null;
        }
        return LocalDateTime.ofInstant(
            Instant.ofEpochMilli(timestamp), 
            ZoneId.systemDefault()
        );
    }
    
    /**
     * Normalizes price (divides by 1000 to get actual VND value).
     */
    private static BigDecimal normalize(Long price) {
        if (price == null) {
            return null;
        }
        return BigDecimal.valueOf(price).divide(BigDecimal.valueOf(1000));
    }
    
    private static BigDecimal normalizeString(String price) {
        if (price == null || price.isEmpty()) {
            return null;
        }
        try {
            return new BigDecimal(price).divide(BigDecimal.valueOf(1000));
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    public BigDecimal normalizedCeilingPrice() { return normalize(ceilingPrice); }
    public BigDecimal normalizedFloorPrice() { return normalize(floorPrice); }
    public BigDecimal normalizedReferencePrice() { return normalize(referencePrice); }
    public BigDecimal normalizedOpenPrice() { return normalize(openPrice); }
    public BigDecimal normalizedHighPrice() { return normalize(highPrice); }
    public BigDecimal normalizedLowPrice() { return normalize(lowPrice); }
    public BigDecimal normalizedClosePrice() { return normalize(closePrice); }
    public BigDecimal normalizedAveragePrice() { return normalize(averagePrice); }
    public BigDecimal normalizedBidPrice1() { return normalizeString(bidPrice1); }
    public BigDecimal normalizedBidPrice2() { return normalizeString(bidPrice2); }
    public BigDecimal normalizedBidPrice3() { return normalizeString(bidPrice3); }
    public BigDecimal normalizedAskPrice1() { return normalizeString(askPrice1); }
    public BigDecimal normalizedAskPrice2() { return normalizeString(askPrice2); }
    public BigDecimal normalizedAskPrice3() { return normalizeString(askPrice3); }
}
