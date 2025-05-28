package com.ticketoffice.backend.infra.adapters.in.dto.mapper;

import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.infra.adapters.in.dto.response.events.EventLightResponse;

public class EventLightResponseMapper {
    public static EventLightResponse getFromEvent(Event event) {
        return new EventLightResponse(
                event.id(),
                event.title(),
                event.date(),
                event.location().name(),
                event.image() != null ? event.image().url() : null,
                event.getTheCheapestPrice(),
                event.prices().getFirst().currency(), // TODO handle different currencies
                event.status().getStatus()
        );
    }
}
