# KBS (KB Securities) API Documentation

## Overview

This document describes the API endpoints used by the KBS (KB Securities) data provider for Vietnamese stock market data.

**Base URLs:**
- IIS Server: `https://kbbuddywts.kbsec.com.vn/iis-server/investment`
- SAS Server: `https://kbbuddywts.kbsec.com.vn/sas`

---

## 1. Listing Module

### 1.1 Get All Stock Symbols
**Endpoint:** `GET /stock/search/data`

**Full URL:** `https://kbbuddywts.kbsec.com.vn/iis-server/investment/stock/search/data`

**Description:** Retrieve all stock symbols with basic information.

**Response Format:** JSON Array
```json
[
  {
    "symbol": "ACB",
    "name": "Ngân hàng TMCP Á Châu",
    "nameEn": "Asia Commercial Bank",
    "exchange": "HOSE",
    "type": "stock",
    "index": 1234,
    "re": 25000,
    "ceiling": 27500,
    "floor": 22500
  }
]
```

**Mapped Methods:**
- `all_symbols()` - Returns symbol and organ_name
- `symbols_by_exchange()` - Returns detailed symbol information

---

### 1.2 Get Symbols by Group/Index
**Endpoint:** `GET /index/{group_code}/stocks`

**Full URL:** `https://kbbuddywts.kbsec.com.vn/iis-server/investment/index/{group_code}/stocks`

**Supported Group Codes:**

**Exchange Codes:**
- `HOSE` - Ho Chi Minh Stock Exchange
- `HNX` - Hanoi Stock Exchange  
- `UPCOM` - UPCOM Market

**Index Codes (Use Numeric Format):**
- `30` - VN30 (30 largest stocks on HOSE)
- `100` - VN100 (100 largest stocks on HOSE)
- `MID` - VNMidCap (Mid-cap index)
- `SML` - VNSmallCap (Small-cap index)
- `SI` - VNSI (Small-cap index)
- `X50` - VNX50 (50 largest stocks across HOSE and HNX)
- `XALL` - VNXALL (All stocks on HOSE and HNX)
- `ALL` - VNALL (All stocks on HOSE and HNX)
- `HNX30` - HNX30 (30 largest stocks on HNX)

**Other Asset Types:**
- `FUND` - ETF (Exchange Traded Funds)
- `CW` - Covered Warrants
- `BOND` - Bonds
- `DER` - Derivatives (Futures)

**Note:** For index groups (VN30, VN100, etc.), use the numeric code (e.g., `30` not `VN30`).
Exchange codes (HOSE, HNX, UPCOM) work as-is.

**Response Format:** JSON Array of symbols
```json
["ACB", "BCM", "BID", "BVH", ...]
```

**Mapped Method:** `symbols_by_group(group)`

---

### 1.3 Get All Industries
**Endpoint:** `GET /sector/all`

**Full URL:** `https://kbbuddywts.kbsec.com.vn/iis-server/investment/sector/all`

**Response Format:** JSON Array
```json
[
  {
    "code": 1,
    "name": "Bán buôn"
  },
  {
    "code": 2,
    "name": "Bảo hiểm"
  }
]
```

**Mapped Method:** Internal use for `symbols_by_industries()`

---

### 1.4 Get Symbols by Industry
**Endpoint:** `GET /sector/stock`

**Full URL:** `https://kbbuddywts.kbsec.com.vn/iis-server/investment/sector/stock?code={industry_code}&l=1`

**Query Parameters:**
- `code` (required): Industry code
- `l` (optional): Language (1 for Vietnamese)

**Response Format:** JSON Object
```json
{
  "stocks": [
    {"sb": "ACB"},
    {"sb": "BID"}
  ]
}
```

**Mapped Method:** `symbols_by_industries()`

---

## 2. Quote Module

### 2.1 Get Historical Price Data
**Endpoint:** `GET /stocks/{symbol}/data_{interval}` (for stocks)
**Endpoint:** `GET /index/{symbol}/data_{interval}` (for indices)

**Full URL:** 
- Stocks: `https://kbbuddywts.kbsec.com.vn/iis-server/investment/stocks/{symbol}/data_{interval}`
- Indices: `https://kbbuddywts.kbsec.com.vn/iis-server/investment/index/{symbol}/data_{interval}`

**Supported Intervals:**
- `1P` - 1 minute
- `5P` - 5 minutes
- `15P` - 15 minutes
- `30P` - 30 minutes
- `60P` - 1 hour
- `day` - Daily
- `week` - Weekly
- `month` - Monthly

