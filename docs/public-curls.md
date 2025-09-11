# Ticket Office Backend - Public API cURL Examples

## Authentication

### 1. User Signup
```bash
curl -X POST http://localhost:8080/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "password": "securePassword123",
    "confirmPassword": "securePassword123"
  }'
```

### 2. User Login
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com",
    "password": "securePassword123"
  }'
```

## Event Search

### 3. Search Events
```bash
curl -X GET "http://localhost:8080/api/public/v1/event/search?country=Argentina&city=BuenosAires&query=concierto&pageSize=9&pageNumber=1" \
  -H "Content-Type: application/json"
```

## Event Details

### 4. Get Event by ID
```bash
curl -X GET "http://localhost:8080/api/public/v1/event/123" \
  -H "Content-Type: application/json"
```

### 5. Get Event Recommendations
```bash
curl -X GET "http://localhost:8080/api/public/v1/event/123/recommendations" \
  -H "Content-Type: application/json"
```

## Checkout

### 6. Create Checkout Session
```bash
curl -X POST http://localhost:8080/api/public/v1/checkout/session \
  -H "Content-Type: application/json" \
  -d '{
    "eventId": "123",
    "tickets": [
      {
        "ticketTypeId": "456",
        "quantity": 2
      }
    ],
    "customerEmail": "customer@example.com"
  }'
```

### 7. Complete Purchase (Buy Tickets)
```bash
curl -X POST http://localhost:8080/api/public/v1/checkout/session/session_123/buy \
  -H "Content-Type: application/json" \
  -d '{
    "paymentMethod": "credit_card",
    "cardDetails": {
      "number": "4242424242424242",
      "expMonth": 12,
      "expYear": 2025,
      "cvc": "123"
    },
    "billingDetails": {
      "name": "John Doe",
      "email": "john.doe@example.com",
      "address": {
        "line1": "123 Main St",
        "city": "Buenos Aires",
        "postalCode": "1001",
        "country": "AR"
      }
    }
  }'
```

## Notes
- Replace placeholder values (like `123`, `456`, `session_123`, etc.) with actual values from your system.
- The CheckoutController's `/buy` endpoint returns a 204 No Content on success.
- The SearchPageController has default values for `pageSize` (9) and `pageNumber` (1) if not provided.
- All endpoints expect and return JSON content type.
