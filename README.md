# VNStock Java KBS

[![Maven Central](https://img.shields.io/maven-central/v/com.vnstock/vnstock-java-kbs.svg)](https://central.sonatype.com/artifact/com.vnstock/vnstock-java-kbs)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Java](https://img.shields.io/badge/Java-25%2B-orange.svg)](https://openjdk.org/)

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
    <version>1.0.0</version>
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

## Sample Application

Check out the [vnstock-java-kbs-sample](vnstock-java-kbs-sample) directory for a complete Spring Boot REST API example demonstrating all library features.

## Building from Source

```bash
# Build the library
cd vnstock-java-kbs
mvn clean install -Dgpg.skip=true

# Run tests
mvn test

# Build everything (including sample)
cd ..
mvn clean install -Dgpg.skip=true
```

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Author

Anh Tri - [anhtri.flagship@gmail.com](mailto:anhtri.flagship@gmail.com)

## Links

- [GitHub Repository](https://github.com/anhtri04/vnstock-java-kbs)
- [Maven Central](https://central.sonatype.com/artifact/com.vnstock/vnstock-java-kbs)
- [Issue Tracker](https://github.com/anhtri04/vnstock-java-kbs/issues)
