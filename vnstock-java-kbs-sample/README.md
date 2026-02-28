# VNStock KBS Sample Application

Spring Boot sample application demonstrating the usage of VNStock Java KBS library.

## Features

- RESTful API for Vietnamese stock market data
- Swagger/OpenAPI documentation
- Spring Boot 3.4 with Java 25
- Integration with KBS (KB Securities) data provider

## API Endpoints

### Listing
- `GET /api/v1/listing/symbols` - Get all stock symbols
- `GET /api/v1/listing/groups/{group}` - Get symbols by group (e.g., VN30, HOSE, ETF)
- `GET /api/v1/listing/groups` - Get all supported groups
- `GET /api/v1/listing/industries` - Get all industries
- `GET /api/v1/listing/industries/{code}/stocks` - Get symbols by industry code

### Quotes
- `GET /api/v1/quotes/{symbol}/history` - Get historical price data
  - Query params: `start` (YYYY-MM-DD), `end` (YYYY-MM-DD), `interval` (1m, 5m, 1d, etc.)
- `GET /api/v1/quotes/{symbol}/intraday` - Get intraday trade history
  - Query params: `page`, `pageSize`

### Companies
- `GET /api/v1/companies/{symbol}/profile` - Get company profile
- `GET /api/v1/companies/{symbol}/officers` - Get company officers
- `GET /api/v1/companies/{symbol}/shareholders` - Get major shareholders
- `GET /api/v1/companies/{symbol}/news` - Get company news

### Trading
- `GET /api/v1/trading/price-board?symbols=VNM,ACB,HPG` - Get real-time price board
- `GET /api/v1/trading/price/{symbol}` - Get real-time price for single symbol

## Running the Application

### Prerequisites
- Java 25
- Maven 3.9+

### Build and Run

```bash
# Build the library first
cd vnstock-java-kbs
mvn clean install
cd ..

# Run the sample application
cd vnstock-java-kbs-sample
mvn spring-boot:run
```

### Access Swagger UI

Open your browser and navigate to: `http://localhost:8080/swagger-ui.html`

## Example Usage

### Get VN30 Symbols
```bash
curl http://localhost:8080/api/v1/listing/groups/VN30
```

### Get Historical Data
```bash
curl "http://localhost:8080/api/v1/quotes/VNM/history?start=2024-01-01&end=2024-01-31&interval=1d"
```

### Get Price Board
```bash
curl "http://localhost:8080/api/v1/trading/price-board?symbols=VNM,ACB,HPG"
```

## Configuration

Edit `application.yml` to customize:

```yaml
vnstock:
  kbs:
    connect-timeout-seconds: 10
    read-timeout-seconds: 30
    max-retries: 3
    language: 1  # 1 = Vietnamese, 2 = English
```

## Health Check

```bash
curl http://localhost:8080/actuator/health
```
