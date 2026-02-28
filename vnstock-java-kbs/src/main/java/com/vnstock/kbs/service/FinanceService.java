package com.vnstock.kbs.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vnstock.kbs.client.KbsHttpClient;
import com.vnstock.kbs.config.KbsConfig;
import com.vnstock.kbs.config.KbsConstants;
import com.vnstock.kbs.exception.KbsApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;

/**
 * Service for financial data operations.
 */
public class FinanceService {
    
    private static final Logger logger = LoggerFactory.getLogger(FinanceService.class);
    
    private final KbsHttpClient httpClient;
    private final KbsConfig config;
    private final String symbol;
    
    public FinanceService(String symbol) {
        this(symbol, new KbsConfig());
    }
    
    public FinanceService(String symbol, KbsConfig config) {
        this.symbol = symbol.toUpperCase();
        this.config = config;
        this.httpClient = new KbsHttpClient(config);
    }
    
    public FinanceService(String symbol, KbsHttpClient httpClient, KbsConfig config) {
        this.symbol = symbol.toUpperCase();
        this.httpClient = httpClient;
        this.config = config;
    }
    
    /**
     * Get income statement.
     * 
     * @param period "year" or "quarter"
     */
    public FinancialReport getIncomeStatement(String period) {
        return fetchFinancialData("KQKD", period);
    }
    
    /**
     * Get balance sheet.
     * 
     * @param period "year" or "quarter"
     */
    public FinancialReport getBalanceSheet(String period) {
        return fetchFinancialData("CDKT", period);
    }
    
    /**
     * Get cash flow statement.
     * 
     * @param period "year" or "quarter"
     */
    public FinancialReport getCashFlow(String period) {
        return fetchFinancialData("LCTT", period);
    }
    
    /**
     * Get financial ratios.
     * 
     * @param period "year" or "quarter"
     */
    public FinancialReport getRatios(String period) {
        return fetchFinancialData("CSTC", period);
    }
    
    private FinancialReport fetchFinancialData(String reportType, String period) {
        int termType = "year".equalsIgnoreCase(period) ? 1 : 2;
        String url = config.baseUrlSas() + "/kbsv-stock-data-store/stock/finance-info/" + symbol;
        
        Map<String, String> params = new HashMap<>();
        params.put("type", reportType);
        params.put("termtype", String.valueOf(termType));
        params.put("page", "1");
        params.put("pageSize", "8");
        params.put("unit", "1000");
        
        // LCTT uses different parameter names
        if ("LCTT".equals(reportType)) {
            params.put("code", symbol);
            params.put("termType", String.valueOf(termType));
        } else {
            params.put("languageid", String.valueOf(config.language()));
        }
        
        try {
            FinancialResponse response = httpClient.get(url, params, FinancialResponse.class);
            
            if (response == null) {
                throw new KbsApiException("Empty response for financial data");
            }
            
            return parseFinancialResponse(response, reportType);
            
        } catch (Exception e) {
            logger.error("Failed to get financial data for {}: {}", symbol, reportType, e);
            throw new KbsApiException("Failed to retrieve financial data for " + symbol, e);
        }
    }
    
    private FinancialReport parseFinancialResponse(FinancialResponse response, String reportType) {
        List<String> periods = new ArrayList<>();
        if (response.head != null) {
            for (HeadInfo head : response.head) {
                String periodLabel = buildPeriodLabel(head.yearPeriod, head.termName);
                periods.add(periodLabel);
            }
        }
        
        Map<String, String> auditStatusMap = new HashMap<>();
        if (response.audit != null) {
            for (AuditInfo audit : response.audit) {
                auditStatusMap.put(
                    String.valueOf(audit.auditedStatusCode), 
                    audit.description
                );
            }
        }
        
        String contentKey = getContentKey(reportType);
        List<FinancialItem> items = response.content != null ? 
            response.content.get(contentKey) : null;
        
        if (items == null) {
            items = List.of();
        }
        
        return new FinancialReport(
            symbol,
            reportType,
            periods,
            auditStatusMap,
            items
        );
    }
    
    private String buildPeriodLabel(Integer year, String termName) {
        if (year == null) return "";
        if (termName != null && termName.contains("Quý")) {
            String quarter = termName.replace("Quý", "").trim();
            return year + "-Q" + quarter;
        }
        return String.valueOf(year);
    }
    
    private String getContentKey(String reportType) {
        return switch (reportType) {
            case "KQKD" -> "Kết quả kinh doanh";
            case "CDKT" -> "Cân đối kế toán";
            case "LCTT" -> "Lưu chuyển tiền tệ gián tiếp";
            case "CSTC" -> "Chỉ số tài chính";
            default -> "";
        };
    }
    
    // Record classes
    public record FinancialReport(
        String symbol,
        String reportType,
        List<String> periods,
        Map<String, String> auditStatusMap,
        List<FinancialItem> items
    ) {}
    
    public record FinancialItem(
        @JsonProperty("Name") String name,
        @JsonProperty("NameEn") String nameEn,
        @JsonProperty("Unit") String unit,
        @JsonProperty("Levels") Integer levels,
        @JsonProperty("ID") Integer id,
        @JsonProperty("Value1") BigDecimal value1,
        @JsonProperty("Value2") BigDecimal value2,
        @JsonProperty("Value3") BigDecimal value3,
        @JsonProperty("Value4") BigDecimal value4
    ) {
        public BigDecimal getValue(int index) {
            return switch (index) {
                case 0 -> value1;
                case 1 -> value2;
                case 2 -> value3;
                case 3 -> value4;
                default -> null;
            };
        }
    }
    
    // Response classes
    private static class FinancialResponse {
        @JsonProperty("Audit") public List<AuditInfo> audit;
        @JsonProperty("Unit") public List<UnitInfo> unit;
        @JsonProperty("Head") public List<HeadInfo> head;
        @JsonProperty("Content") public Map<String, List<FinancialItem>> content;
    }
    
    private static class AuditInfo {
        @JsonProperty("AuditedStatusCode") public String auditedStatusCode;
        @JsonProperty("Description") public String description;
    }
    
    private static class UnitInfo {
        @JsonProperty("UnitCode") public String unitCode;
        @JsonProperty("Description") public String description;
    }
    
    private static class HeadInfo {
        @JsonProperty("YearPeriod") public Integer yearPeriod;
        @JsonProperty("TermName") public String termName;
        @JsonProperty("TermNameEN") public String termNameEn;
        @JsonProperty("AuditedStatus") public String auditedStatus;
    }
}
