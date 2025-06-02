package com.ticketoffice.backend.application.usecases.events;

import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.ports.EventRepository;
import com.ticketoffice.backend.domain.usecases.events.GetEventUseCase;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class GetEventUseCaseImpl implements GetEventUseCase {

    final private EventRepository eventRepository;

    public GetEventUseCaseImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Optional<Event> apply(String id) {
        return eventRepository.getById(id);
    }
}
