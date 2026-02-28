package com.vnstock.kbs.sample.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

/**
 * Request DTO for historical price data.
 */
public record HistoryRequest(
    @NotNull(message = "Start date is required")
    LocalDate start,
    
    @NotNull(message = "End date is required")
    LocalDate end,
    
    @Pattern(regexp = "^(1m|5m|15m|30m|1h|1d|1w|1M)$", 
             message = "Interval must be one of: 1m, 5m, 15m, 30m, 1h, 1d, 1w, 1M")
    String interval
) {}
