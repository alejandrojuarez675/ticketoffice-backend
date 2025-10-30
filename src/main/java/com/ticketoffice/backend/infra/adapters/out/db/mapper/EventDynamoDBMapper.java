package com.ticketoffice.backend.infra.adapters.out.db.mapper;

import static com.ticketoffice.backend.infra.adapters.out.db.mapper.EventDynamoDBMapper.DynamoKeys.ADDITIONAL_INFO;
import static com.ticketoffice.backend.infra.adapters.out.db.mapper.EventDynamoDBMapper.DynamoKeys.DATE;
import static com.ticketoffice.backend.infra.adapters.out.db.mapper.EventDynamoDBMapper.DynamoKeys.DESCRIPTION;
import static com.ticketoffice.backend.infra.adapters.out.db.mapper.EventDynamoDBMapper.DynamoKeys.ID;
import static com.ticketoffice.backend.infra.adapters.out.db.mapper.EventDynamoDBMapper.DynamoKeys.IMAGE_ALT;
import static com.ticketoffice.backend.infra.adapters.out.db.mapper.EventDynamoDBMapper.DynamoKeys.IMAGE_ID;
import static com.ticketoffice.backend.infra.adapters.out.db.mapper.EventDynamoDBMapper.DynamoKeys.IMAGE_URL;
import static com.ticketoffice.backend.infra.adapters.out.db.mapper.EventDynamoDBMapper.DynamoKeys.LOCATION_ADDRESS;
import static com.ticketoffice.backend.infra.adapters.out.db.mapper.EventDynamoDBMapper.DynamoKeys.LOCATION_CITY;
import static com.ticketoffice.backend.infra.adapters.out.db.mapper.EventDynamoDBMapper.DynamoKeys.LOCATION_COUNTRY;
import static com.ticketoffice.backend.infra.adapters.out.db.mapper.EventDynamoDBMapper.DynamoKeys.LOCATION_ID;
import static com.ticketoffice.backend.infra.adapters.out.db.mapper.EventDynamoDBMapper.DynamoKeys.LOCATION_NAME;
import static com.ticketoffice.backend.infra.adapters.out.db.mapper.EventDynamoDBMapper.DynamoKeys.ORGANIZER_ID;
import static com.ticketoffice.backend.infra.adapters.out.db.mapper.EventDynamoDBMapper.DynamoKeys.STATUS;
import static com.ticketoffice.backend.infra.adapters.out.db.mapper.EventDynamoDBMapper.DynamoKeys.TICKETS;
import static com.ticketoffice.backend.infra.adapters.out.db.mapper.EventDynamoDBMapper.DynamoKeys.TICKETS_CURRENCY;
import static com.ticketoffice.backend.infra.adapters.out.db.mapper.EventDynamoDBMapper.DynamoKeys.TICKETS_ID;
import static com.ticketoffice.backend.infra.adapters.out.db.mapper.EventDynamoDBMapper.DynamoKeys.TICKETS_IS_FREE;
import static com.ticketoffice.backend.infra.adapters.out.db.mapper.EventDynamoDBMapper.DynamoKeys.TICKETS_STOCK;
import static com.ticketoffice.backend.infra.adapters.out.db.mapper.EventDynamoDBMapper.DynamoKeys.TICKETS_TYPE;
import static com.ticketoffice.backend.infra.adapters.out.db.mapper.EventDynamoDBMapper.DynamoKeys.TICKETS_VALUE;
import static com.ticketoffice.backend.infra.adapters.out.db.mapper.EventDynamoDBMapper.DynamoKeys.TITLE;

