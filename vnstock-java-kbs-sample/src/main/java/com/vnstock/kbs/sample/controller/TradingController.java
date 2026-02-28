package com.vnstock.kbs.sample.controller;

import com.vnstock.kbs.model.PriceBoardEntry;
import com.vnstock.kbs.sample.service.TradingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * REST controller for trading operations.
 */
@RestController
@RequestMapping("/api/v1/trading")
@Tag(name = "Trading", description = "Real-time trading data operations")
public class TradingController {
    
    private final TradingService tradingService;
    
    public TradingController(TradingService tradingService) {
        this.tradingService = tradingService;
    }
    
    @GetMapping("/price-board")
    @Operation(summary = "Get real-time price board for multiple symbols")
    public ResponseEntity<List<PriceBoardEntry>> getPriceBoard(
        @Parameter(description = "Comma-separated list of symbols") 
        @RequestParam String symbols
    ) {
        List<String> symbolList = Arrays.asList(symbols.split(","));
        return ResponseEntity.ok(tradingService.getPriceBoard(symbolList));
    }
    
    @GetMapping("/price/{symbol}")
    @Operation(summary = "Get real-time price for a single symbol")
    public ResponseEntity<PriceBoardEntry> getPrice(
        @Parameter(description = "Stock symbol") @PathVariable String symbol
    ) {
        return ResponseEntity.ok(tradingService.getPrice(symbol));
    }
}
