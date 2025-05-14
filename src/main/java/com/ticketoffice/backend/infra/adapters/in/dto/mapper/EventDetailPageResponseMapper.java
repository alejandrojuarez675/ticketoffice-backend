package com.ticketoffice.backend.infra.adapters.in.dto.mapper;

import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.infra.adapters.in.dto.response.events.EventDetailPageResponse;

public class EventDetailPageResponseMapper {

    public static EventDetailPageResponse getFromEvent(Event event) {
        return new EventDetailPageResponse(
                event.id(),
                event.title(),
                event.date(),
                LocationDtoMapper.getFromLocation(event.location()),
                ImageDtoMapper.getFromImage(event.image()),
                event.prices().stream().map(PriceDtoMapper::getFromPrice).toList(),
                event.description(),
                event.additionalInfo(),
                OrganizerDtoMapper.getFromOrganizer(event.organizer())
        );
    }
}
