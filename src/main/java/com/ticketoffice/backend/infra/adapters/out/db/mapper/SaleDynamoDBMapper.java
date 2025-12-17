package com.ticketoffice.backend.infra.adapters.out.db.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketoffice.backend.domain.models.Buyer;
import com.ticketoffice.backend.domain.models.Sale;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Mapper class to convert between Sale domain model and DynamoDB attribute maps.
 */
public class SaleDynamoDBMapper {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Converts a Sale domain object to a DynamoDB attribute map.
     *
     * @param sale The Sale domain object to convert
     * @return A map of DynamoDB attributes
     */
    public Map<String, AttributeValue> toDynamoDB(Sale sale) {
        if (sale == null) {
            return null;
        }

        Map<String, AttributeValue> item = new java.util.HashMap<>();
        
        // Required fields
        item.put("id", AttributeValue.builder().s(sale.id() != null ? sale.id() : UUID.randomUUID().toString()).build());
        item.put("eventId", AttributeValue.builder().s(sale.eventId()).build());
        item.put("ticketId", AttributeValue.builder().s(sale.ticketId()).build());
        item.put("quantity", AttributeValue.builder().n(String.valueOf(sale.quantity())).build());
        item.put("price", AttributeValue.builder().n(String.valueOf(sale.price())).build());
        
        // Optional fields
        if (sale.mainEmail() != null) {
            item.put("mainEmail", AttributeValue.builder().s(sale.mainEmail()).build());
        }
        
        if (sale.validated() != null) {
            item.put("validated", AttributeValue.builder().bool(sale.validated()).build());
        }
        
        // Handle buyer list if present
        if (sale.buyer() != null && !sale.buyer().isEmpty()) {
            try {
                String buyersJson = objectMapper.writeValueAsString(sale.buyer());
                item.put("buyers", AttributeValue.builder().s(buyersJson).build());
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to serialize buyers list", e);
            }
        }
        
        return item;
    }
    
    /**
     * Converts a DynamoDB attribute map to a Sale domain object.
     *
     * @param item The DynamoDB attribute map
     * @return A Sale domain object
     */
    public Sale toDomain(Map<String, AttributeValue> item) {
        if (item == null || item.isEmpty()) {
            return null;
        }

        try {
            List<Buyer> buyers = List.of();
            if (item.containsKey("buyers")) {
                String buyersJson = item.get("buyers").s();
                buyers = objectMapper.readValue(buyersJson, new TypeReference<>() {
                });
            }

            return new Sale(
                item.get("id") != null ? item.get("id").s() : null,
                item.get("eventId") != null ? item.get("eventId").s() : null,
                item.get("ticketId") != null ? item.get("ticketId").s() : null,
                item.get("quantity") != null ? Integer.parseInt(item.get("quantity").n()) : null,
                item.get("price") != null ? Double.parseDouble(item.get("price").n()) : null,
                buyers,
                item.get("mainEmail") != null ? item.get("mainEmail").s() : null,
                item.get("validated") != null ? item.get("validated").bool() : false
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize buyers list", e);
        }
    }
}
