package com.ticketoffice.backend.infra.adapters.out.db.dao;

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
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractDynamoDao {

    protected final DynamoDbClient client;
    protected final String tableName;

    public AbstractDynamoDao(DynamoDbClient client, String tableName) {
        this.client = client;
        this.tableName = tableName;
    }

    public void save(Map<String, AttributeValue> eventAttributes) {
        PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(eventAttributes)
                .build();
        client.putItem(request);
    }

    public Map<String, AttributeValue> getById(String id) {
        GetItemRequest request = GetItemRequest.builder()
                .tableName(tableName)
                .key(Collections.singletonMap("id", AttributeValue.builder().s(id).build()))
                .build();

        GetItemResponse response = client.getItem(request);
        return response.hasItem() ? response.item() : Collections.emptyMap();
    }

    public List<Map<String, AttributeValue>> getAll() {
        ScanRequest request = ScanRequest.builder()
                .tableName(tableName)
                .build();

        ScanResponse response = client.scan(request);
        return response.items();
    }

    public void update(String id, Map<String, AttributeValue> updatedAttributes) {
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
                .key(Collections.singletonMap("id", AttributeValue.builder().s(id).build()))
                .attributeUpdates(attributeUpdates)
                .build();

        client.updateItem(request);
    }


    /**
     * Helper method to query by multiple index keys.
     *
     * @param indexName The name of the index to query
     * @param keyConditions Map of key names to their corresponding values
     * @return List of items matching the query
     */
    protected List<Map<String, AttributeValue>> queryByMultipleIndex(String indexName, Map<String, String> keyConditions) {
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
    protected List<Map<String, AttributeValue>> queryByIndex(String indexName, String keyName, String keyValue) {
        return queryByMultipleIndex(indexName, Collections.singletonMap(keyName, keyValue));
    }

    /**
     * Helper method to delete an item by its ID.
     *
     * @param id The ID of the item to delete
     */
    public void deleteById(String id) {
        DeleteItemRequest request = DeleteItemRequest.builder()
                .tableName(tableName)
                .key(Collections.singletonMap("id", AttributeValue.builder().s(id).build()))
                .build();
        client.deleteItem(request);
    }
}
