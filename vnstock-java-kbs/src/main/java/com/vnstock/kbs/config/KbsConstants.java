package com.vnstock.kbs.config;

import java.util.Map;
import java.util.Set;

/**
 * Constants for KBS API.
 */
public final class KbsConstants {
    
    private KbsConstants() {
        // Prevent instantiation
    }
    
    // Supported intervals for historical data
    public static final Map<String, String> INTERVAL_MAP = Map.ofEntries(
        Map.entry("1m", "1P"),
        Map.entry("5m", "5P"),
        Map.entry("15m", "15P"),
        Map.entry("30m", "30P"),
        Map.entry("1h", "60P"),
        Map.entry("60m", "60P"),
        Map.entry("1d", "day"),
        Map.entry("daily", "day"),
        Map.entry("1w", "week"),
        Map.entry("weekly", "week"),
        Map.entry("1M", "month"),
        Map.entry("monthly", "month")
    );
    
    // Supported index symbols
    public static final Set<String> SUPPORTED_INDICES = Set.of(
        "VNINDEX", "HNXINDEX", "UPCOMINDEX", "VN30", "HNX30", "VN100"
    );
    
    // Group codes for symbols_by_group
    public static final Map<String, String> GROUP_CODES = Map.ofEntries(
        Map.entry("HOSE", "HOSE"),
        Map.entry("HNX", "HNX"),
        Map.entry("UPCOM", "UPCOM"),
        Map.entry("VN30", "30"),
        Map.entry("VN100", "100"),
        Map.entry("VNMidCap", "MID"),
        Map.entry("VNSmallCap", "SML"),
        Map.entry("VNSI", "SI"),
        Map.entry("VNX50", "X50"),
        Map.entry("VNXALL", "XALL"),
        Map.entry("VNALL", "ALL"),
        Map.entry("HNX30", "HNX30"),
        Map.entry("ETF", "FUND"),
        Map.entry("CW", "CW"),
        Map.entry("BOND", "BOND"),
        Map.entry("FU_INDEX", "DER")
    );
    
    // Financial report types
    public static final Map<String, String> FINANCIAL_REPORT_TYPES = Map.of(
        "income_statement", "KQKD",
        "balance_sheet", "CDKT",
        "cash_flow", "LCTT",
        "financial_ratios", "CSTC",
        "planned_indicators", "CTKH",
        "summary", "BCTT"
    );
    
    // Event types
    public static final Map<Integer, String> EVENT_TYPES = Map.of(
        1, "Đại hội cổ đông",
        2, "Trả cổ tức",
        3, "Phát hành",
        4, "Giao dịch cổ đông nội bộ",
        5, "Sự kiện khác"
    );
    
    // Industry codes mapping
    public static final Map<Integer, String> INDUSTRY_CODES = Map.ofEntries(
        Map.entry(1, "Bán buôn"),
        Map.entry(2, "Bảo hiểm"),
        Map.entry(3, "Bất động sản"),
        Map.entry(5, "Chứng khoán"),
        Map.entry(6, "Công nghệ và thông tin"),
        Map.entry(7, "Bán lẻ"),
        Map.entry(8, "Chăm sóc sức khỏe"),
        Map.entry(10, "Khai khoáng"),
        Map.entry(11, "Ngân hàng"),
        Map.entry(12, "Nông - Lâm - Ngư"),
        Map.entry(15, "SX Thiết bị, máy móc"),
        Map.entry(16, "SX Hàng gia dụng"),
        Map.entry(17, "Sản phẩm cao su"),
        Map.entry(18, "SX Nhựa - Hóa chất"),
        Map.entry(19, "Thực phẩm - Đồ uống"),
        Map.entry(20, "Chế biến Thủy sản"),
        Map.entry(21, "Vật liệu xây dựng"),
        Map.entry(22, "Tiện ích"),
        Map.entry(23, "Vận tải - kho bãi"),
        Map.entry(24, "Xây dựng"),
        Map.entry(25, "Dịch vụ lưu trú, ăn uống, giải trí"),
        Map.entry(26, "SX Phụ trợ"),
        Map.entry(27, "Thiết bị điện"),
        Map.entry(28, "Dịch vụ tư vấn, hỗ trợ"),
        Map.entry(29, "Tài chính khác")
    );
}
