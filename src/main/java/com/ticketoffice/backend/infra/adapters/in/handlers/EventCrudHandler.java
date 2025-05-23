package com.ticketoffice.backend.infra.adapters.in.handlers;

import com.ticketoffice.backend.domain.usecases.events.CreateEventUseCase;
import com.ticketoffice.backend.domain.usecases.events.GetAllEventsUseCase;
import com.ticketoffice.backend.domain.usecases.events.GetEventUseCase;
import com.ticketoffice.backend.infra.adapters.in.dto.mapper.EventCrudRequestMapper;
import com.ticketoffice.backend.infra.adapters.in.dto.mapper.EventDetailPageResponseMapper;
import com.ticketoffice.backend.infra.adapters.in.dto.mapper.EventLightResponseMapper;
import com.ticketoffice.backend.infra.adapters.in.dto.request.EventCrudRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.response.events.EventDetailPageResponse;
import com.ticketoffice.backend.infra.adapters.in.dto.response.events.EventLightResponse;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EventCrudHandler {

    private final CreateEventUseCase createEventUseCase;
    private final GetAllEventsUseCase getAllEventsUseCase;

    public EventCrudHandler(CreateEventUseCase createEventUseCase, GetAllEventsUseCase getAllEventsUseCase) {
        this.createEventUseCase = createEventUseCase;
        this.getAllEventsUseCase = getAllEventsUseCase;
    }

    public List<EventLightResponse> findAll() {
        return getAllEventsUseCase.getAllEvents()
            .stream()
            .map(EventLightResponseMapper::getFromEvent)
            .toList();
    }

    public void create(EventCrudRequest event) {
        createEventUseCase.createEvent(EventCrudRequestMapper.toDomain(event));
    }

}
