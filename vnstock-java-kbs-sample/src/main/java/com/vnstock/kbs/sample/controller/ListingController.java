package com.vnstock.kbs.sample.controller;

import com.vnstock.kbs.model.StockSymbol;
import com.vnstock.kbs.sample.service.ListingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for listing operations.
 */
@RestController
@RequestMapping("/api/v1/listing")
@Tag(name = "Listing", description = "Stock listing and symbol operations")
public class ListingController {
    
    private final ListingService listingService;
    
    public ListingController(ListingService listingService) {
        this.listingService = listingService;
    }
    
    @GetMapping("/symbols")
    @Operation(summary = "Get all stock symbols")
    public ResponseEntity<List<StockSymbol>> getAllSymbols() {
        return ResponseEntity.ok(listingService.getAllSymbols());
    }
    
    @GetMapping("/groups/{group}")
    @Operation(summary = "Get symbols by group")
    public ResponseEntity<List<String>> getSymbolsByGroup(
        @Parameter(description = "Group name (e.g., VN30, HOSE, ETF)") 
        @PathVariable String group
    ) {
        return ResponseEntity.ok(listingService.getSymbolsByGroup(group));
    }
    
    @GetMapping("/groups")
    @Operation(summary = "Get all supported groups")
    public ResponseEntity<Map<String, String>> getSupportedGroups() {
        return ResponseEntity.ok(listingService.getSupportedGroups());
    }
    
    @GetMapping("/industries")
    @Operation(summary = "Get all industries")
    public ResponseEntity<List<Map<String, Object>>> getAllIndustries() {
        return ResponseEntity.ok(listingService.getAllIndustries());
    }
    
    @GetMapping("/industries/{code}/stocks")
    @Operation(summary = "Get symbols by industry code")
    public ResponseEntity<List<Map<String, Object>>> getSymbolsByIndustry(
        @Parameter(description = "Industry code") 
        @PathVariable int code
    ) {
        return ResponseEntity.ok(listingService.getSymbolsByIndustry(code));
    }
}