**Query Parameters:**
- `sdate` (required): Start date (DD-MM-YYYY format)
- `edate` (required): End date (DD-MM-YYYY format)

**Response Format:** JSON Object
```json
{
  "data_day": [
    {
      "t": "2024-01-15T00:00:00",
      "o": 25100,
      "h": 25800,
      "l": 24900,
      "c": 25500,
      "v": 1234567
    }
  ]
}
```

**Mapped Method:** `history(start, end, interval)`

**Field Mapping:**
- `t` → `time`
- `o` → `open` (divided by 1000)
- `h` → `high` (divided by 1000)
- `l` → `low` (divided by 1000)
- `c` → `close` (divided by 1000)
- `v` → `volume`

---

### 2.2 Get Intraday Trade History
**Endpoint:** `GET /trade/history/{symbol}`

**Full URL:** `https://kbbuddywts.kbsec.com.vn/iis-server/investment/trade/history/{symbol}`

**Query Parameters:**
- `page` (optional): Page number (default: 1)
- `limit` (optional): Records per page (default: 100)

**Response Format:** JSON Object
```json
{
  "data": [
    {
      "t": "2024-01-15 09:15:30:15",
      "TD": "15/01/2024",
      "SB": "ACB",
      "FT": "09:15:30",
      "LC": "B",
      "FMP": 25500,
      "FCV": 100,
      "FV": 100,
      "AVO": 50000,
      "AVA": 1275000000
    }
  ]
}
```

**Mapped Method:** `intraday(page, page_size)`

**Field Mapping:**
- `t` → `timestamp`
- `TD` → `trading_date`
- `SB` → `symbol`
- `FT` → `time`
- `LC` → `side` (B=buy, S=sell)
- `FMP` → `price` (divided by 1000)
- `FCV` → `price_change`
- `FV` → `match_volume`
- `AVO` → `accumulated_volume`
- `AVA` → `accumulated_value`

---

## 3. Company Module

### 3.1 Get Company Profile
**Endpoint:** `GET /stockinfo/profile/{symbol}`

**Full URL:** `https://kbbuddywts.kbsec.com.vn/iis-server/investment/stockinfo/profile/{symbol}?l=1`

**Query Parameters:**
- `l` (optional): Language (1 for Vietnamese)

**Response Format:** JSON Object
```json
{
  "SM": "Business model description",
  "SB": "ACB",
  "FD": "1993-06-01",
  "CC": 27000000000000,
  "HM": 5000,
  "LD": "2000-12-01",
  "FV": 10000,
  "EX": "HOSE",
  "LP": 10000,
  "VL": 1000000000,
  "CTP": "CEO Name",
  "CTPP": "CEO Position",
  "IS": "Inspector Name",
  "ISP": "Inspector Position",
  "FP": "License Number",
  "BP": "Business Code",
  "TC": "Tax Code",
  "KT": "Auditor",
  "TY": "Company Type",
  "ADD": "Address",
  "PHONE": "Phone",
  "FAX": "Fax",
  "EMAIL": "Email",
  "URL": "Website",
  "HS": "History",
  "Subsidiaries": [...],
  "Leaders": [...],
  "Ownership": [...],
  "Shareholders": [...],
  "CharterCapital": [...],
  "LaborStructure": [...]
}
```

**Mapped Method:** `overview()`

**Field Mapping:**
- `SM` → `business_model`
- `SB` → `symbol`
- `FD` → `founded_date`
- `CC` → `charter_capital`
- `HM` → `number_of_employees`
- `LD` → `listing_date`
- `FV` → `par_value`
- `EX` → `exchange`
- `LP` → `listing_price`
- `VL` → `listed_volume`
- `CTP` → `ceo_name`
- `CTPP` → `ceo_position`
- `IS` → `inspector_name`
- `ISP` → `inspector_position`
- `FP` → `establishment_license`
- `BP` → `business_code`
- `TC` → `tax_id`
- `KT` → `auditor`
- `TY` → `company_type`
- `ADD` → `address`
- `PHONE` → `phone`
- `FAX` → `fax`
- `EMAIL` → `email`
- `URL` → `website`
- `BRANCH` → `branches`
- `HS` → `history`
- `KLCPNY` → `free_float_percentage`
- `SFV` → `free_float`
- `KLCPLH` → `outstanding_shares`
- `AD` → `as_of_date`

---

