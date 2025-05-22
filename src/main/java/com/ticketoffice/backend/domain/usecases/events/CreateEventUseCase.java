package com.ticketoffice.backend.domain.usecases.events;

import com.ticketoffice.backend.domain.models.Event;

public interface CreateEventUseCase {
    void createEvent(Event event);
}
