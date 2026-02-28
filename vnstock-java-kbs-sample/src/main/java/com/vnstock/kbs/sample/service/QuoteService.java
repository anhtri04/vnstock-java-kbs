package com.vnstock.kbs.sample.service;

import com.vnstock.kbs.VnstockKbsClient;
import com.vnstock.kbs.model.HistoricalPrice;
import com.vnstock.kbs.model.IntradayTrade;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Service for quote operations.
 */
@Service
public class QuoteService {
    
    private final VnstockKbsClient kbsClient;
    
    public QuoteService(VnstockKbsClient kbsClient) {
        this.kbsClient = kbsClient;
    }
    
    public List<HistoricalPrice> getHistory(String symbol, LocalDate start, LocalDate end, String interval) {
        return kbsClient.quote(symbol).getHistory(start, end, interval);
    }
    
    public List<IntradayTrade> getIntraday(String symbol, int page, int pageSize) {
        return kbsClient.quote(symbol).getIntraday(page, pageSize);
    }
}
