package com.ticketoffice.backend.infra.adapters.out.cache.dynamo.mapper;

import com.ticketoffice.backend.domain.models.CheckoutSession;
import java.time.LocalDateTime;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Mapper class to convert between CheckoutSession domain model and DynamoDB attribute values.
 */
public class CheckoutSessionDynamoDBMapper {

    public static class DynamoKeys {
        public static final String ID = "id";
        public static final String EVENT_ID = "eventId";
        public static final String PRICE_ID = "priceId";
        public static final String QUANTITY = "quantity";
        public static final String PRICE = "price";
        public static final String STATUS = "status";
        public static final String EXPIRATION_TIME = "expirationTime";
        public static final String TTL = "ttl";
    }

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private CheckoutSessionDynamoDBMapper() {
        // Private constructor to prevent instantiation
    }

    /**
     * Converts a CheckoutSession domain object to a DynamoDB attribute map.
     *
     * @param checkoutSession The CheckoutSession to convert
     * @return A map of DynamoDB attribute values
     */
    public static Map<String, AttributeValue> toMap(CheckoutSession checkoutSession) {
        if (checkoutSession == null) {
            return null;
        }

        Map<String, AttributeValue> item = new HashMap<>();
        item.put(DynamoKeys.ID, AttributeValue.builder().s(checkoutSession.getId()).build());
        item.put(DynamoKeys.EVENT_ID, AttributeValue.builder().s(checkoutSession.getEventId()).build());
        item.put(DynamoKeys.PRICE_ID, AttributeValue.builder().s(checkoutSession.getPriceId()).build());
        item.put(DynamoKeys.QUANTITY, AttributeValue.builder().n(checkoutSession.getQuantity().toString()).build());
        item.put(DynamoKeys.PRICE, AttributeValue.builder().n(String.valueOf(checkoutSession.getPrice())).build());
        item.put(DynamoKeys.STATUS, AttributeValue.builder().s(checkoutSession.getStatus().name()).build());
        
        if (checkoutSession.getExpirationTime() != null) {
            String expirationTime = checkoutSession.getExpirationTime().format(DATE_TIME_FORMATTER);
            item.put(DynamoKeys.EXPIRATION_TIME, AttributeValue.builder().s(expirationTime).build());
            
            // Add TTL attribute (in seconds since epoch)
            long ttl = checkoutSession.getExpirationTime().toEpochSecond(java.time.ZoneOffset.UTC);
            item.put(DynamoKeys.TTL, AttributeValue.builder().n(String.valueOf(ttl)).build());
        }

        return item;
    }

    /**
     * Converts a DynamoDB attribute map to a CheckoutSession domain object.
     *
     * @param item The DynamoDB item map
     * @return A CheckoutSession object or null if the input is null
     */
    public static CheckoutSession fromMap(Map<String, AttributeValue> item) {
        if (item == null || item.isEmpty()) {
            return null;
        }

        return new CheckoutSession(
                item.get(DynamoKeys.ID).s(),
                item.get(DynamoKeys.EVENT_ID).s(),
                item.get(DynamoKeys.PRICE_ID).s(),
                Integer.parseInt(item.get(DynamoKeys.QUANTITY).n()),
                Double.parseDouble(item.get(DynamoKeys.PRICE).n()),
                CheckoutSession.Status.valueOf(item.get(DynamoKeys.STATUS).s()),
                item.containsKey(DynamoKeys.EXPIRATION_TIME) ? 
                    LocalDateTime.parse(item.get(DynamoKeys.EXPIRATION_TIME).s(), DATE_TIME_FORMATTER) :
                    null
        );
    }
}
