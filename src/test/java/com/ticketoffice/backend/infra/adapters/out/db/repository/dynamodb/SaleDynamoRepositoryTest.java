package com.ticketoffice.backend.infra.adapters.out.db.repository.dynamodb;

import com.ticketoffice.backend.domain.models.Buyer;
import com.ticketoffice.backend.domain.models.Sale;
import com.ticketoffice.backend.infra.adapters.out.db.dao.SaleDynamoDao;
import com.ticketoffice.backend.infra.adapters.out.db.mapper.SaleDynamoDBMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleDynamoRepositoryTest {

    @Mock
    private SaleDynamoDao saleDynamoDao;

    @Mock
    private SaleDynamoDBMapper saleDynamoDBMapper;

    @InjectMocks
    private SaleDynamoRepository saleDynamoRepository;

    private Sale testSale;
    private Map<String, AttributeValue> testDynamoItem;
    private final String testSaleId = UUID.randomUUID().toString();
    private final String testEventId = "event-123";
    private final String testTicketId = "ticket-456";

    @BeforeEach
    void setUp() {
        var buyer = new Buyer(
                "buyer-123",  // id
                "John",       // name
                "Doe",        // lastName
                "john.doe@example.com",  // email
                "+1234567890",          // phone
                "US",                   // nationality
                "PASSPORT",            // documentType
                "AB123456"             // document
        );

        testSale = new Sale(
            testSaleId,                     // id
            testEventId,                    // eventId
            testTicketId,                   // ticketId
            2,                             // quantity
            199.99,                        // price
            List.of(buyer), // buyer
            "john.doe@example.com",        // mainEmail
            true                           // validated
        );

        testDynamoItem = Map.of(
            "id", AttributeValue.builder().s(testSaleId).build(),
            "eventId", AttributeValue.builder().s(testEventId).build(),
            "ticketId", AttributeValue.builder().s(testTicketId).build(),
            "quantity", AttributeValue.builder().n("2").build(),
            "price", AttributeValue.builder().n("199.99").build(),
            "mainEmail", AttributeValue.builder().s("john.doe@example.com").build(),
            "validated", AttributeValue.builder().bool(true).build()
        );
    }

    @Test
    void save_shouldSaveSaleAndReturnIt() {
        // Given
        when(saleDynamoDBMapper.toDynamoDB(any(Sale.class))).thenReturn(testDynamoItem);
        doNothing().when(saleDynamoDao).save(any());

        // When
        Optional<Sale> result = saleDynamoRepository.save(testSale);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testSale);
        verify(saleDynamoDBMapper).toDynamoDB(testSale);
        verify(saleDynamoDao).save(testDynamoItem);
    }

    @Test
    void save_shouldGenerateNewIdWhenNotProvided() {
        // Given
        var buyer = new Buyer(
                "buyer-123",  // id
                "John",       // name
                "Doe",        // lastName
                "john.doe@example.com",  // email
                "+1234567890",          // phone
                "US",                   // nationality
                "PASSPORT",            // documentType
                "AB123456"             // document
        );

        Sale saleWithoutId = new Sale(
            null,  // id
            testEventId,
            testTicketId,
            2,
            199.99,
            List.of(buyer),
            "john.doe@example.com",
            true
        );
        
        when(saleDynamoDBMapper.toDynamoDB(any(Sale.class))).thenReturn(testDynamoItem);
        doNothing().when(saleDynamoDao).save(any());

        // When
        Optional<Sale> result = saleDynamoRepository.save(saleWithoutId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().id()).isNotBlank();
        verify(saleDynamoDBMapper).toDynamoDB(any(Sale.class));
        verify(saleDynamoDao).save(testDynamoItem);
    }

    @Test
    void countByEventIdAndTicketId_shouldReturnCountFromDao() {
        // Given
        int expectedCount = 5;
        when(saleDynamoDao.countByEventIdAndTicketId(testEventId, testTicketId)).thenReturn(expectedCount);

        // When
        int result = saleDynamoRepository.countByEventIdAndTicketId(testEventId, testTicketId);

        // Then
        assertThat(result).isEqualTo(expectedCount);
        verify(saleDynamoDao).countByEventIdAndTicketId(testEventId, testTicketId);
    }

    @Test
    void findByEventId_shouldReturnSalesFromDao() {
        // Given
        List<Map<String, AttributeValue>> dynamoItems = List.of(testDynamoItem);
        when(saleDynamoDao.findByEventId(testEventId)).thenReturn(dynamoItems);
        when(saleDynamoDBMapper.toDomain(testDynamoItem)).thenReturn(testSale);

        // When
        List<Sale> result = saleDynamoRepository.findByEventId(testEventId);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).isEqualTo(testSale);
        verify(saleDynamoDao).findByEventId(testEventId);
        verify(saleDynamoDBMapper).toDomain(testDynamoItem);
    }

    @Test
    void getById_shouldReturnSaleWhenFound() {
        // Given
        when(saleDynamoDao.getById(testSaleId)).thenReturn(testDynamoItem);
        when(saleDynamoDBMapper.toDomain(testDynamoItem)).thenReturn(testSale);

        // When
        Optional<Sale> result = saleDynamoRepository.getById(testSaleId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testSale);
        verify(saleDynamoDao).getById(testSaleId);
        verify(saleDynamoDBMapper).toDomain(testDynamoItem);
    }

    @Test
    void getById_shouldReturnEmptyWhenNotFound() {
        // Given
        when(saleDynamoDao.getById(anyString())).thenReturn(Map.of());

        // When
        Optional<Sale> result = saleDynamoRepository.getById("non-existent-id");

        // Then
        assertThat(result).isEmpty();
        verify(saleDynamoDao).getById("non-existent-id");
        verify(saleDynamoDBMapper, never()).toDomain(any());
    }

    @Test
    void update_shouldUpdateExistingSale() {
        // Given
        when(saleDynamoDao.getById(testSaleId)).thenReturn(testDynamoItem);
        when(saleDynamoDBMapper.toDynamoDB(any(Sale.class))).thenReturn(testDynamoItem);

        // When
        Optional<Sale> result = saleDynamoRepository.update(testSaleId, testSale);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testSale);
        verify(saleDynamoDao).getById(testSaleId);
        verify(saleDynamoDBMapper).toDynamoDB(any(Sale.class));
        verify(saleDynamoDao).save(testDynamoItem);
    }

    @Test
    void update_shouldReturnEmptyWhenSaleNotFound() {
        // Given
        when(saleDynamoDao.getById(anyString())).thenReturn(Map.of());

        // When
        Optional<Sale> result = saleDynamoRepository.update("non-existent-id", testSale);

        // Then
        assertThat(result).isEmpty();
        verify(saleDynamoDao).getById("non-existent-id");
        verify(saleDynamoDBMapper, never()).toDynamoDB(any());
        verify(saleDynamoDao, never()).save(any());
    }
}
