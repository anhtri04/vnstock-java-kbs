package com.vnstock.kbs.sample.controller;

import com.vnstock.kbs.model.CompanyProfile;
import com.vnstock.kbs.sample.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for company operations.
 */
@RestController
@RequestMapping("/api/v1/companies")
@Tag(name = "Companies", description = "Company information operations")
public class CompanyController {
    
    private final CompanyService companyService;
    
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }
    
    @GetMapping("/{symbol}/profile")
    @Operation(summary = "Get company profile")
    public ResponseEntity<CompanyProfile> getProfile(
        @Parameter(description = "Stock symbol") @PathVariable String symbol
    ) {
        return ResponseEntity.ok(companyService.getProfile(symbol));
    }
    
    @GetMapping("/{symbol}/officers")
    @Operation(summary = "Get company officers/leaders")
    public ResponseEntity<List<Map<String, Object>>> getOfficers(
        @Parameter(description = "Stock symbol") @PathVariable String symbol
    ) {
        return ResponseEntity.ok(companyService.getOfficers(symbol));
    }
    
    @GetMapping("/{symbol}/shareholders")
    @Operation(summary = "Get major shareholders")
    public ResponseEntity<List<Map<String, Object>>> getShareholders(
        @Parameter(description = "Stock symbol") @PathVariable String symbol
    ) {
        return ResponseEntity.ok(companyService.getShareholders(symbol));
    }
    
    @GetMapping("/{symbol}/news")
    @Operation(summary = "Get company news")
    public ResponseEntity<List<Map<String, Object>>> getNews(
        @Parameter(description = "Stock symbol") @PathVariable String symbol,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int pageSize
    ) {
        return ResponseEntity.ok(companyService.getNews(symbol, page, pageSize));
    }
}
