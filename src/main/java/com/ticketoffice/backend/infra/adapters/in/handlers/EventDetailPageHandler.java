package com.ticketoffice.backend.infra.adapters.in.handlers;

import com.ticketoffice.backend.domain.usecases.events.GetEventUseCase;
import com.ticketoffice.backend.infra.adapters.in.dto.mapper.EventDetailPageResponseMapper;
import com.ticketoffice.backend.infra.adapters.in.dto.response.events.EventDetailPageResponse;
import com.ticketoffice.backend.infra.adapters.in.exception.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class EventDetailPageHandler {

    final private GetEventUseCase getEventUseCase;

    public EventDetailPageHandler(GetEventUseCase getEventUseCase) {
        this.getEventUseCase = getEventUseCase;
    }

    public EventDetailPageResponse getEvent(String id) throws NotFoundException {
        return getEventUseCase.getEventById(id)
            .map(EventDetailPageResponseMapper::getFromEvent)
            .orElseThrow(() -> new NotFoundException(String.format("Event with id %s not found", id)));
    }
}
