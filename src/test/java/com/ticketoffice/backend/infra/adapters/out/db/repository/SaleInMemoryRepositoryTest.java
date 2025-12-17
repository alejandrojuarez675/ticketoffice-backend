package com.ticketoffice.backend.infra.adapters.out.db.repository;

import com.ticketoffice.backend.domain.models.Buyer;
import com.ticketoffice.backend.domain.models.Sale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SaleInMemoryRepositoryTest {

    private SaleInMemoryRepository repository;
    private Sale testSale;
    private final String testSaleId = UUID.randomUUID().toString();
    private final String testEventId = "event-123";
    private final String testTicketId = "ticket-456";

    @BeforeEach
    void setUp() {
        repository = new SaleInMemoryRepository();
        
        // Clear the repository before each test
        repository.findAll().forEach(sale -> repository.delete(sale.id()));
        
        // Create a test sale
        testSale = new Sale(
            testSaleId,
            testEventId,
            testTicketId,
            2,
            199.99,
            List.of(new Buyer(
                "buyer-123",
                "John",
                "Doe",
                "john.doe@example.com",
                "+1234567890",
                "US",
                "PASSPORT",
                "AB123456"
            )),
            "john.doe@example.com",
            true
        );
    }

    @Test
    void save_shouldSaveSaleAndReturnIt() {
        // When
        Optional<Sale> result = repository.save(testSale);
        
        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testSale);
        assertThat(repository.findAll()).hasSize(1);
    }

    @Test
    void save_shouldGenerateIdWhenNotProvided() {
        // Given
        Sale saleWithoutId = new Sale(
            null,
            testEventId,
            testTicketId,
            1,
            99.99,
            List.of(new Buyer(
                "buyer-456",
                "Jane",
                "Doe",
                "jane.doe@example.com",
                "+1987654321",
                "US",
                "PASSPORT",
                "CD789012"
            )),
            "jane.doe@example.com",
            false
        );

        // When
        Optional<Sale> result = repository.save(saleWithoutId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().id()).isNotBlank();
        assertThat(repository.findAll()).hasSize(1);
    }

    @Test
    void getById_shouldReturnSaleWhenFound() {
        // Given
        repository.save(testSale);

        // When
        Optional<Sale> result = repository.getById(testSaleId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testSale);
    }

    @Test
    void getById_shouldReturnEmptyWhenNotFound() {
        // When
        Optional<Sale> result = repository.getById("non-existent-id");

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void update_shouldUpdateExistingSale() {
        // Given
        repository.save(testSale);
        Sale updatedSale = new Sale(
            testSaleId,
            testEventId,
            "updated-ticket-id",
            3,
            249.99,
            testSale.buyer(),
            "updated.email@example.com",
            false
        );

        // When
        Optional<Sale> result = repository.update(testSaleId, updatedSale);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().ticketId()).isEqualTo("updated-ticket-id");
        assertThat(result.get().quantity()).isEqualTo(3);
        assertThat(result.get().price()).isEqualTo(249.99);
        assertThat(result.get().mainEmail()).isEqualTo("updated.email@example.com");
        assertThat(result.get().validated()).isFalse();
    }

    @Test
    void update_shouldReturnEmptyWhenSaleNotFound() {
        // Given
        Sale updatedSale = new Sale(
            "non-existent-id",
            testEventId,
            testTicketId,
            1,
            99.99,
            testSale.buyer(),
            "updated@example.com",
            true
        );

        // When
        Optional<Sale> result = repository.update("non-existent-id", updatedSale);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void countByEventIdAndTicketId_shouldReturnCorrectCount() {
        // Given
        repository.save(testSale);
        
        // Add more test data
        repository.save(createTestSale("sale-2", testEventId, testTicketId));
        repository.save(createTestSale("sale-3", testEventId, "different-ticket"));
        repository.save(createTestSale("sale-4", "different-event", testTicketId));

        // When
        int countSameEventAndTicket = repository.countByEventIdAndTicketId(testEventId, testTicketId);
        int countSameEventAnyTicket = repository.countByEventIdAndTicketId(testEventId, null);
        int countDifferentEvent = repository.countByEventIdAndTicketId("non-existent-event", testTicketId);

        // Then
        assertThat(countSameEventAndTicket).isEqualTo(2);
        assertThat(countSameEventAnyTicket).isEqualTo(3);
        assertThat(countDifferentEvent).isZero();
    }

    @Test
    void findByEventId_shouldReturnSalesForEvent() {
        // Given
        repository.save(testSale);
        repository.save(createTestSale("sale-2", testEventId, "ticket-789"));
        repository.save(createTestSale("sale-3", "different-event", "ticket-123"));

        // When
        List<Sale> results = repository.findByEventId(testEventId);

        // Then
        assertThat(results).hasSize(2);
        assertThat(results).allMatch(sale -> sale.eventId().equals(testEventId));
    }

    @Test
    void delete_shouldRemoveSale() {
        // Given
        repository.save(testSale);
        assertThat(repository.getById(testSaleId)).isPresent();

        // When
        repository.delete(testSaleId);

        // Then
        assertThat(repository.getById(testSaleId)).isEmpty();
    }

    private Sale createTestSale(String id, String eventId, String ticketId) {
        return new Sale(
            id,
            eventId,
            ticketId,
            1,
            99.99,
            List.of(new Buyer(
                "buyer-" + UUID.randomUUID().toString().substring(0, 8),
                "Test",
                "User",
                "test@example.com",
                "+1234567890",
                "US",
                "PASSPORT",
                "XX123456"
            )),
            "test@example.com",
            true
        );
    }
}
