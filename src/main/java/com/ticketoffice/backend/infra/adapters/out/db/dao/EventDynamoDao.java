package com.ticketoffice.backend.infra.adapters.out.db.dao;

import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.ticketoffice.backend.infra.adapters.out.db.mapper.EventDynamoDBMapper;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

/**
 * Data Access Object for interacting with EventTable in DynamoDB.
 * Provides CRUD operations and query methods using Global Secondary Indexes.
 */
public class EventDynamoDao extends AbstractDynamoDao {

    // Index names
    private static final String COUNTRY_INDEX = "country-index";
    private static final String CITY_INDEX = "city-index";
    private static final String ORGANIZER_INDEX = "organizer-index";

    /**
     * Creates a new EventDao with default AWS credentials and region.
     * Uses the default credential provider chain and us-east-1 region.
     */
    @Inject
    public EventDynamoDao() {
        super(
                DynamoDbClient.builder()
                        .region(Region.US_EAST_1)
                        .credentialsProvider(DefaultCredentialsProvider.create())
                        .build(),
                "EventTable"
        );
    }

    /**
     * Queries events by country.
     *
     * @param country The country to query events for
     * @return List of events in the specified country
     */
    public List<Map<String, AttributeValue>> getEventsByCountry(String country) {
        return queryByIndex(COUNTRY_INDEX, EventDynamoDBMapper.DynamoKeys.LOCATION_COUNTRY, country);
    }

    /**
     * Queries events by organizer ID.
     *
     * @param organizerId The ID of the organizer
     * @return List of events organized by the specified organizer
     */
    public List<Map<String, AttributeValue>> getEventsByOrganizer(String organizerId) {
        return queryByIndex(ORGANIZER_INDEX, EventDynamoDBMapper.DynamoKeys.ORGANIZER_ID, organizerId);
    }


    public List<Map<String, AttributeValue>> getEventsByCity(String city) {
        return queryByIndex(CITY_INDEX, EventDynamoDBMapper.DynamoKeys.LOCATION_CITY, city);
    }
}
