package com.vnstock.kbs;

import com.vnstock.kbs.config.KbsConfig;
import com.vnstock.kbs.model.*;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Integration tests for VnstockKbsClient.
 * Note: These tests make real API calls.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VnstockKbsClientTest {
    
    private static VnstockKbsClient client;
    
    @BeforeAll
    static void setUp() {
        client = new VnstockKbsClient();
    }
    
    @AfterAll
    static void tearDown() {
        if (client != null) {
            client.close();
        }
    }
    
    @Test
    @Order(1)
    @DisplayName("Get all symbols")
    void testGetAllSymbols() {
        List<StockSymbol> symbols = client.listing().getAllSymbols();
        
        assertThat(symbols).isNotEmpty();
        assertThat(symbols.size()).isGreaterThan(100);
        
        // Check first symbol has required fields
        StockSymbol first = symbols.get(0);
        assertThat(first.symbol()).isNotEmpty();
        assertThat(first.name()).isNotEmpty();
        assertThat(first.exchange()).isNotEmpty();
    }
    
    @Test
    @Order(2)
    @DisplayName("Get symbols by group - VN30")
    void testGetSymbolsByGroup() {
        List<String> vn30 = client.listing().getSymbolsByGroup("VN30");
        
        assertThat(vn30).hasSize(30);
        assertThat(vn30).contains("VNM", "VCB", "HPG", "GAS");
    }
    
    @Test
    @Order(3)
    @DisplayName("Get historical price data")
    void testGetHistoricalData() {
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 31);
        
        List<HistoricalPrice> history = client.quote("VNM")
            .getHistory(start, end, "1d");
        
        assertThat(history).isNotEmpty();
        
        HistoricalPrice first = history.get(0);
        assertThat(first.open()).isPositive();
        assertThat(first.close()).isPositive();
        assertThat(first.high()).isPositive();
        assertThat(first.low()).isPositive();
        assertThat(first.volume()).isPositive();
    }
    
    @Test
    @Order(4)
    @DisplayName("Get company profile")
    void testGetCompanyProfile() {
        CompanyProfile profile = client.company("VNM").getOverview();
        
        assertThat(profile).isNotNull();
        assertThat(profile.symbol()).isEqualTo("VNM");
        assertThat(profile.businessModel()).isNotEmpty();
    }
    
    @Test
    @Order(5)
    @DisplayName("Get price board")
    void testGetPriceBoard() {
        List<PriceBoardEntry> board = client.trading()
            .getPriceBoard(List.of("VNM", "ACB"));
        
        assertThat(board).isNotEmpty();
        
        PriceBoardEntry entry = board.get(0);
        assertThat(entry.symbol()).isNotEmpty();
        assertThat(entry.closePrice()).isPositive();
    }
    
    @Test
    @Order(6)
    @DisplayName("Get financial data")
    void testGetFinancialData() {
        var report = client.finance("VNM").getIncomeStatement("year");
        
        assertThat(report).isNotNull();
        assertThat(report.symbol()).isEqualTo("VNM");
        assertThat(report.periods()).isNotEmpty();
    }
}
