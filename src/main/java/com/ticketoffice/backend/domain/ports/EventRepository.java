package com.ticketoffice.backend.domain.ports;

import com.ticketoffice.backend.domain.models.Event;
import java.util.Optional;

public interface EventRepository {

    Optional<Event> getEventById(String id);
}
