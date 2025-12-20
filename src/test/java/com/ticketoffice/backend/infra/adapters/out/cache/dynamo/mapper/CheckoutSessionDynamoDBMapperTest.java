package com.ticketoffice.backend.infra.adapters.out.cache.dynamo.mapper;

import com.ticketoffice.backend.domain.models.CheckoutSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CheckoutSessionDynamoDBMapperTest {

    @Test
    void toMapAndFromMap_shouldCorrectlyMapCheckoutSession() {
        // Arrange
        String id = UUID.randomUUID().toString();
        String eventId = "event-123";
        String priceId = "price-456";
        int quantity = 2;
        double price = 99.99;
        LocalDateTime expirationTime = LocalDateTime.now().plusHours(1);
        
        CheckoutSession session = new CheckoutSession(
            id,
            eventId,
            priceId,
            quantity,
            price,
            CheckoutSession.Status.CREATED,
            expirationTime
        );

        // Act - Convert to map and back
        Map<String, AttributeValue> attributeMap = CheckoutSessionDynamoDBMapper.toMap(session);
        CheckoutSession result = CheckoutSessionDynamoDBMapper.fromMap(attributeMap);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(eventId, result.getEventId());
        assertEquals(priceId, result.getPriceId());
        assertEquals(quantity, result.getQuantity());
        assertEquals(price, result.getPrice());
        assertEquals(CheckoutSession.Status.CREATED, result.getStatus());
        assertNotNull(result.getExpirationTime());
        assertEquals(expirationTime, result.getExpirationTime());
    }

    @Test
    void fromMap_shouldHandleMissingOptionalFields() {
        // Arrange
        String id = UUID.randomUUID().toString();
        Map<String, AttributeValue> attributeMap = new HashMap<>();
        attributeMap.put("id", AttributeValue.builder().s(id).build());
        attributeMap.put("status", AttributeValue.builder().s("CREATED").build());
        attributeMap.put("eventId", AttributeValue.builder().s("event-123").build());
        attributeMap.put("priceId", AttributeValue.builder().s("price-456").build());
        attributeMap.put("quantity", AttributeValue.builder().n("2").build());
        attributeMap.put("price", AttributeValue.builder().n("99.99").build());
        
        // Act
        CheckoutSession result = CheckoutSessionDynamoDBMapper.fromMap(attributeMap);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("event-123", result.getEventId());
        assertEquals("price-456", result.getPriceId());
        assertEquals(2, result.getQuantity());
        assertEquals(99.99, result.getPrice());
        assertEquals(CheckoutSession.Status.CREATED, result.getStatus());
    }

    @Test
    void toMap_shouldHandleNullValues() {
        // Arrange
        CheckoutSession session = new CheckoutSession(
            "test-id",
            null,  // eventId
            null,  // priceId
            0,     // quantity
            0.0,   // price
            CheckoutSession.Status.CREATED,
            null   // expirationTime
        );

        // Act
        Map<String, AttributeValue> result = CheckoutSessionDynamoDBMapper.toMap(session);

        // Assert
        assertNotNull(result);
        assertEquals("test-id", result.get("id").s());
        assertEquals("CREATED", result.get("status").s());
        assertTrue(result.containsKey("eventId"));
        assertTrue(result.containsKey("priceId"));
        assertTrue(result.containsKey("quantity"));
        assertTrue(result.containsKey("price"));
        assertFalse(result.containsKey("expirationTime"));
    }

    @Test
    void toMap_shouldConvertAllFields() {
        // Arrange
        String id = UUID.randomUUID().toString();
        String eventId = "event-123";
        String priceId = "price-456";
        int quantity = 3;
        double price = 149.99;
        LocalDateTime expirationTime = LocalDateTime.now().plusHours(1);
        
        CheckoutSession session = new CheckoutSession(
            id,
            eventId,
            priceId,
            quantity,
            price,
            CheckoutSession.Status.CONFIRMING,
            expirationTime
        );

        // Act
        Map<String, AttributeValue> result = CheckoutSessionDynamoDBMapper.toMap(session);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.get("id").s());
        assertEquals(eventId, result.get("eventId").s());
        assertEquals(priceId, result.get("priceId").s());
        assertEquals(String.valueOf(quantity), result.get("quantity").n());
        assertEquals(String.valueOf(price), result.get("price").n());
        assertEquals("CONFIRMING", result.get("status").s());
        
        // Verify expiration time is set correctly
        assertTrue(result.containsKey("expirationTime"));
        assertTrue(result.containsKey("ttl"));
        LocalDateTime parsedExpiration = LocalDateTime.parse(
            result.get("expirationTime").s(), 
            CheckoutSessionDynamoDBMapper.DATE_TIME_FORMATTER
        );
        assertEquals(expirationTime.getYear(), parsedExpiration.getYear());
        assertEquals(expirationTime.getMonth(), parsedExpiration.getMonth());
        assertEquals(expirationTime.getDayOfMonth(), parsedExpiration.getDayOfMonth());
        assertEquals(expirationTime.getHour(), parsedExpiration.getHour());
    }
}
