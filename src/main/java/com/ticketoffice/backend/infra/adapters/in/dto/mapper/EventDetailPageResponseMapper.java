package com.ticketoffice.backend.infra.adapters.in.dto.mapper;

import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.models.Organizer;
import com.ticketoffice.backend.infra.adapters.in.dto.response.events.EventDetailPageResponse;
import com.ticketoffice.backend.infra.adapters.in.dto.shared.TicketDTO;
import java.util.List;
import java.util.Optional;

public class EventDetailPageResponseMapper {

    public static EventDetailPageResponse toResponse(Event event, Organizer organizer, List<TicketDTO> priceListToOverride) {
        List<TicketDTO> ticketDTOS = Optional.ofNullable(priceListToOverride)
                .orElse(
                        Optional.ofNullable(event.tickets())
                                .map(prices -> prices.stream().map(TicketDtoMapper::getFromTicket).toList())
                                .orElse(null)
                );

        return new EventDetailPageResponse(
                event.id(),
                event.title(),
                event.date(),
                LocationDtoMapper.getFromLocation(event.location()),
                event.image() != null ? ImageDtoMapper.getFromImage(event.image()) : null,
                ticketDTOS,
                event.description(),
                event.additionalInfo(),
                OrganizerDtoMapper.getFromOrganizer(organizer),
                event.status().getStatus()
        );
    }

    public static EventDetailPageResponse toResponse(Event event, Organizer organizer) {
        return toResponse(event, organizer, null);
    }
}
