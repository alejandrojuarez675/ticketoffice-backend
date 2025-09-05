package com.ticketoffice.backend.application.usecases.events;

import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.ports.EventRepository;
import com.ticketoffice.backend.domain.usecases.events.GetEventsByParamsUseCase;
import com.ticketoffice.backend.domain.utils.EventSearchParameters;
import java.util.List;

public class GetEventsByParamsUseCaseImpl implements GetEventsByParamsUseCase {

    private final EventRepository eventRepository;

    public GetEventsByParamsUseCaseImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public List<Event> apply(EventSearchParameters eventSearchParameters, Integer pageSize, Integer pageNumber) {
        return eventRepository.search(eventSearchParameters.getPredicate(), pageSize, pageNumber);
    }
}
