# Implementation Plan: Amazon SES Email Client

**Date**: 2023-12-13  
**Spec**: [add-mailing-client](../add-mailing-client/spec.md)

## Summary
Implement a transactional email client using Amazon SES with native templates, following hexagonal architecture and enabling asynchronous email sending.

## Technical Context

**Language/Version**: Java 21  
**Primary Dependencies**: 
- Javalin (HTTP adapter)
- Google Guice (Dependency Injection)
- AWS SDK for Java v2 (SES)

**Target Platform**: AWS EC2 (Linux)  
**Project Type**: Backend service  
**Performance Goals**: 
- Asynchronous email sending to avoid blocking HTTP requests
- Handle up to 100 emails per second

**Constraints**:
- Must use native SES templates (no inline HTML)
- Must follow hexagonal architecture
- Must support IAM Role authentication

## Project Structure

### Documentation (this feature)

```text
specs/features/add-mailing-client/
├── plan.md              # This file 
└── spec.md              # Feature specifications
```

### Source Code

```text
src/main/java/com/ticketoffice/
├── core/
│   ├── ports/
│   │   └── MailSenderPort.java
│   └── model/
│       └── MailMessage.java
└── infrastructure/
    └── aws/
        └── ses/
            ├── SesMailSenderAdapter.java
            └── config/
                └── SesConfig.java

test/
└── java/com/ticketoffice/
    └── infrastructure/aws/ses/
        └── SesMailSenderAdapterTest.java
```

**Structure Decision**: Follows hexagonal architecture with clear separation between domain and infrastructure layers.

## Implementation Phases

### Phase 1: Core Domain

- [ ] T001 Create `MailMessage` domain model
- [ ] T002 Define `MailSenderPort` interface
- [ ] T003 Add domain exceptions for email sending

### Phase 2: Infrastructure

- [ ] T004 Implement `SesMailSenderAdapter`
- [ ] T005 Configure AWS SDK v2 SES client
- [ ] T006 Add IAM role authentication
- [ ] T007 Implement template-based email sending
- [ ] T008 Add async processing support

### Phase 3: Configuration

- [ ] T009 Add environment variables configuration
- [ ] T010 Setup Google Guice bindings
- [ ] T011 Configure logging and error handling

## Environment Variables

```
# Required
AWS_REGION=us-east-1
AWS_SES_SENDER=no-reply@example.com

# Optional (with defaults)
AWS_SES_ENDPOINT_OVERRIDE=  # For local development
LOG_LEVEL=INFO
```

## Testing Strategy

### Unit Tests
- [ ] T101 Test `SesMailSenderAdapter` with AWS SDK mocks
- [ ] T102 Test template variable substitution
- [ ] T103 Test error handling and retries

### Integration Tests
- [ ] T201 Test with SES sandbox environment
- [ ] T202 Verify IAM permissions
- [ ] T203 Test async processing

## Deployment

### Prerequisites
- [ ] D1 Verify IAM role has `ses:SendTemplatedEmail` permission
- [ ] D2 Create required SES templates in AWS Console
- [ ] D3 Verify sender email/domain in SES

### Deployment Steps
1. Update IAM policies for EC2 role
2. Set environment variables
3. Deploy application
4. Verify email sending with test cases

## Rollback Plan
1. Revert to previous version
2. Verify rollback with health checks
3. Monitor error logs for any issues

## Monitoring and Maintenance
- CloudWatch metrics for email sending
- Logging of all email operations
- Alerting for failed sends