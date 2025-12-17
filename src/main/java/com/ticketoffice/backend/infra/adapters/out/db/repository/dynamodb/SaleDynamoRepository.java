package com.ticketoffice.backend.infra.adapters.out.db.repository.dynamodb;

import com.ticketoffice.backend.domain.models.Sale;
import com.ticketoffice.backend.domain.ports.SaleRepository;
import com.ticketoffice.backend.infra.adapters.out.db.dao.SaleDynamoDao;
import com.ticketoffice.backend.infra.adapters.out.db.mapper.SaleDynamoDBMapper;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * DynamoDB implementation of the SaleRepository interface.
 * Handles all data access operations for Sale entities using Amazon DynamoDB.
 */
@Singleton
public class SaleDynamoRepository implements SaleRepository {
    private static final Logger logger = LoggerFactory.getLogger(SaleDynamoRepository.class);

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
            // Generate a new ID if one doesn't exist
            Sale saleToSave = sale;
            if (sale.id() == null || sale.id().isEmpty()) {
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
            saleDynamoDao.save(item);
            return Optional.of(saleToSave);
        } catch (Exception e) {
            logger.error("Error saving sale: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public Integer countByEventIdAndTicketId(String eventId, String ticketId) {
        try {
            return saleDynamoDao.countByEventIdAndTicketId(eventId, ticketId);
        } catch (Exception e) {
            // Log the error
            logger.error("Error counting sales for eventId: {} and ticketId: {} - {}", eventId, ticketId, e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public List<Sale> findByEventId(String eventId) {
        try {
            return saleDynamoDao.findByEventId(eventId).stream()
                    .map(saleDynamoDBMapper::toDomain)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            // Log the error
            logger.error("Error finding sales for eventId: {} - {}", eventId, e.getMessage(), e);
            return List.of();
        }
    }

    @Override
    public Optional<Sale> getById(String id) {
        try {
            Map<String, AttributeValue> item = saleDynamoDao.getById(id);
            if (item == null || item.isEmpty()) {
                return Optional.empty();
            }

            return Optional.ofNullable(saleDynamoDBMapper.toDomain(item));
        } catch (Exception e) {
            // Log the error
            logger.error("Error finding sale by id: {} - {}", id, e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Sale> update(String id, Sale sale) {
        try {
            // First check if the sale exists
            Map<String, AttributeValue> existingItem = saleDynamoDao.getById(id);
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
            saleDynamoDao.save(updatedItem);
            return Optional.of(updatedSale);
        } catch (Exception e) {
            // Log the error
            logger.error("Error updating sale by id: {} - {}", id, e.getMessage(), e);
            return Optional.empty();
        }
    }
}