import com.ticketoffice.backend.domain.enums.EventStatus;
import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.models.Image;
import com.ticketoffice.backend.domain.models.Location;
import com.ticketoffice.backend.domain.models.Ticket;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EventDynamoDBMapper {

    public static class DynamoKeys {
        public static final String ID = "id";
        public static final String TITLE = "title";
        public static final String DATE = "date";
        public static final String DESCRIPTION = "description";
        public static final String ORGANIZER_ID = "organizerId";
        public static final String STATUS = "status";
        public static final String LOCATION_ID = "locationId";
        public static final String LOCATION_NAME = "locationName";
        public static final String LOCATION_ADDRESS = "locationAddress";
        public static final String LOCATION_CITY = "city";
        public static final String LOCATION_COUNTRY = "country";
        public static final String ADDITIONAL_INFO = "additionalInfo";
        public static final String IMAGE_ID = "imageId";
        public static final String IMAGE_URL = "imageUrl";
        public static final String IMAGE_ALT = "imageAlt";
        public static final String TICKETS = "tickets";
        public static final String TICKETS_ID = "id";
        public static final String TICKETS_VALUE = "value";
        public static final String TICKETS_CURRENCY = "currency";
        public static final String TICKETS_TYPE = "type";
        public static final String TICKETS_IS_FREE = "isFree";
        public static final String TICKETS_STOCK = "stock";

    }

    private EventDynamoDBMapper() {}

    public static Event fromMap(Map<String, AttributeValue> em) {
        // Extract basic fields
        String id = em.get(ID).s();
        String title = em.get(TITLE).s();
        LocalDateTime date = LocalDateTime.parse(em.get(DATE).s());
        String description = em.get(DESCRIPTION).s();
        String organizerId = em.get(ORGANIZER_ID).s();
        EventStatus status = EventStatus.valueOf(em.get(STATUS).s());

        // Build Location object
        Location location = new Location(
                em.containsKey(LOCATION_ID) ? em.get(LOCATION_ID).s() : "",
                em.containsKey(LOCATION_NAME) ? em.get(LOCATION_NAME).s() : "",
                em.containsKey(LOCATION_ADDRESS) ? em.get(LOCATION_ADDRESS).s() : "",
                em.get(LOCATION_CITY).s(),
                em.get(LOCATION_COUNTRY).s()
        );

        // Build Image object
        Image image = new Image(
                em.containsKey(IMAGE_ID) ? em.get(IMAGE_ID).s() : "",
                em.containsKey(IMAGE_URL) ? em.get(IMAGE_URL).s() : "",
                em.containsKey(IMAGE_URL) ? em.get(IMAGE_ALT).s() : ""
        );

        // Handle tickets list
        List<Ticket> tickets = em.containsKey(TICKETS) ?
                em.get(TICKETS).l().stream()
                        .map(av -> {
                            Map<String, AttributeValue> ticketMap = av.m();
                            return new Ticket(
                                    ticketMap.get(TICKETS_ID).s(),
                                    Double.parseDouble(ticketMap.get(TICKETS_VALUE).n()),
                                    ticketMap.get(TICKETS_CURRENCY).s(),
                                    ticketMap.get(TICKETS_TYPE).s(),
                                    Boolean.getBoolean(ticketMap.get(TICKETS_IS_FREE).s()),
                                    Integer.parseInt(ticketMap.get(TICKETS_STOCK).n())
                            );
                        })
                        .collect(Collectors.toList()) :
                List.of();

        // Handle additionalInfo list
        List<String> additionalInfo = em.containsKey(ADDITIONAL_INFO) ?
                em.get(ADDITIONAL_INFO).l().stream()
                        .map(AttributeValue::s)
                        .collect(Collectors.toList()) :
                List.of();

        return new Event(
                id,
                title,
                date,
                location,
                image,
                tickets,
                description,
                additionalInfo,
                organizerId,
                status
        );
    }

    public static Map<String, AttributeValue> toMap(Event event) {
        Map<String, AttributeValue> eventMap = new HashMap<>();

        eventMap.put(ID, AttributeValue.builder().s(event.id()).build());
        eventMap.put(TITLE, AttributeValue.builder().s(event.title()).build());
        eventMap.put(DATE, AttributeValue.builder().s(event.date().toString()).build());
        eventMap.put(LOCATION_ID, AttributeValue.builder().s(event.location().id()).build());
        eventMap.put(LOCATION_NAME, AttributeValue.builder().s(event.location().name()).build());
        eventMap.put(LOCATION_ADDRESS, AttributeValue.builder().s(event.location().address()).build());
        eventMap.put(LOCATION_CITY, AttributeValue.builder().s(event.location().city()).build());
        eventMap.put(LOCATION_COUNTRY, AttributeValue.builder().s(event.location().country()).build());
        eventMap.put(IMAGE_ID, AttributeValue.builder().s(event.image().id()).build());
        eventMap.put(IMAGE_URL, AttributeValue.builder().s(event.image().url()).build());
        eventMap.put(IMAGE_ALT, AttributeValue.builder().s(event.image().alt()).build());
        eventMap.put(DESCRIPTION, AttributeValue.builder().s(event.description()).build());
        eventMap.put(ORGANIZER_ID, AttributeValue.builder().s(event.organizerId()).build());
        eventMap.put(STATUS, AttributeValue.builder().s(event.status().toString()).build());

        List<AttributeValue> ticketsList = event.tickets().stream()
                .map(ticket -> {
                    Map<String, AttributeValue> ticketMap = new HashMap<>();
                    ticketMap.put(TICKETS_ID, AttributeValue.builder().s(ticket.id()).build());
                    ticketMap.put(TICKETS_VALUE, AttributeValue.builder().n(ticket.value().toString()).build());
                    ticketMap.put(TICKETS_CURRENCY, AttributeValue.builder().s(ticket.currency()).build());
                    ticketMap.put(TICKETS_TYPE, AttributeValue.builder().s(ticket.type()).build());
                    ticketMap.put(TICKETS_IS_FREE, AttributeValue.builder().bool(ticket.isFree()).build());
                    ticketMap.put(TICKETS_STOCK, AttributeValue.builder().n(ticket.stock().toString()).build());
                    return AttributeValue.builder().m(ticketMap).build();
                })
                .collect(Collectors.toList());
        eventMap.put(TICKETS, AttributeValue.builder().l(ticketsList).build());

        List<AttributeValue> additionalInfoList = event.additionalInfo().stream()
                .map(info -> AttributeValue.builder().s(info).build())
                .collect(Collectors.toList());
        eventMap.put(ADDITIONAL_INFO, AttributeValue.builder().l(additionalInfoList).build());

        return eventMap;
    }
}
