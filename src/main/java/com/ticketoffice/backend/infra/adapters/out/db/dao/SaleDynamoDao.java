package com.ticketoffice.backend.infra.adapters.out.db.dao;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data Access Object for Sale entities in DynamoDB.
 * Handles all DynamoDB operations for the Sale entity.
 */
public class SaleDynamoDao extends AbstractDynamoDao {

    private static final String TABLE_NAME = "SalesTable";
    private static final String EVENT_ID_INDEX = "EventIdIndex";
    private static final String EVENT_TICKET_INDEX = "EventIdTicketIdIndex";

    public SaleDynamoDao(DynamoDbClient client) {
        super(client, TABLE_NAME);
    }

    /**
     * Saves a sale to DynamoDB.
     *
     * @param saleAttributes The sale attributes to save
     */
    public void save(Map<String, AttributeValue> saleAttributes) {
        PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(saleAttributes)
                .build();
        client.putItem(request);
    }

    /**
     * Retrieves a sale by its ID.
     *
     * @param id The ID of the sale to retrieve
     * @return A map of the sale attributes, or an empty map if not found
     */
    public Map<String, AttributeValue> getById(String id) {
        GetItemRequest request = GetItemRequest.builder()
                .tableName(tableName)
                .key(Collections.singletonMap("id", AttributeValue.builder().s(id).build()))
                .build();

        GetItemResponse response = client.getItem(request);
        return response.hasItem() ? response.item() : Collections.emptyMap();
    }

    /**
     * Finds all sales for a specific event.
     *
     * @param eventId The ID of the event
     * @return List of sales matching the event ID
     */
    public List<Map<String, AttributeValue>> findByEventId(String eventId) {
        QueryRequest request = QueryRequest.builder()
                .tableName(tableName)
                .indexName(EVENT_ID_INDEX)
                .keyConditionExpression("eventId = :eventId")
                .expressionAttributeValues(Collections.singletonMap(
                        ":eventId", 
                        AttributeValue.builder().s(eventId).build()
                ))
                .build();

        QueryResponse response = client.query(request);
        return response.items();
    }

    /**
     * Counts the number of sales for a specific event and ticket.
     *
     * @param eventId  The ID of the event
     * @param ticketId The ID of the ticket (optional)
     * @return The count of matching sales
     */
    public int countByEventIdAndTicketId(String eventId, String ticketId) {
        String keyConditionExpression = "eventId = :eventId";
        Map<String, AttributeValue> expressionValues = new HashMap<>();
        
        expressionValues.put(":eventId", AttributeValue.builder().s(eventId).build());

        if (ticketId != null && !ticketId.isEmpty()) {
            keyConditionExpression += " AND ticketId = :ticketId";
            expressionValues.put(":ticketId", AttributeValue.builder().s(ticketId).build());
        }

        QueryRequest request = QueryRequest.builder()
                .tableName(tableName)
                .indexName(ticketId != null && !ticketId.isEmpty() ? EVENT_TICKET_INDEX : EVENT_ID_INDEX)
                .keyConditionExpression(keyConditionExpression)
                .expressionAttributeValues(expressionValues)
                .select("COUNT")
                .build();

        QueryResponse response = client.query(request);
        return response.count();
    }
}
