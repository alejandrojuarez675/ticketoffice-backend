# DynamoDB Integration Guide

## Overview
This document provides a comprehensive guide to the DynamoDB integration in the Ticket Office Backend. The integration allows the application to use Amazon DynamoDB as a persistent data store for managing sales, events, and user data.

## Table of Contents
- [Architecture](#architecture)
- [Data Model](#data-model)
- [Implementation Details](#implementation-details)
- [Configuration](#configuration)
- [Usage](#usage)
- [Testing](#testing)
- [Deployment](#deployment)

## Architecture

The DynamoDB integration follows a layered architecture with clear separation of concerns:

1. **Repository Layer**: Implements the repository interfaces using DynamoDB
2. **DAO Layer**: Handles low-level DynamoDB operations
3. **Mapper Layer**: Converts between domain models and DynamoDB items
4. **Domain Layer**: Contains the core business logic and interfaces

### Key Components

- **`SaleDynamoRepository`**: Implements the `SaleRepository` interface for DynamoDB
- **`SaleDynamoDao`**: Handles DynamoDB operations for sales
- **`SaleDynamoDBMapper`**: Maps between `Sale` domain objects and DynamoDB items
- **`AbstractDynamoDao`**: Base class providing common DynamoDB operations

## Data Model

The following tables are used in the DynamoDB integration:

### Sales Table
- **Table Name**: `ticketoffice-sales`
- **Partition Key**: `id` (String)
- **GSI1**: `eventId-index` (for querying sales by event)

### Events Table
- **Table Name**: `ticketoffice-events`
- **Partition Key**: `id` (String)

### Users Table
- **Table Name**: `ticketoffice-users`
- **Partition Key**: `id` (String)

## Implementation Details

### Repository Implementation

The `SaleDynamoRepository` implements the `SaleRepository` interface and provides the following operations:

- Save a sale
- Find sales by event ID
- Count sales by event and ticket ID
- Get a sale by ID
- Update a sale

### DAO Layer

The `SaleDynamoDao` extends `AbstractDynamoDao` and provides DynamoDB-specific implementations for:

- CRUD operations
- Query operations using GSIs
- Batch operations

### Mapper Layer

The `SaleDynamoDBMapper` handles the conversion between domain models and DynamoDB items, including:

- Attribute value mapping
- Nested object serialization/deserialization
- Data type conversion

## Configuration

### AWS Credentials

Configure AWS credentials using one of the following methods:

1. **Environment Variables**:
   ```bash
   export AWS_ACCESS_KEY_ID=your_access_key
   export AWS_SECRET_ACCESS_KEY=your_secret_key
   export AWS_REGION=your_region
   ```

2. **AWS Credentials File** (`~/.aws/credentials`):
   ```ini
   [default]
   aws_access_key_id=your_access_key
   aws_secret_access_key=your_secret_key
   region=your_region
   ```

### Application Properties

Configure the following properties in `application.yml`:

```yaml
aws:
  dynamodb:
    endpoint: ${DYNAMODB_ENDPOINT:}  # Leave empty for production
    region: ${AWS_REGION:us-east-1}
    table-prefix: ${DYNAMODB_TABLE_PREFIX:}
```

## Usage

### Saving a Sale

```java
@Autowired
private SaleRepository saleRepository;

public void createSale(Sale sale) {
    saleRepository.save(sale);
}
```

### Finding Sales by Event ID

```java
@Autowired
private SaleRepository saleRepository;

public List<Sale> getSalesByEvent(String eventId) {
    return saleRepository.findByEventId(eventId);
}
```

### Counting Sales

```java
@Autowired
private SaleRepository saleRepository;

public int countSales(String eventId, String ticketId) {
    return saleRepository.countByEventIdAndTicketId(eventId, ticketId);
}
```

## Testing

The integration includes comprehensive test coverage:

### Unit Tests
- Test mappers and converters
- Test repository methods with mocks

### Integration Tests
- Test against a local DynamoDB instance
- Test CRUD operations
- Test query operations

To run the tests:

```bash
./gradlew test
```

## Deployment

### Infrastructure as Code

The DynamoDB tables should be created using AWS CloudFormation or Terraform. Example CloudFormation template:

```yaml
Resources:
  SalesTable:
    Type: AWS::DynamoDB::Table
    Properties:
      TableName: ticketoffice-sales
      AttributeDefinitions:
        - AttributeName: id
          AttributeType: S
        - AttributeName: eventId
          AttributeType: S
      KeySchema:
        - AttributeName: id
          KeyType: HASH
      BillingMode: PAY_PER_REQUEST
      GlobalSecondaryIndexes:
        - IndexName: eventId-index
          KeySchema:
            - AttributeName: eventId
              KeyType: HASH
          Projection:
            ProjectionType: ALL
```

### Performance Considerations

- **Partition Keys**: Choose partition keys that distribute read/write operations evenly
- **GSIs**: Use GSIs for frequently accessed query patterns
- **Batch Operations**: Use batch operations for bulk operations
- **Pagination**: Implement pagination for large result sets

## Monitoring and Maintenance

### CloudWatch Alarms
Set up CloudWatch alarms for:
- Throttled requests
- Consumed capacity
- System errors

### Backup and Restore
- Enable point-in-time recovery for critical tables
- Implement regular backup procedures

## Troubleshooting

### Common Issues

1. **ProvisionedThroughputExceededException**
   - Increase provisioned throughput
   - Implement exponential backoff
   
2. **ValidationException**
   - Check attribute types
   - Verify required attributes are provided

3. **ResourceNotFoundException**
   - Verify table name and region
   - Check IAM permissions

### Logging

Enable debug logging for AWS SDK:

```yaml
logging:
  level:
    com.amazonaws: DEBUG
    com.ticketoffice.backend.infra.adapters.out.db: DEBUG
```

## Best Practices

1. **Design for Scale**
   - Use appropriate partition keys
   - Design for even data distribution
   
2. **Optimize Queries**
   - Use GSIs for common query patterns
   - Avoid scans when possible
   
3. **Error Handling**
   - Implement retries with backoff
   - Handle throttling gracefully
   
4. **Security**
   - Use IAM roles and policies
   - Encrypt sensitive data
   
5. **Cost Optimization**
   - Use on-demand capacity for unpredictable workloads
   - Monitor and adjust provisioned capacity as needed
