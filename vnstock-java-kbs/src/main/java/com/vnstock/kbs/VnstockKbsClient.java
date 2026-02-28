package com.vnstock.kbs;

import com.vnstock.kbs.client.KbsHttpClient;
import com.vnstock.kbs.config.KbsConfig;
import com.vnstock.kbs.service.*;

/**
 * Main entry point for VNStock KBS Java library.
 * 
 * This client provides access to Vietnamese stock market data from
 * KBS (KB Securities) data source.
 * 
 * Usage:
 * <pre>
 * VnstockKbsClient client = new VnstockKbsClient();
 * 
 * // Listing operations
 * var symbols = client.listing().getAllSymbols();
 * var vn30 = client.listing().getSymbolsByGroup("VN30");
 * 
 * // Quote operations
 * var history = client.quote("VNM").getHistory(start, end, "1d");
 * var intraday = client.quote("VNM").getIntraday(1, 100);
 * 
 * // Company operations
 * var profile = client.company("VNM").getOverview();
 * var officers = client.company("VNM").getOfficers();
 * 
 * // Financial operations
 * var income = client.finance("VNM").getIncomeStatement("year");
 * var ratios = client.finance("VNM").getRatios("quarter");
 * 
 * // Trading operations
 * var board = client.trading().getPriceBoard(List.of("VNM", "ACB"));
 * </pre>
 */
public class VnstockKbsClient {
    
    private final KbsConfig config;
    private final KbsHttpClient httpClient;
    
    // Service instances (lazy-loaded)
    private volatile ListingService listingService;
    private volatile TradingService tradingService;
    
    /**
     * Creates a client with default configuration.
     */
    public VnstockKbsClient() {
        this(new KbsConfig());
    }
    
    /**
     * Creates a client with custom configuration.
     * 
     * @param config Configuration settings
     */
    public VnstockKbsClient(KbsConfig config) {
        this.config = config;
        this.httpClient = new KbsHttpClient(config);
    }
    
    /**
     * Get listing service for symbol operations.
     */
    public ListingService listing() {
        if (listingService == null) {
            synchronized (this) {
                if (listingService == null) {
                    listingService = new ListingService(httpClient, config);
                }
            }
        }
        return listingService;
    }
    
    /**
     * Get quote service for a specific symbol.
     * 
     * @param symbol Stock symbol (e.g., "VNM", "ACB")
     */
    public QuoteService quote(String symbol) {
        return new QuoteService(symbol, httpClient, config);
    }
    
    /**
     * Get company service for a specific symbol.
     * 
     * @param symbol Stock symbol (e.g., "VNM", "ACB")
     */
    public CompanyService company(String symbol) {
        return new CompanyService(symbol, httpClient, config);
    }
    
    /**
     * Get finance service for a specific symbol.
     * 
     * @param symbol Stock symbol (e.g., "VNM", "ACB")
     */
    public FinanceService finance(String symbol) {
        return new FinanceService(symbol, httpClient, config);
    }
    
    /**
     * Get trading service for real-time data.
     */
    public TradingService trading() {
        if (tradingService == null) {
            synchronized (this) {
                if (tradingService == null) {
                    tradingService = new TradingService(httpClient, config);
                }
            }
        }
        return tradingService;
    }
    
    /**
     * Get the configuration.
     */
    public KbsConfig getConfig() {
        return config;
    }
    
    /**
     * Closes the client and releases resources.
     */
    public void close() {
        httpClient.close();
    }
}
