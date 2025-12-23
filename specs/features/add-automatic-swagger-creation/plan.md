# Plan for Adding Automatic Swagger Documentation

## Overview
This document outlines the plan to implement automatic Swagger/OpenAPI documentation for the TicketOffice backend API.

## Current State Analysis
- The application uses Javalin framework
- Endpoints are organized in controller classes
- No current API documentation is automatically generated

## Identified Endpoints

### 1. Public Endpoints
- `GET /ping` - Basic health check
- `GET /health` - Health check with status
- `POST /auth/login` - User authentication
- `GET /api/public/v1/event/search` - Search events
- `GET /api/public/v1/event/{id}` - Get event details
- `POST /api/public/v1/checkout` - Process checkout

### 2. Authenticated Endpoints
- `GET /api/v1/events` - List all events
- `GET /api/v1/events/{id}` - Get event by ID
- `PUT /api/v1/events/{id}` - Update event
- `DELETE /api/v1/events/{id}` - Delete event
- `GET /api/v1/sales` - List sales
- `GET /api/v1/sales/{id}` - Get sale by ID
- `PUT /api/v1/sales/{id}` - Update sale
- `GET /api/v1/users` - List users
- `GET /api/v1/users/{id}` - Get user by ID

### 3. Regionalization Endpoints
- `GET /api/v1/regionalization/countries` - Available countries
- `GET /api/v1/regionalization/countries/{countryCode}` - Country config
- `GET /api/v1/regionalization/countries/{countryCode}/cities` - Cities by country
- `GET /api/v1/regionalization/currencies` - Available currencies
- `GET /api/v1/regionalization/document-types` - Document types

## Implementation Steps

### 1. Add Dependencies
Add the following to `build.gradle`:
```gradle
dependencies {
    // Swagger/OpenAPI
    implementation 'io.swagger.core.v3:swagger-core:2.2.8'
    implementation 'io.swagger.core.v3:swagger-models:2.2.8'
    implementation 'io.swagger.core.v3:swagger-annotations:2.2.8'
    implementation 'io.swagger.core.v3:swagger-jaxrs2:2.2.8'
    
    // Javalin OpenAPI integration
    implementation 'io.javalin:javalin-openapi:5.6.1'
}
```

### 2. Configure OpenAPI
Create a configuration class for OpenAPI:
```java
@OpenAPIDefinition(
    info = @Info(
        title = "TicketOffice API",
        version = "1.0",
        description = "API documentation for TicketOffice backend",
        contact = @Contact(
            name = "Support",
            email = "support@ticketoffice.com"
        )
    ),
    security = @SecurityRequirement(name = "bearerAuth")
)
public class OpenApiConfig {
    // Configuration class for OpenAPI
}
```

### 3. Document Controllers
Add OpenAPI annotations to controller methods. Example:
```java
@OpenApi(
    path = "/api/v1/events/{id}",
    methods = {HttpMethod.GET},
    summary = "Get event by ID",
    description = "Retrieves detailed information about a specific event",
    pathParams = @OpenApiParam(name = "id", description = "Event ID", required = true),
    responses = {
        @OpenApiResponse(status = "200", content = @OpenApiContent(from = EventResponse.class)),
        @OpenApiResponse(status = "404", description = "Event not found"),
        @OpenApiResponse(status = "401", description = "Unauthorized")
    },
    security = @OpenApiSecurity(name = "bearerAuth")
)
public void getEventById(Context ctx) {
    // Existing implementation
}
```

### 4. Configure Security Scheme
Add JWT security scheme:
```java
@OpenAPIDefinition(
    // ... existing config ...
    components = @Components(
        securitySchemes = @SecurityScheme(
            name = "bearerAuth",
            type = SecuritySchemeType.HTTP,
            scheme = "bearer",
            bearerFormat = "JWT"
        )
    )
)
```

### 5. Add Swagger UI
Configure Swagger UI endpoint:
```java
OpenApiUIHandler openApiUIHandler = new OpenApiUIHandler(
    new OpenAPIConfiguration()
        .withOpenApiInfo(new Info()
            .title("TicketOffice API")
            .version("1.0")
            .description("API documentation for TicketOffice backend")
        )
        .withServer("http://localhost:8080", "Local development")
);

app.get("/swagger", openApiUIHandler);
app.get("/openapi.json", ctx -> ctx.json(OpenApiHandler.getOpenApiSpec()));
```

### 6. Document Models
Add schema annotations to DTOs:
```java
@Schema(description = "Event information")
public class EventResponse {
    @Schema(description = "Unique identifier of the event", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;
    
    @Schema(description = "Name of the event", example = "Concierto de Rock")
    private String name;
    
    @Schema(description = "Description of the event", example = "Gran concierto de rock con artistas internacionales")
    private String description;
    
    // Other fields with appropriate annotations
}
```

## Testing Plan
1. Verify all endpoints are documented
2. Test authentication flow in Swagger UI
3. Validate request/response schemas
4. Test error responses
5. Verify security requirements

## Deployment
- Include Swagger UI only in non-production environments
- Configure proper CORS for Swagger UI
- Set up API documentation versioning

## Maintenance
- Update documentation with new endpoints
- Keep request/response models in sync
- Review and update examples regularly
- Monitor API documentation usage