package com.ticketoffice.backend.infra.adapters.in.dto.mapper;

import com.ticketoffice.backend.domain.enums.EventStatus;
import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.infra.adapters.in.dto.request.EventCrudRequest;
import java.util.UUID;

public class EventCrudRequestMapper {
    public static Event toDomain(EventCrudRequest eventCrudRequest) {
        return new Event(
                UUID.randomUUID().toString(),
                eventCrudRequest.title(),
                eventCrudRequest.date(),
                LocationDtoMapper.getFromLocationDTO(eventCrudRequest.location()),
                ImageDtoMapper.getFromImageDTO(eventCrudRequest.image()),
                eventCrudRequest.tickets().stream().map(TicketDtoMapper::getFromTicketDTO).toList(),
                eventCrudRequest.description(),
                eventCrudRequest.additionalInfo(),
                null,
                EventStatus.ACTIVE
        );
    }
}
