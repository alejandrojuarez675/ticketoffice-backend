package com.ticketoffice.backend.domain.usecases.events;

import com.ticketoffice.backend.domain.models.Event;
import java.util.Optional;

public interface GetEventUseCase {
    Optional<Event> getEventById(String id);
}
