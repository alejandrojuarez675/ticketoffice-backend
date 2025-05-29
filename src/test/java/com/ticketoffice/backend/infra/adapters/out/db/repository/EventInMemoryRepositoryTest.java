package com.ticketoffice.backend.infra.adapters.out.db.repository;

import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.models.Location;
import com.ticketoffice.backend.domain.models.TicketPrice;
import com.ticketoffice.backend.domain.models.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class EventInMemoryRepositoryTest {

    private static final String TEST_CITY = "Bogotá";
    private static final String TEST_ORGANIZER_ID = "78cd80e2-023f-4b38-a409-d4a20f2d4ac7";

    private EventInMemoryRepository repository;

    @BeforeEach
    void setUp() {
        repository = new EventInMemoryRepository();
        repository.findAll().forEach(e -> repository.delete(e.id()));
    }

    @Test
    void search_shouldReturnPaginatedResults() {
        // Given
        // Add multiple test events
        for (int i = 0; i < 5; i++) {
            Event event = createTestEvent("Event " + i, TEST_CITY, TEST_ORGANIZER_ID);
            repository.save(event);
        }

        // Create a simple predicate that matches all events
        Predicate<Event> truePredicate = event -> event.title().matches("Event [0-9]");
        List<Predicate<Event>> predicates = List.of(truePredicate);

        // When - Request first page with 2 items
        int pageSize = 2;
        int pageNumber = 0; // 0-based page number
        List<Event> result = repository.search(predicates, pageSize, pageNumber);

        // Then
        assertEquals(pageSize, result.size(), "Should return exactly pageSize items");
    }

    @Test
    void search_shouldReturnEmptyList_whenPageNumberExceedsAvailablePages() {
        // Given
        for (int i = 0; i < 10; i++) {
            Event event = createTestEvent("Event " + i, TEST_CITY, TEST_ORGANIZER_ID);
            repository.save(event);
        }

        Predicate<Event> truePredicate = event -> event.title().matches("Event [0-9]");
        List<Predicate<Event>> predicates = List.of(truePredicate);

        // When - Request page 2 with pageSize 3 when there are only 3 items total
        List<Event> result = repository.search(predicates, 6, 3);

        // Then
        assertTrue(result.isEmpty(), "Should return empty list when page number exceeds available pages");
    }


    @Test
    void search_shouldReturnAllItems_whenPageSizeIsLargerThanTotalItems() {
        // Given
        int totalItems = 3;
        for (int i = 0; i < totalItems; i++) {
            Event event = createTestEvent("Event " + i, TEST_CITY, TEST_ORGANIZER_ID);
            repository.save(event);
        }

        Predicate<Event> truePredicate = event -> event.title().matches("Event [0-9]");
        List<Predicate<Event>> predicates = List.of(truePredicate);

        // When - Request more items than exist
        List<Event> result = repository.search(predicates, totalItems + 10, 0);

        // Then
        assertEquals(totalItems, result.size(), "Should return all items when pageSize > total items");
    }

    @Test
    void search_shouldReturnEmptyList_whenNoItemsMatchPredicate() {
        // Given
        for (int i = 0; i < 3; i++) {
            Event event = createTestEvent("Event " + i, TEST_CITY, TEST_ORGANIZER_ID);
            repository.save(event);
        }
        // Predicate that won't match any events
        Predicate<Event> falsePredicate = event -> false;
        List<Predicate<Event>> predicates = List.of(falsePredicate);

        // When
        List<Event> result = repository.search(predicates, 10, 0);

        // Then
        assertTrue(result.isEmpty(), "Should return empty list when no items match the predicate");
    }


    @Test
    void search_shouldHandleEmptyRepository() {
        // Given
        Predicate<Event> truePredicate = event -> event.title().matches("Event [0-9]");
        List<Predicate<Event>> predicates = List.of(truePredicate);

        // When
        List<Event> result = repository.search(predicates, 10, 0);

        // Then
        assertTrue(result.isEmpty(), "Should return empty list when repository is empty");
    }

    private Event createTestEvent(String title, String city, String organizerId) {
        return new Event(
                java.util.UUID.randomUUID().toString(),
                title,
                LocalDateTime.now().plusDays(1),
                new Location("1", "Test Venue", "Test Address", city),
                new Image("1", "http://test.com/image.jpg", "Test Image"),
                List.of(
                        new TicketPrice(
                                java.util.UUID.randomUUID().toString(),
                                100.0,
                                "$",
                                "General",
                                false
                        )
                ),
                "Test Description",
                List.of("Test Info"),
                organizerId,
                null
        );
    }
}
