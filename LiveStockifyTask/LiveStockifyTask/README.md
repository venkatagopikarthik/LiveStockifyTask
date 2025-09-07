# LiveStockify Quote API

A RESTful API that provides random inspirational quotes with IP-based rate limiting to prevent abuse.

## Features

- **Random Quote Generation**: Returns inspirational quotes from a curated collection
- **Rate Limiting**: IP-based rate limiting (5 requests per minute per IP)
- **Request Logging**: Logs all requests with client IP and response status
- **Thread-Safe**: Uses ConcurrentHashMap for thread-safe rate limiting
- **Unit Tests**: Comprehensive test coverage for core functionality

## API Endpoints

### GET /api/quote

Returns a random inspirational quote.

**Response (200 OK):**
```json
{
  "quote": "The only way to do great work is to love what you do. - Steve Jobs"
}
```

**Response (429 Too Many Requests):**
```json
{
  "error": "Rate limit exceeded. Try again in 45 seconds."
}
```

## Rate Limiting

- **Limit**: 5 requests per minute per IP address
- **Time Window**: 60 seconds
- **Storage**: In-memory using ConcurrentHashMap
- **Thread Safety**: Fully thread-safe implementation

## Setup Instructions

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Running the Application

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd LiveStockifyTask
   ```

2. **Build the project:**
   ```bash
   mvn clean compile
   ```

3. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

4. **The API will be available at:**
   ```
   http://localhost:8081
   ```

### Running Tests

```bash
mvn test
```

## Testing the API

### Using cURL

**Get a random quote:**
```bash
curl -X GET http://localhost:8081/api/quote
```

**Test rate limiting:**
```bash
# Make multiple requests quickly to test rate limiting
for i in {1..7}; do
  echo "Request $i:"
  curl -X GET http://localhost:8081/api/quote
  echo -e "\n"
done
```

### Using Postman

1. **Create a new GET request:**
   - URL: `http://localhost:8081/api/quote`
   - Method: GET

2. **Test rate limiting:**
   - Send the request multiple times quickly
   - After 5 requests, you should receive a 429 status code

## Design Decisions

### Rate Limiting Implementation

- **Chosen Approach**: Custom implementation using ConcurrentHashMap
- **Reasoning**: 
  - No external dependencies required
  - Full control over rate limiting logic
  - Thread-safe with ConcurrentHashMap
  - Automatic cleanup of expired entries
  - Lightweight and efficient

### IP Address Detection

- **Priority Order**:
  1. X-Forwarded-For header (for load balancers/proxies)
  2. X-Real-IP header (for reverse proxies)
  3. Remote address (direct connections)

### Logging Strategy

- **Interceptor-based**: Logs all requests and responses
- **Information Logged**:
  - Client IP address
  - HTTP method and URI
  - Response status code
  - Timestamp

### Data Storage

- **In-Memory**: No database required as per requirements
- **Quote Collection**: Hardcoded list of 20 inspirational quotes
- **Thread Safety**: All operations are thread-safe

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/liveStockify/LiveStockifyTask/
│   │       ├── LiveStockifyTaskApplication.java
│   │       ├── config/
│   │       │   ├── LoggingInterceptor.java
│   │       │   └── WebConfig.java
│   │       ├── controller/
│   │       │   └── QuoteController.java
│   │       ├── model/
│   │       │   ├── ErrorResponse.java
│   │       │   └── Quote.java
│   │       └── service/
│   │           ├── QuoteService.java
│   │           └── RateLimitingService.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/
        └── com/liveStockify/LiveStockifyTask/
            └── service/
                ├── QuoteServiceTest.java
                └── RateLimitingServiceTest.java
```

## Configuration

The application can be configured via `application.properties`:

```properties
# Server Configuration
server.port=8081

# Rate Limiting Configuration
app.rate-limit.max-requests=5
app.rate-limit.time-window-minutes=1

# Logging Configuration
logging.level.com.liveStockify.LiveStockifyTask=INFO
```

## Monitoring

The application logs all requests and responses. You can monitor the logs to see:

- Request patterns
- Rate limiting triggers
- Client IP addresses
- Response status codes

## Future Enhancements

- Add Swagger/OpenAPI documentation
- Implement configurable rate limits per endpoint
- Add metrics and monitoring endpoints
- Implement distributed rate limiting for multiple instances
- Add more quote categories and filtering options

## License

This project is part of the LiveStockify technical assessment.
