package com.ticketoffice.backend.application.usecases.events;

import com.ticketoffice.backend.domain.ports.EventRepository;
import com.ticketoffice.backend.domain.usecases.events.CountEventsByParamsUseCase;
import com.ticketoffice.backend.domain.utils.EventSearchParameters;
import org.springframework.stereotype.Service;

@Service
public class CountEventsByParamsUseCaseImpl implements CountEventsByParamsUseCase {

    private final EventRepository eventRepository;

    public CountEventsByParamsUseCaseImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Integer apply(EventSearchParameters eventSearchParameters) {
        return eventRepository.count(eventSearchParameters.getPredicates());
    }
}
