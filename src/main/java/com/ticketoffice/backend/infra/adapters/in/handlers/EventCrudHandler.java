package com.ticketoffice.backend.infra.adapters.in.handlers;

import com.ticketoffice.backend.domain.usecases.events.CreateEventUseCase;
import com.ticketoffice.backend.domain.usecases.events.GetEventUseCase;
import com.ticketoffice.backend.infra.adapters.in.dto.mapper.EventCrudRequestMapper;
import com.ticketoffice.backend.infra.adapters.in.dto.mapper.EventDetailPageResponseMapper;
import com.ticketoffice.backend.infra.adapters.in.dto.request.EventCrudRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.response.events.EventDetailPageResponse;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EventCrudHandler {

    private final CreateEventUseCase createEventUseCase;
    private final GetEventUseCase getEventUseCase;

    public EventCrudHandler(CreateEventUseCase createEventUseCase, GetEventUseCase getEventUseCase) {
        this.createEventUseCase = createEventUseCase;
        this.getEventUseCase = getEventUseCase;
    }

    public List<EventDetailPageResponse> findAll() {
        return getEventUseCase.getAllEvents()
            .stream()
            .map(EventDetailPageResponseMapper::getFromEvent)
            .toList();
    }

    public void create(EventCrudRequest event) {
        createEventUseCase.createEvent(EventCrudRequestMapper.toDomain(event));
    }

}
