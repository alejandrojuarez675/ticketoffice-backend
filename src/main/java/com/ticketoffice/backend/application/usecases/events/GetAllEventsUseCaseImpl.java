package com.ticketoffice.backend.application.usecases.events;

import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.ports.EventRepository;
import com.ticketoffice.backend.domain.usecases.events.GetAllEventsUseCase;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GetAllEventsUseCaseImpl implements GetAllEventsUseCase {

    final private EventRepository eventRepository;

    public GetAllEventsUseCaseImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
}
