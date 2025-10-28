package com.ticketoffice.backend.infra.adapters.out.db.dao;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeAction;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

/**
 * Data Access Object for interacting with EventTable in DynamoDB.
 * Provides CRUD operations and query methods using Global Secondary Indexes.
 */
public class EventDao {
    private final DynamoDbClient client;
    private final String tableName = "EventTable";
    
    // Index names
    private static final String COUNTRY_INDEX = "country-index";
    private static final String CITY_INDEX = "city-index";
    private static final String TITLE_INDEX = "title-index";
    private static final String STATUS_INDEX = "status-index";
    private static final String DATE_INDEX = "date-index";
    private static final String ORGANIZER_INDEX = "organizer-index";

    /**
     * Creates a new EventDao with default AWS credentials and region.
     * Uses the default credential provider chain and us-east-1 region.
     */
    public EventDao() {
        this.client = DynamoDbClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    /**
     * Saves an event to DynamoDB.
     *
     * @param eventAttributes Map containing all event attributes
     */
    public void saveEvent(Map<String, AttributeValue> eventAttributes) {
        PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(eventAttributes)
                .build();
        client.putItem(request);
    }

    /**
     * Retrieves an event by its ID.
     *
     * @param eventId The ID of the event to retrieve
     * @return Map containing the event attributes, or empty if not found
     */
    public Map<String, AttributeValue> getEventById(String eventId) {
        GetItemRequest request = GetItemRequest.builder()
                .tableName(tableName)
                .key(Collections.singletonMap("id", AttributeValue.builder().s(eventId).build()))
                .build();
        
        GetItemResponse response = client.getItem(request);
        return response.hasItem() ? response.item() : Collections.emptyMap();
    }

    /**
     * Queries events by country.
     *
     * @param country The country to query events for
     * @return List of events in the specified country
     */
    public List<Map<String, AttributeValue>> getEventsByCountry(String country) {
        return queryByIndex(COUNTRY_INDEX, "country", country);
    }

    /**
     * Queries events by city.
     *
     * @param city The city to query events for
     * @return List of events in the specified city
     */
    public List<Map<String, AttributeValue>> getEventsByCity(String city) {
        return queryByIndex(CITY_INDEX, "city", city);
    }

    /**
     * Queries events by title (case-sensitive).
     *
     * @param title The title to search for
     * @return List of events with the specified title
     */
    public List<Map<String, AttributeValue>> getEventsByTitle(String title) {
        return queryByIndex(TITLE_INDEX, "title", title);
    }

    /**
     * Queries events by status.
     *
     * @param status The status to filter events by
     * @return List of events with the specified status
     */
    public List<Map<String, AttributeValue>> getEventsByStatus(String status) {
        return queryByIndex(STATUS_INDEX, "status", status);
    }

    /**
     * Queries events by date.
     *
     * @param date The date to query events for (format should match your date format)
     * @return List of events on the specified date
     */
    public List<Map<String, AttributeValue>> getEventsByDate(String date) {
        return queryByIndex(DATE_INDEX, "date", date);
    }

    /**
     * Queries events by organizer ID.
     *
     * @param organizerId The ID of the organizer
     * @return List of events organized by the specified organizer
     */
    public List<Map<String, AttributeValue>> getEventsByOrganizer(String organizerId) {
        return queryByIndex(ORGANIZER_INDEX, "organizerId", organizerId);
    }

    /**
     * Updates an existing event.
     *
     * @param eventId The ID of the event to update
     * @param updatedAttributes Map of attributes to update
     */
    public void updateEvent(String eventId, Map<String, AttributeValue> updatedAttributes) {
        // Ensure we don't update the ID
        updatedAttributes.remove("id");
        
        Map<String, AttributeValueUpdate> attributeUpdates = updatedAttributes.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> AttributeValueUpdate.builder()
                                .value(e.getValue())
                                .action(AttributeAction.PUT)
                                .build()
                ));

        UpdateItemRequest request = UpdateItemRequest.builder()
                .tableName(tableName)
                .key(Collections.singletonMap("id", AttributeValue.builder().s(eventId).build()))
                .attributeUpdates(attributeUpdates)
                .build();

        client.updateItem(request);
    }

    /**
     * Deletes an event by ID.
     *
     * @param eventId The ID of the event to delete
     */
    public void deleteEvent(String eventId) {
        DeleteItemRequest request = DeleteItemRequest.builder()
                .tableName(tableName)
                .key(Collections.singletonMap("id", AttributeValue.builder().s(eventId).build()))
                .build();
        
        client.deleteItem(request);
    }

    /**
     * Helper method to query by multiple index keys.
     *
     * @param indexName The name of the index to query
     * @param keyConditions Map of key names to their corresponding values
     * @return List of items matching the query
     */
    private List<Map<String, AttributeValue>> queryByMultipleIndex(String indexName, Map<String, String> keyConditions) {
        if (keyConditions == null || keyConditions.isEmpty()) {
            throw new IllegalArgumentException("Key conditions cannot be null or empty");
        }

        // Build the key condition expression (e.g., "#key1 = :val1 AND #key2 = :val2")
        StringBuilder keyConditionExpression = new StringBuilder();
        Map<String, String> expressionAttributeNames = new java.util.HashMap<>();
        Map<String, AttributeValue> expressionAttributeValues = new java.util.HashMap<>();
        
        int i = 1;
        for (Map.Entry<String, String> entry : keyConditions.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String keyAlias = "#key" + i;
            String valueAlias = ":val" + i;
            
            if (i > 1) {
                keyConditionExpression.append(" AND ");
            }
            keyConditionExpression.append(keyAlias).append(" = ").append(valueAlias);
            
            expressionAttributeNames.put(keyAlias, key);
            expressionAttributeValues.put(valueAlias, AttributeValue.builder().s(value).build());
            
            i++;
        }

        QueryRequest queryRequest = QueryRequest.builder()
                .tableName(tableName)
                .indexName(indexName)
                .keyConditionExpression(keyConditionExpression.toString())
                .expressionAttributeNames(expressionAttributeNames)
                .expressionAttributeValues(expressionAttributeValues)
                .build();

        QueryResponse response = client.query(queryRequest);
        return response.items();
    }
    
    /**
     * Helper method to query by any index.
     *
     * @param indexName The name of the index to query
     * @param keyName The name of the key to query on
     * @param keyValue The value to match
     * @return List of items matching the query
     */
    private List<Map<String, AttributeValue>> queryByIndex(String indexName, String keyName, String keyValue) {
        return queryByMultipleIndex(indexName, Collections.singletonMap(keyName, keyValue));
    }
}
