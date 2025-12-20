package com.ticketoffice.backend.infra.adapters.out.cache.dynamo;

import com.ticketoffice.backend.domain.models.CheckoutSession;
import com.ticketoffice.backend.infra.adapters.out.cache.dynamo.dao.CheckoutSessionDynamoDao;
import com.ticketoffice.backend.infra.adapters.out.cache.dynamo.mapper.CheckoutSessionDynamoDBMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckoutSessionDynamoRepositoryTest {

    @Mock
    private CheckoutSessionDynamoDao dao;

    private CheckoutSessionDynamoRepository repository;
    private CheckoutSession testSession;

    @BeforeEach
    void setUp() {
        repository = new CheckoutSessionDynamoRepository(dao);
        testSession = new CheckoutSession(
            UUID.randomUUID().toString(),
            "event-123",
            "ticket-456",
            2,
            100.0, // price
            CheckoutSession.Status.CREATED,
            LocalDateTime.now().plusHours(1) // expirationTime
        );
    }

    @Test
    void getById_shouldReturnSession_whenExists() {
        // Arrange
        Map<String, AttributeValue> item = CheckoutSessionDynamoDBMapper.toMap(testSession);
        when(dao.getById(testSession.getId())).thenReturn(item);

        // Act
        Optional<CheckoutSession> result = repository.getById(testSession.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testSession.getId(), result.get().getId());
        assertEquals(testSession.getEventId(), result.get().getEventId());
        assertEquals(testSession.getPriceId(), result.get().getPriceId());
        assertEquals(testSession.getQuantity(), result.get().getQuantity());
        assertEquals(testSession.getStatus(), result.get().getStatus());
        verify(dao).getById(testSession.getId());
    }

    @Test
    void getById_shouldReturnEmpty_whenNotFound() {
        // Arrange
        when(dao.getById(anyString())).thenReturn(null);

        // Act
        Optional<CheckoutSession> result = repository.getById("non-existent-id");

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void createCheckoutSession_shouldSaveAndReturnSession() {
        // Arrange
        Map<String, AttributeValue> item = CheckoutSessionDynamoDBMapper.toMap(testSession);
        when(dao.getById(testSession.getId())).thenReturn(item);
        doNothing().when(dao).save(any()); // Mock the void save operation

        // Act
        Optional<CheckoutSession> result = repository.createCheckoutSession(testSession);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testSession.getId(), result.get().getId());
        assertEquals(testSession.getEventId(), result.get().getEventId());
        assertEquals(testSession.getPriceId(), result.get().getPriceId());
        assertEquals(testSession.getQuantity(), result.get().getQuantity());
        assertEquals(testSession.getStatus(), result.get().getStatus());
        verify(dao).save(any());
        verify(dao).getById(testSession.getId());
    }

    @Test
    void updateStatus_shouldUpdateAndReturnSession() {
        // Arrange
        Map<String, AttributeValue> item = CheckoutSessionDynamoDBMapper.toMap(testSession);
        when(dao.getById(testSession.getId())).thenReturn(item);
        doNothing().when(dao).save(any()); // Mock the void save operation

        // Act
        Optional<CheckoutSession> result = repository.updateStatus(
            testSession.getId(), 
            CheckoutSession.Status.CONFIRMING
        );

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testSession.getId(), result.get().getId());
        assertEquals(testSession.getEventId(), result.get().getEventId());
        assertEquals(testSession.getPriceId(), result.get().getPriceId());
        verify(dao).save(any());
        verify(dao, times(2)).getById(testSession.getId());
    }

    @Test
    void updateStatus_shouldReturnEmpty_whenSessionNotFound() {
        // Arrange
        when(dao.getById(anyString())).thenReturn(null);

        // Act
        Optional<CheckoutSession> result = repository.updateStatus(
            "non-existent-id", 
            CheckoutSession.Status.CREATED
        );

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void deleteById_shouldCallDaoDelete() {
        // Arrange
        String sessionId = "test-session-id";
        
        // Act
        repository.deleteById(sessionId);
        
        // Assert
        verify(dao).deleteById(sessionId);
    }

    @Test
    void countByEventIdAndTicketId_shouldReturnCount() {
        // Arrange
        String eventId = "event-123";
        String ticketId = "ticket-456";
        int expectedCount = 5;
        when(dao.countByEventIdAndTicketId(eventId, ticketId)).thenReturn(expectedCount);

        // Act
        int result = repository.countByEventIdAndTicketId(eventId, ticketId);

        // Assert
        assertEquals(expectedCount, result);
        verify(dao).countByEventIdAndTicketId(eventId, ticketId);
    }
}
