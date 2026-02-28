package com.vnstock.kbs.sample.service;

import com.vnstock.kbs.VnstockKbsClient;
import com.vnstock.kbs.model.CompanyProfile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for company operations.
 */
@Service
public class CompanyService {
    
    private final VnstockKbsClient kbsClient;
    
    public CompanyService(VnstockKbsClient kbsClient) {
        this.kbsClient = kbsClient;
    }
    
    public CompanyProfile getProfile(String symbol) {
        return kbsClient.company(symbol).getOverview();
    }
    
    public List<Map<String, Object>> getOfficers(String symbol) {
        var officers = kbsClient.company(symbol).getOfficers();
        return officers.stream()
            .<Map<String, Object>>map(o -> Map.of(
                "name", o.name(),
                "position", o.position(),
                "fromDate", o.fromDate()
            ))
            .collect(Collectors.toList());
    }
    
    public List<Map<String, Object>> getShareholders(String symbol) {
        var shareholders = kbsClient.company(symbol).getShareholders();
        return shareholders.stream()
            .<Map<String, Object>>map(s -> Map.of(
                "name", s.name(),
                "sharesOwned", s.sharesOwned(),
                "ownershipPercentage", s.ownershipPercentage()
            ))
            .collect(Collectors.toList());
    }
    
    public List<Map<String, Object>> getNews(String symbol, int page, int pageSize) {
        var news = kbsClient.company(symbol).getNews(page, pageSize);
        return news.stream()
            .<Map<String, Object>>map(n -> Map.of(
                "title", n.title(),
                "publishTime", n.publishTime(),
                "url", n.url()
            ))
            .collect(Collectors.toList());
    }
}
