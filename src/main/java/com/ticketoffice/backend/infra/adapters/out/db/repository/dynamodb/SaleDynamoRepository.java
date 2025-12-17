package com.ticketoffice.backend.infra.adapters.out.db.repository.dynamodb;

import com.ticketoffice.backend.domain.models.Sale;
import com.ticketoffice.backend.domain.ports.SaleRepository;
import com.ticketoffice.backend.infra.adapters.out.db.dao.SaleDynamoDao;
import com.ticketoffice.backend.infra.adapters.out.db.mapper.SaleDynamoDBMapper;
import com.ticketoffice.backend.infra.adapters.out.db.utils.RetryUtil;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * DynamoDB implementation of the SaleRepository interface.
 * Handles all data access operations for Sale entities using Amazon DynamoDB.
 * Includes retry logic for transient failures and comprehensive error handling.
 */
@Singleton
public class SaleDynamoRepository implements SaleRepository {
    private static final Logger logger = LoggerFactory.getLogger(SaleDynamoRepository.class);
    private static final RetryUtil.RetryConfig RETRY_CONFIG = new RetryUtil.RetryConfig();

    private final SaleDynamoDao saleDynamoDao;
    private final SaleDynamoDBMapper saleDynamoDBMapper;

    @Inject
    public SaleDynamoRepository(SaleDynamoDao saleDynamoDao, SaleDynamoDBMapper saleDynamoDBMapper) {
        this.saleDynamoDao = saleDynamoDao;
        this.saleDynamoDBMapper = saleDynamoDBMapper;
    }

    @Override
    public Optional<Sale> save(Sale sale) {
        try {
            // Generate ID if not provided
            Sale saleToSave = sale;
            if (sale.id() == null) {
                saleToSave = new Sale(
                    UUID.randomUUID().toString(),
                    sale.eventId(),
                    sale.ticketId(),
                    sale.quantity(),
                    sale.price(),
                    sale.buyer(),
                    sale.mainEmail(),
                    sale.validated()
                );
            }
            
            Map<String, AttributeValue> item = saleDynamoDBMapper.toDynamoDB(saleToSave);
            
            // Execute with retry logic
            RetryUtil.executeWithRetry("save", () -> saleDynamoDao.save(item), RETRY_CONFIG);
            
            return Optional.of(saleToSave);
            
        } catch (Exception e) {
            logger.error("Error saving sale: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public Integer countByEventIdAndTicketId(String eventId, String ticketId) {
        try {
            return RetryUtil.executeWithRetry(
                "countByEventIdAndTicketId",
                () -> saleDynamoDao.countByEventIdAndTicketId(eventId, ticketId),
                0,
                RETRY_CONFIG
            );
        } catch (Exception e) {
            logger.error("Error counting sales by eventId: {} and ticketId: {} - {}", 
                eventId, ticketId, e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public List<Sale> findByEventId(String eventId) {
        try {
            List<Map<String, AttributeValue>> items = RetryUtil.executeWithRetry(
                "findByEventId",
                () -> saleDynamoDao.findByEventId(eventId),
                List.of(),
                RETRY_CONFIG
            );
            
            return items.stream()
                .map(saleDynamoDBMapper::toDomain)
                .toList();
                
        } catch (Exception e) {
            logger.error("Error finding sales by eventId: {} - {}", eventId, e.getMessage(), e);
            return List.of();
        }
    }

    @Override
    public Optional<Sale> getById(String id) {
        try {
            Map<String, AttributeValue> item = RetryUtil.executeWithRetry(
                "getById",
                () -> saleDynamoDao.getById(id),
                Map.of(),
                RETRY_CONFIG
            );
            
            if (item == null || item.isEmpty()) {
                return Optional.empty();
            }
            
            return Optional.ofNullable(saleDynamoDBMapper.toDomain(item));
            
        } catch (Exception e) {
            logger.error("Error getting sale by id: {} - {}", id, e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Sale> update(String id, Sale sale) {
        try {
            // First check if the sale exists
            Map<String, AttributeValue> existingItem = RetryUtil.executeWithRetry(
                "getByIdForUpdate",
                () -> saleDynamoDao.getById(id),
                Map.of(),
                RETRY_CONFIG
            );
            
            if (existingItem == null || existingItem.isEmpty()) {
                return Optional.empty();
            }
            
            // Create a new Sale with the provided ID
            Sale updatedSale = new Sale(
                id,
                sale.eventId(),
                sale.ticketId(),
                sale.quantity(),
                sale.price(),
                sale.buyer(),
                sale.mainEmail(),
                sale.validated()
            );
            
            Map<String, AttributeValue> updatedItem = saleDynamoDBMapper.toDynamoDB(updatedSale);
            
            // Execute with retry logic
            RetryUtil.executeWithRetry("saveUpdate", () -> saleDynamoDao.save(updatedItem), RETRY_CONFIG);
            
            return Optional.of(updatedSale);
            
        } catch (Exception e) {
            logger.error("Error updating sale by id: {} - {}", id, e.getMessage(), e);
            return Optional.empty();
        }
    }
    
}
