package com.ticketoffice.backend.infra.adapters.in.handlers;

import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.models.Organizer;
import com.ticketoffice.backend.domain.usecases.events.GetEventUseCase;
import com.ticketoffice.backend.domain.usecases.organizer.GetOrganizerByUserIdUseCase;
import com.ticketoffice.backend.infra.adapters.in.dto.mapper.EventDetailPageResponseMapper;
import com.ticketoffice.backend.infra.adapters.in.dto.response.events.EventDetailPageResponse;
import com.ticketoffice.backend.infra.adapters.in.exception.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class EventDetailPageHandler {

    final private GetEventUseCase getEventUseCase;
    private final GetOrganizerByUserIdUseCase getOrganizerByUserIdUseCase;

    public EventDetailPageHandler(
            GetEventUseCase getEventUseCase,
            GetOrganizerByUserIdUseCase getOrganizerByUserIdUseCase
    ) {
        this.getEventUseCase = getEventUseCase;
        this.getOrganizerByUserIdUseCase = getOrganizerByUserIdUseCase;
    }

    public EventDetailPageResponse getEvent(String id) throws NotFoundException {
        Event event = getEventUseCase.getEventById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id %s not found", id)));

        Organizer organizer = getOrganizerByUserIdUseCase.findByUserId(event.organizerId())
                .orElseThrow(() -> new NotFoundException(String.format("Organizer with id %s not found", event.organizerId())));

        return EventDetailPageResponseMapper.toResponse(event, organizer);
    }
}
