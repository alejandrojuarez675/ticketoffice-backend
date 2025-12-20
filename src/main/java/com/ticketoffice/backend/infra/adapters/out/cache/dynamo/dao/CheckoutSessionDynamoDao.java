package com.ticketoffice.backend.infra.adapters.out.cache.dynamo.dao;

import static com.ticketoffice.backend.infra.adapters.out.cache.dynamo.mapper.CheckoutSessionDynamoDBMapper.DynamoKeys.EVENT_ID;
import static com.ticketoffice.backend.infra.adapters.out.cache.dynamo.mapper.CheckoutSessionDynamoDBMapper.DynamoKeys.PRICE_ID;

import com.google.inject.Inject;
import com.ticketoffice.backend.infra.adapters.out.db.dao.AbstractDynamoDao;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;

/**
 * Data Access Object for CheckoutSession entities in DynamoDB.
 * Handles all DynamoDB operations for the CheckoutSession entity.
 */
public class CheckoutSessionDynamoDao extends AbstractDynamoDao {

    private static final String TABLE_NAME = "CheckoutSessions";
    private static final String EVENT_TICKET_INDEX = "EventIdTicketIdIndex";

    /**
     * Creates a new CheckoutSessionDynamoDao with default AWS credentials and region.
     * Uses the default credential provider chain and us-east-1 region.
     */
    @Inject
    public CheckoutSessionDynamoDao() {
        super(
                DynamoDbClient.builder()
                        .region(Region.US_EAST_1)
                        .credentialsProvider(DefaultCredentialsProvider.create())
                        .build(),
                TABLE_NAME
        );
    }

    /**
     * Counts the number of checkout sessions for a specific event and ticket.
     *
     * @param eventId  The ID of the event
     * @param ticketId The ID of the ticket (optional)
     * @return The count of matching checkout sessions
     */
    public @NotNull Integer countByEventIdAndTicketId(@NotNull String eventId, @NotNull String ticketId) {
        String keyConditionExpression = EVENT_ID + " = :eventId AND " + PRICE_ID + " = :ticketId";
        Map<String, AttributeValue> expressionValues = new HashMap<>();
        expressionValues.put(":eventId", AttributeValue.builder().s(eventId).build());
        expressionValues.put(":ticketId", AttributeValue.builder().s(ticketId).build());

        QueryRequest request = QueryRequest.builder()
                .tableName(tableName)
                .indexName(EVENT_TICKET_INDEX)
                .keyConditionExpression(keyConditionExpression)
                .expressionAttributeValues(expressionValues)
                .select("COUNT")
                .build();

        QueryResponse response = client.query(request);
        return response.count();
    }
}
