package com.ticketoffice.backend.infra.adapters.in.dto.mapper;

import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.infra.adapters.in.dto.response.events.EventDetailPageResponse;
import java.util.List;

public class EventDetailPageResponseMapper {

    public static EventDetailPageResponse getFromEvent(Event event) {
        return new EventDetailPageResponse(
                event.id(),
                event.title(),
                event.date(),
                LocationDtoMapper.getFromLocation(event.location()),
                event.image() != null ? ImageDtoMapper.getFromImage(event.image()) : null,
                event.prices() != null ? event.prices().stream().map(PriceDtoMapper::getFromPrice).toList() : List.of(),
                event.description(),
                event.additionalInfo(),
                OrganizerDtoMapper.getFromOrganizer(event.organizer())
        );
    }
}
