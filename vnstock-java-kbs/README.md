# VNStock Java KBS Provider

Java library for accessing Vietnamese stock market data from KBS (KB Securities).

## Features

- **Listing**: Get all symbols, symbols by exchange/group/industry
- **Quote**: Historical OHLCV data, intraday trades
- **Company**: Profile, officers, shareholders, ownership, subsidiaries, news
- **Finance**: Income statement, balance sheet, cash flow, financial ratios
- **Trading**: Real-time price board

## Requirements

- Java 25 or later
- Maven 3.9+

## Installation

Add to your `pom.xml`:

```xml
<dependency>
    <groupId>com.vnstock</groupId>
    <artifactId>vnstock-java-kbs</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## Quick Start

```java
import com.vnstock.kbs.VnstockKbsClient;
import com.vnstock.kbs.model.*;
import java.time.LocalDate;
import java.util.List;

public class Example {
    public static void main(String[] args) {
        // Create client
        VnstockKbsClient client = new VnstockKbsClient();
        
        // Get all symbols
        List<StockSymbol> symbols = client.listing().getAllSymbols();
        System.out.println("Total symbols: " + symbols.size());
        
        // Get VN30 symbols
        List<String> vn30 = client.listing().getSymbolsByGroup("VN30");
        System.out.println("VN30 symbols: " + vn30);
        
        // Get historical data for VNM
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 31);
        List<HistoricalPrice> history = client.quote("VNM")
            .getHistory(start, end, "1d");
        
        history.forEach(price -> {
            System.out.printf("%s: Open=%.2f, Close=%.2f, Volume=%d%n",
                price.time(), price.open(), price.close(), price.volume());
        });
        
        // Get company profile
        CompanyProfile profile = client.company("VNM").getOverview();
        System.out.println("Company: " + profile.name());
        
        // Get real-time price board
        List<PriceBoardEntry> board = client.trading()
            .getPriceBoard(List.of("VNM", "ACB", "HPG"));
        
        board.forEach(entry -> {
            System.out.printf("%s: Price=%.2f, Change=%.2f%%%n",
                entry.symbol(),
                entry.normalizedClosePrice(),
                entry.percentChange());
        });
        
        // Close client
        client.close();
    }
}
```

## Configuration

```java
import com.vnstock.kbs.config.KbsConfig;

KbsConfig config = KbsConfig.builder()
    .connectTimeout(Duration.ofSeconds(15))
    .readTimeout(Duration.ofSeconds(60))
    .maxRetries(5)
    .language(1) // 1 = Vietnamese, 2 = English
    .build();

VnstockKbsClient client = new VnstockKbsClient(config);
```

## API Documentation

See [KBS_API_DOCUMENTATION.md](../docs/KBS_API_DOCUMENTATION.md) for detailed API documentation.

## Building

```bash
mvn clean compile
mvn test
mvn package
```

## License

MIT License
