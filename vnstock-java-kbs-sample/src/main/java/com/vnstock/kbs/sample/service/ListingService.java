package com.vnstock.kbs.sample.service;

import com.vnstock.kbs.VnstockKbsClient;
import com.vnstock.kbs.model.StockSymbol;
import com.vnstock.kbs.service.ListingService.IndustryInfo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for listing operations.
 */
@Service
public class ListingService {
    
    private final VnstockKbsClient kbsClient;
    
    public ListingService(VnstockKbsClient kbsClient) {
        this.kbsClient = kbsClient;
    }
    
    public List<StockSymbol> getAllSymbols() {
        return kbsClient.listing().getAllSymbols();
    }
    
    public List<String> getSymbolsByGroup(String group) {
        return kbsClient.listing().getSymbolsByGroup(group);
    }
    
    public Map<String, String> getSupportedGroups() {
        return kbsClient.listing().getSupportedGroups();
    }
    
    public List<Map<String, Object>> getAllIndustries() {
        List<IndustryInfo> industries = kbsClient.listing().getAllIndustries();
        return industries.stream()
            .<Map<String, Object>>map(i -> Map.of(
                "code", i.code(),
                "name", i.name(),
                "change", i.change()
            ))
            .collect(Collectors.toList());
    }
    
    public List<Map<String, Object>> getSymbolsByIndustry(int code) {
        var stocks = kbsClient.listing().getSymbolsByIndustry(code);
        return stocks.stream()
            .<Map<String, Object>>map(s -> Map.of(
                "symbol", s.sb(),
                "price", s.pr(),
                "change", s.ch(),
                "volume", s.vo()
            ))
            .collect(Collectors.toList());
    }
}
