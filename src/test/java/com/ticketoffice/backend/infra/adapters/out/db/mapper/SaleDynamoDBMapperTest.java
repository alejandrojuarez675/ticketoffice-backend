package com.ticketoffice.backend.infra.adapters.out.db.mapper;

import com.ticketoffice.backend.domain.models.Buyer;
import com.ticketoffice.backend.domain.models.Sale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

class SaleDynamoDBMapperTest {

    private SaleDynamoDBMapper mapper;
    private Sale testSale;
    private final String testSaleId = UUID.randomUUID().toString();
    private final String testEventId = "event-123";
    private final String testTicketId = "ticket-456";

    @BeforeEach
    void setUp() {
        mapper = new SaleDynamoDBMapper();
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
    void toDynamoDB_shouldMapAllFields() {
        // When
        Map<String, AttributeValue> result = mapper.toDynamoDB(testSale);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.get("id").s()).isEqualTo(testSaleId);
        assertThat(result.get("eventId").s()).isEqualTo(testEventId);
        assertThat(result.get("ticketId").s()).isEqualTo(testTicketId);
        assertThat(Integer.parseInt(result.get("quantity").n())).isEqualTo(2);
        assertThat(Double.parseDouble(result.get("price").n())).isEqualTo(199.99);
        assertThat(result.get("mainEmail").s()).isEqualTo("john.doe@example.com");
        assertThat(result.get("validated").bool()).isTrue();
        assertThat(result.get("buyers").s()).contains("John").contains("Doe").contains("john.doe@example.com");
    }

    @Test
    void toDynamoDB_whenSaleHasNoId_shouldGenerateOne() {
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
        Map<String, AttributeValue> result = mapper.toDynamoDB(saleWithoutId);

        // Then
        assertThat(result).isNotNull();
        assertThat(UUID.fromString(result.get("id").s())).isNotNull();
    }

    @Test
    void toDomain_shouldMapAllFields() {
        // Given
        String buyersJson = "[{\"id\":\"buyer-123\",\"name\":\"John\",\"lastName\":\"Doe\"," +
            "\"email\":\"john.doe@example.com\",\"phone\":\"+1234567890\"," +
            "\"nationality\":\"US\",\"documentType\":\"PASSPORT\",\"document\":\"AB123456\"}]";
            
        Map<String, AttributeValue> item = Map.of(
            "id", AttributeValue.builder().s(testSaleId).build(),
            "eventId", AttributeValue.builder().s(testEventId).build(),
            "ticketId", AttributeValue.builder().s(testTicketId).build(),
            "quantity", AttributeValue.builder().n("2").build(),
            "price", AttributeValue.builder().n("199.99").build(),
            "buyers", AttributeValue.builder().s(buyersJson).build(),
            "mainEmail", AttributeValue.builder().s("john.doe@example.com").build(),
            "validated", AttributeValue.builder().bool(true).build()
        );

        // When
        Sale result = mapper.toDomain(item);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(testSaleId);
        assertThat(result.eventId()).isEqualTo(testEventId);
        assertThat(result.ticketId()).isEqualTo(testTicketId);
        assertThat(result.quantity()).isEqualTo(2);
        assertThat(result.price()).isEqualTo(199.99);
        assertThat(result.mainEmail()).isEqualTo("john.doe@example.com");
        assertThat(result.validated()).isTrue();
        assertThat(result.buyer()).hasSize(1);
        assertThat(result.buyer().getFirst().name()).isEqualTo("John");
        assertThat(result.buyer().getFirst().lastName()).isEqualTo("Doe");
        assertThat(result.buyer().getFirst().email()).isEqualTo("john.doe@example.com");
    }

    @Test
    void toDomain_WithNullItem_ReturnsNull() {
        // When
        Sale result = mapper.toDomain(null);

        // Then
        assertNull(result);
    }

    @Test
    void toDomain_WithEmptyItem_ReturnsNull() {
        // When
        Sale result = mapper.toDomain(Map.of());

        // Then
        assertNull(result);
    }
}
