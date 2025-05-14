package com.ticketoffice.backend.application.services;

import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.usecases.events.GetEventUseCase;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class EventService implements GetEventUseCase {

    @Override
    public Optional<Event> getEventById(String id) {
        return Optional.empty();
    }
}
