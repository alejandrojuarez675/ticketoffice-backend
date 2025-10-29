package com.ticketoffice.backend.infra.adapters.out.db.mapper;

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

    private EventDynamoDBMapper() {}

    public static Event fromMap(Map<String, AttributeValue> em) {
        // Extract basic fields
        String id = em.get("id").s();
        String title = em.get("title").s();
        LocalDateTime date = LocalDateTime.parse(em.get("date").s());
        String description = em.get("description").s();
        String organizerId = em.get("organizerId").s();
        EventStatus status = EventStatus.valueOf(em.get("status").s());

        // Build Location object
        Location location = new Location(
                em.containsKey("locationId") ? em.get("locationId").s() : "",
                em.containsKey("locationName") ? em.get("locationName").s() : "",
                em.containsKey("locationAddress") ? em.get("locationAddress").s() : "",
                em.get("city").s(),
                em.get("country").s()
        );

        // Build Image object
        Image image = new Image(
                em.containsKey("imageId") ? em.get("imageId").s() : "",
                em.containsKey("imageUrl") ? em.get("imageUrl").s() : "",
                em.containsKey("imageAlt") ? em.get("imageAlt").s() : ""
        );

        // Handle tickets list
        List<Ticket> tickets = em.containsKey("tickets") ?
                em.get("tickets").l().stream()
                        .map(av -> {
                            Map<String, AttributeValue> ticketMap = av.m();
                            return new Ticket(
                                    ticketMap.get("id").s(),
                                    Double.parseDouble(ticketMap.get("value").n()),
                                    ticketMap.get("currency").s(),
                                    ticketMap.get("type").s(),
                                    Boolean.getBoolean(ticketMap.get("isFree").s()),
                                    Integer.parseInt(ticketMap.get("stock").n())
                            );
                        })
                        .collect(Collectors.toList()) :
                List.of();

        // Handle additionalInfo list
        List<String> additionalInfo = em.containsKey("additionalInfo") ?
                em.get("additionalInfo").l().stream()
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

        eventMap.put("id", AttributeValue.builder().s(event.id()).build());
        eventMap.put("title", AttributeValue.builder().s(event.title()).build());
        eventMap.put("date", AttributeValue.builder().s(event.date().toString()).build());
        eventMap.put("locationId", AttributeValue.builder().s(event.location().id()).build());
        eventMap.put("locationName", AttributeValue.builder().s(event.location().name()).build());
        eventMap.put("locationAddress", AttributeValue.builder().s(event.location().address()).build());
        eventMap.put("city", AttributeValue.builder().s(event.location().city()).build());
        eventMap.put("country", AttributeValue.builder().s(event.location().country()).build());
        eventMap.put("imageId", AttributeValue.builder().s(event.image().id()).build());
        eventMap.put("imageUrl", AttributeValue.builder().s(event.image().url()).build());
        eventMap.put("imageAlt", AttributeValue.builder().s(event.image().alt()).build());
        eventMap.put("description", AttributeValue.builder().s(event.description()).build());
        eventMap.put("organizerId", AttributeValue.builder().s(event.organizerId()).build());
        eventMap.put("status", AttributeValue.builder().s(event.status().toString()).build());

        List<AttributeValue> ticketsList = event.tickets().stream()
                .map(ticket -> {
                    Map<String, AttributeValue> ticketMap = new HashMap<>();
                    ticketMap.put("id", AttributeValue.builder().s(ticket.id()).build());
                    ticketMap.put("value", AttributeValue.builder().n(ticket.value().toString()).build());
                    ticketMap.put("currency", AttributeValue.builder().s(ticket.currency()).build());
                    ticketMap.put("type", AttributeValue.builder().s(ticket.type()).build());
                    ticketMap.put("isFree", AttributeValue.builder().bool(ticket.isFree()).build());
                    ticketMap.put("stock", AttributeValue.builder().n(ticket.stock().toString()).build());
                    return AttributeValue.builder().m(ticketMap).build();
                })
                .collect(Collectors.toList());
        eventMap.put("tickets", AttributeValue.builder().l(ticketsList).build());

        List<AttributeValue> additionalInfoList = event.additionalInfo().stream()
                .map(info -> AttributeValue.builder().s(info).build())
                .collect(Collectors.toList());
        eventMap.put("additionalInfo", AttributeValue.builder().l(additionalInfoList).build());

        return eventMap;
    }
}