### 3.2 Get Company Events
**Endpoint:** `GET /stockinfo/event/{symbol}`

**Full URL:** `https://kbbuddywts.kbsec.com.vn/iis-server/investment/stockinfo/event/{symbol}`

**Query Parameters:**
- `l` (optional): Language (1 for Vietnamese)
- `p` (optional): Page number (default: 1)
- `s` (optional): Page size (default: 10)
- `eID` (optional): Event type filter (1-5)
  - 1: Đại hội cổ đông (AGM)
  - 2: Trả cổ tức (Dividend)
  - 3: Phát hành (Issuance)
  - 4: Giao dịch cổ đông nội bộ (Insider trading)
  - 5: Sự kiện khác (Other)

**Mapped Method:** `events(event_type, page, page_size)`

---

### 3.3 Get Company News
**Endpoint:** `GET /stockinfo/news/{symbol}`

**Full URL:** `https://kbbuddywts.kbsec.com.vn/iis-server/investment/stockinfo/news/{symbol}`

**Query Parameters:**
- `l` (optional): Language (1 for Vietnamese)
- `p` (optional): Page number (default: 1)
- `s` (optional): Page size (default: 10)

**Mapped Method:** `news(page, page_size)`

---

### 3.4 Get Insider Trading
**Endpoint:** `GET /stockinfo/news/internal-trading/{symbol}`

**Full URL:** `https://kbbuddywts.kbsec.com.vn/iis-server/investment/stockinfo/news/internal-trading/{symbol}`

**Query Parameters:**
- `l` (optional): Language (1 for Vietnamese)
- `p` (optional): Page number (default: 1)
- `s` (optional): Page size (default: 10)

**Mapped Method:** `insider_trading(page, page_size)`

---

## 4. Finance Module

### 4.1 Get Financial Reports
**Endpoint:** `GET /kbsv-stock-data-store/stock/finance-info/{symbol}`

**Full URL:** `https://kbbuddywts.kbsec.com.vn/sas/kbsv-stock-data-store/stock/finance-info/{symbol}`

**Query Parameters:**
- `page` (optional): Page number (default: 1)
- `pageSize` (optional): Records per page (default: 8)
- `type` (required): Report type
  - `KQKD` - Income Statement (Kết quả kinh doanh)
  - `CDKT` - Balance Sheet (Cân đối kế toán)
  - `LCTT` - Cash Flow (Lưu chuyển tiền tệ)
  - `CSTC` - Financial Ratios (Chỉ số tài chính)
  - `CTKH` - Planned Indicators (Chỉ tiêu kế hoạch)
  - `BCTT` - Summary Financial Report (Báo cáo tài chính tóm tắt)
- `unit` (optional): Unit (1000 = thousands VND)
- `termtype` (required): Period type (1=year, 2=quarter)
- `languageid` (optional): Language (1 for Vietnamese, except LCTT)

**Special Note for LCTT (Cash Flow):**
- Uses `termType` (camelCase) instead of `termtype`
- Requires `code` parameter
- Does not use `languageid`

**Response Format:** JSON Object
```json
{
  "Audit": [
    {
      "AuditedStatusCode": 1,
      "Description": "Đã kiểm toán"
    }
  ],
  "Unit": [...],
  "Head": [
    {
      "YearPeriod": 2024,
      "TermName": "Quý 1",
      "TermNameEN": "Quarter 1",
      "AuditedStatus": 1,
      "ReportDate": "2024-03-31"
    }
  ],
  "Content": {
    "Kết quả kinh doanh": [
      {
        "Name": "Doanh thu bán hàng",
        "NameEn": "Revenue",
        "Unit": "VND",
        "Levels": 0,
        "ID": 1,
        "Value1": 1000000000,
        "Value2": 950000000,
        "Value3": 900000000,
        "Value4": 850000000
      }
    ]
  }
}
```

**Mapped Methods:**
- `income_statement(period)` → type=KQKD
- `balance_sheet(period)` → type=CDKT
- `cash_flow(period)` → type=LCTT
- `ratio(period)` → type=CSTC

---

## 5. Trading Module

### 5.1 Get Price Board (Real-time)
**Endpoint:** `POST /stock/iss`

**Full URL:** `https://kbbuddywts.kbsec.com.vn/iis-server/investment/stock/iss`

**Request Headers:**
```
Accept-Language: en-US,en;q=0.9,vi;q=0.8
Content-Type: application/json
x-lang: vi
```

**Request Body:**
```json
{
  "code": "ACB,VNM,HPG"
}
```

