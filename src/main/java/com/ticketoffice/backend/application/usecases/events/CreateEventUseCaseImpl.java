package com.ticketoffice.backend.application.usecases.events;

import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.ports.EventRepository;
import com.ticketoffice.backend.domain.usecases.events.CreateEventUseCase;
import org.springframework.stereotype.Service;

@Service
public class CreateEventUseCaseImpl implements CreateEventUseCase {

    final private EventRepository eventRepository;

    public CreateEventUseCaseImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public void createEvent(Event event) {
        eventRepository.save(event);
    }
}
