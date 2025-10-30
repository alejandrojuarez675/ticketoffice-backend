package com.ticketoffice.backend.application.usecases.events;

import com.google.inject.Inject;
import com.ticketoffice.backend.domain.exception.ResourceDoesntExistException;
import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.ports.EventRepository;
import com.ticketoffice.backend.domain.usecases.events.GetEventUseCase;
import com.ticketoffice.backend.domain.usecases.events.GetSimilarEventsToAnEventUseCase;
import com.ticketoffice.backend.domain.utils.EventSimilarSearchParameters;

import java.util.List;

public class GetSimilarEventsToAnEventUseCaseImpl implements GetSimilarEventsToAnEventUseCase {

    private final GetEventUseCase getEventUseCase;
    private final EventRepository eventRepository;

    @Inject
    public GetSimilarEventsToAnEventUseCaseImpl(
            GetEventUseCase getEventUseCase,
            EventRepository eventRepository
    ) {
        this.getEventUseCase = getEventUseCase;
        this.eventRepository = eventRepository;
    }

    @Override
    public List<Event> apply(String eventId, Integer quantity) throws ResourceDoesntExistException {
        Event event = getEventUseCase.apply(eventId)
                .orElseThrow(() -> new ResourceDoesntExistException(String.format("Event with id %s not found", eventId)));
        return eventRepository.search(EventSimilarSearchParameters.of(event), quantity);
    }
}
