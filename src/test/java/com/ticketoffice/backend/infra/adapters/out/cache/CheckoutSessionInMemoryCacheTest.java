package com.ticketoffice.backend.infra.adapters.out.cache;

import static org.junit.jupiter.api.Assertions.*;

import com.ticketoffice.backend.application.utils.CheckoutSessionIdUtils;
import com.ticketoffice.backend.domain.models.CheckoutSession;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CheckoutSessionInMemoryCacheTest {

    private CheckoutSessionInMemoryCache cache;
    private CheckoutSession testSession;
    private final String EVENT_ID = "event1";
    private final String TICKET_ID = "ticket1";
    private final int QUANTITY = 2;
    private final String SESSION_ID = CheckoutSessionIdUtils.createCheckoutSessionId(EVENT_ID, TICKET_ID, QUANTITY);

    @BeforeEach
    void setUp() {
        cache = new CheckoutSessionInMemoryCache();

        double PRICE = 100.0;
        testSession = new CheckoutSession(
                SESSION_ID,
                EVENT_ID,
                TICKET_ID,
                QUANTITY,
                PRICE,
                CheckoutSession.Status.CONFIRMING,
                LocalDateTime.now().plusHours(1)
        );
        // Clear the static map before each test
        cache.deleteById(SESSION_ID);
    }

    @Test
    void createCheckoutSession_shouldAddSessionToCache() {
        // When
        Optional<CheckoutSession> result = cache.createCheckoutSession(testSession);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testSession, result.get());
        assertTrue(cache.getById(SESSION_ID).isPresent());
    }

    @Test
    void getById_shouldReturnSession_whenSessionExists() {
        // Given
        cache.createCheckoutSession(testSession);

        // When
        Optional<CheckoutSession> result = cache.getById(SESSION_ID);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testSession, result.get());
    }

    @Test
    void getById_shouldReturnEmpty_whenSessionDoesNotExist() {
        // When
        Optional<CheckoutSession> result = cache.getById("non_existent_id");

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void countByEventIdAndTicketId_shouldReturnCorrectCount() {
        // Given
        CheckoutSession session1 = new CheckoutSession(
                CheckoutSessionIdUtils.createCheckoutSessionId("event1", "ticket1", 2), "event1", "ticket1", 2, 100.0,
                CheckoutSession.Status.CREATED,
                LocalDateTime.now().plusHours(1)
        );
        CheckoutSession session2 = new CheckoutSession(
                CheckoutSessionIdUtils.createCheckoutSessionId("event1", "ticket1", 3), "event1", "ticket1", 3, 150.0, CheckoutSession.Status.CREATED,
                LocalDateTime.now().plusHours(1)
        );
        CheckoutSession otherSession = new CheckoutSession(
                CheckoutSessionIdUtils.createCheckoutSessionId("event2", "ticket1", 1), "event2", "ticket1", 1, 200.0, CheckoutSession.Status.CREATED,
                LocalDateTime.now().plusHours(1)
        );

        cache.createCheckoutSession(session1);
        cache.createCheckoutSession(session2);
        cache.createCheckoutSession(otherSession);

        // When
        int count = cache.countByEventIdAndTicketId("event1", "ticket1");

        // Then
        assertEquals(5, count); // 2 + 3 = 5 (sum of quantities)
    }

    @Test
    void countByEventIdAndTicketId_shouldReturnZero_whenNoMatchingSessions() {
        // When
        int count = cache.countByEventIdAndTicketId("non_existent_event", "non_existent_ticket");

        // Then
        assertEquals(0, count);
    }

    @Test
    void deleteById_shouldRemoveSession() {
        // Given
        cache.createCheckoutSession(testSession);
        assertTrue(cache.getById(SESSION_ID).isPresent());

        // When
        cache.deleteById(SESSION_ID);

        // Then
        assertTrue(cache.getById(SESSION_ID).isEmpty());
    }

    @Test
    void updateStatus_shouldUpdateSessionStatus() {
        // Given
        cache.createCheckoutSession(testSession);

        // When
        Optional<CheckoutSession> updated = cache.updateStatus(SESSION_ID, CheckoutSession.Status.CREATED);

        // Then
        assertTrue(updated.isPresent());
        assertEquals(CheckoutSession.Status.CREATED, updated.get().getStatus());
        assertTrue(cache.getById(SESSION_ID).isPresent());
        assertEquals(CheckoutSession.Status.CREATED, cache.getById(SESSION_ID).get().getStatus());
    }

    @Test
    void updateStatus_shouldReturnEmpty_whenSessionDoesNotExist() {
        // When
        Optional<CheckoutSession> result = cache.updateStatus("non_existent_id", CheckoutSession.Status.CREATED);

        // Then
        assertTrue(result.isEmpty());
    }
}
