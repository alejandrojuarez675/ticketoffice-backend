package com.ticketoffice.backend.infra.adapters.in.dto.mapper;

import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.models.Organizer;
import com.ticketoffice.backend.infra.adapters.in.dto.response.events.EventDetailPageResponse;
import com.ticketoffice.backend.infra.adapters.in.dto.shared.PriceDTO;
import java.util.List;
import java.util.Optional;

public class EventDetailPageResponseMapper {

    public static EventDetailPageResponse toResponse(Event event, Organizer organizer, List<PriceDTO> priceListToOverride) {
        List<PriceDTO> priceDTOS = Optional.ofNullable(priceListToOverride)
                .orElse(
                        Optional.ofNullable(event.prices())
                                .map(prices -> prices.stream().map(PriceDtoMapper::getFromPrice).toList())
                                .orElse(null)
                );

        return new EventDetailPageResponse(
                event.id(),
                event.title(),
                event.date(),
                LocationDtoMapper.getFromLocation(event.location()),
                event.image() != null ? ImageDtoMapper.getFromImage(event.image()) : null,
                priceDTOS,
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