**Response Format:** JSON Array
```json
[
  {
    "TT": 123456,
    "PP": 0,
    "HI": 25800,
    "TV": 31500000000,
    "LO": 24900,
    "LS": 1000000000,
    "CHP": 2.0,
    "V1": 10000,
    "V2": 5000,
    "V3": 2000,
    "B1": 25400,
    "AP": 25250,
    "B2": 25300,
    "B3": 25200,
    "RE": 25000,
    "EX": "HOSE",
    "FB": 50000,
    "FC": 10,
    "S1": 25500,
    "S2": 25600,
    "S3": 25700,
    "FL": 22500,
    "FO": 20.5,
    "FR": 45000,
    "PTQ": 10000,
    "FS": 8,
    "SB": "ACB",
    "PTV": 250000000,
    "TLQ": 1000000000,
    "OP": 25100,
    "CH": 500,
    "CL": 27500,
    "CP": 25500,
    "TB": 0,
    "PMP": 25000,
    "CV": 1000,
    "t": 1705312800000,
    "PMQ": 50000,
    "TO": 0,
    "U1": 8000,
    "U2": 6000,
    "U3": 4000,
    "MS": "O"
  }
]
```

**Mapped Method:** `price_board(symbols_list)`

**Field Mapping:**
- `SB` → `symbol`
- `t` → `time` (timestamp in milliseconds)
- `EX` → `exchange`
- `CL` → `ceiling_price`
- `FL` → `floor_price`
- `RE` → `reference_price`
- `CP` → `close_price` / `match_price`
- `CV` → `match_volume`
- `OP` → `open_price`
- `HI` → `high_price`
- `LO` → `low_price`
- `AP` → `average_price`
- `TV` → `total_value`
- `CH` → `price_change`
- `CHP` → `percent_change`
- `B1`, `B2`, `B3` → `bid_price_1`, `bid_price_2`, `bid_price_3`
- `V1`, `V2`, `V3` → `bid_vol_1`, `bid_vol_2`, `bid_vol_3`
- `S1`, `S2`, `S3` → `ask_price_1`, `ask_price_2`, `ask_price_3`
- `U1`, `U2`, `U3` → `ask_vol_1`, `ask_vol_2`, `ask_vol_3`
- `FB` → `foreign_buy_volume`
- `FR` → `foreign_sell_volume`
- `TT` → `total_trades`

---

## 6. Supported Index Symbols

The following index symbols are supported for historical data:
- `VNINDEX` - VN-Index
- `HNXINDEX` - HNX-Index
- `UPCOMINDEX` - UPCOM-Index
- `VN30` - VN30 Index
- `HNX30` - HNX30 Index
- `VN100` - VN100 Index

---

## 7. Asset Type Detection

KBS automatically detects asset types:
- **Stock:** Regular stock symbols (e.g., ACB, VNM, HPG)
- **Index:** Index symbols listed above
- **Derivative:** Futures contracts (e.g., VN30F2501M, VN30F2502)
- **ETF:** ETF symbols (e.g., E1VFVN30)
- **Bond:** Bond symbols
- **Covered Warrant:** CW symbols

---

## 8. Error Handling

**Common HTTP Status Codes:**
- `200` - Success
- `400` - Bad Request (invalid parameters)
- `404` - Not Found (invalid symbol or endpoint)
- `500` - Internal Server Error

**Response Error Format:**
```json
{
  "status": 404,
  "message": "Data not found"
}
```

---

## 9. Rate Limiting

No explicit rate limiting is documented. However, it is recommended to:
- Add delays between requests
- Use reasonable page sizes
- Cache results when possible
- Use proxy rotation for high-frequency requests

---

## 10. Data Normalization

### Price Values
All price values (open, high, low, close, bid, ask) are returned in integer format and should be divided by 1000 to get the actual price in VND.

### Volume Values
Volume values are returned as integers representing actual share counts.

### Timestamps
- Historical data: ISO 8601 format (`2024-01-15T00:00:00`)
- Intraday data: Custom format (`2024-01-15 09:15:30:15` with milliseconds)
- Price board: Unix timestamp in milliseconds

---

## 11. Data Source Attribution

When using data from KBS, please attribute:
- **Source:** KB Securities (KBS)
- **Provider:** kbbuddywts.kbsec.com.vn
- **Data Type:** Vietnamese Stock Market Data

---

*Last Updated: February 28, 2026*
*Version: 1.0.0*
