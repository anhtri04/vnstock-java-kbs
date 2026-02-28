package com.vnstock.kbs.sample.controller;

import com.vnstock.kbs.model.HistoricalPrice;
import com.vnstock.kbs.model.IntradayTrade;
import com.vnstock.kbs.sample.dto.HistoryRequest;
import com.vnstock.kbs.sample.service.QuoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for quote operations.
 */
@RestController
@RequestMapping("/api/v1/quotes")
@Tag(name = "Quotes", description = "Stock quote and price data operations")
public class QuoteController {
    
    private final QuoteService quoteService;
    
    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }
    
    @GetMapping("/{symbol}/history")
    @Operation(summary = "Get historical price data")
    public ResponseEntity<List<HistoricalPrice>> getHistory(
        @Parameter(description = "Stock symbol") @PathVariable String symbol,
        @Parameter(description = "Start date (YYYY-MM-DD)") 
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
        @Parameter(description = "End date (YYYY-MM-DD)") 
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
        @Parameter(description = "Interval (1m, 5m, 15m, 30m, 1h, 1d, 1w, 1M)") 
        @RequestParam(defaultValue = "1d") String interval
    ) {
        return ResponseEntity.ok(quoteService.getHistory(symbol, start, end, interval));
    }
    
    @PostMapping("/{symbol}/history")
    @Operation(summary = "Get historical price data (POST)")
    public ResponseEntity<List<HistoricalPrice>> getHistoryPost(
        @Parameter(description = "Stock symbol") @PathVariable String symbol,
        @Valid @RequestBody HistoryRequest request
    ) {
        return ResponseEntity.ok(quoteService.getHistory(
            symbol, request.start(), request.end(), request.interval()
        ));
    }
    
    @GetMapping("/{symbol}/intraday")
    @Operation(summary = "Get intraday trade history")
    public ResponseEntity<List<IntradayTrade>> getIntraday(
        @Parameter(description = "Stock symbol") @PathVariable String symbol,
        @Parameter(description = "Page number") @RequestParam(defaultValue = "1") int page,
        @Parameter(description = "Page size") @RequestParam(defaultValue = "100") int pageSize
    ) {
        return ResponseEntity.ok(quoteService.getIntraday(symbol, page, pageSize));
    }
}
