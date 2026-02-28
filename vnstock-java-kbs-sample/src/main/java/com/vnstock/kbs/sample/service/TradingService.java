package com.vnstock.kbs.sample.service;

import com.vnstock.kbs.VnstockKbsClient;
import com.vnstock.kbs.model.PriceBoardEntry;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for trading operations.
 */
@Service
public class TradingService {
    
    private final VnstockKbsClient kbsClient;
    
    public TradingService(VnstockKbsClient kbsClient) {
        this.kbsClient = kbsClient;
    }
    
    public List<PriceBoardEntry> getPriceBoard(List<String> symbols) {
        return kbsClient.trading().getPriceBoard(symbols);
    }
    
    public PriceBoardEntry getPrice(String symbol) {
        return kbsClient.trading().getPrice(symbol);
    }
}
