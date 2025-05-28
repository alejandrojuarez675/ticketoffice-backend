package com.ticketoffice.backend.domain.usecases.events;

import com.ticketoffice.backend.domain.exception.NotAuthenticatedException;
import com.ticketoffice.backend.domain.models.Event;
import java.util.List;

public interface GetAllEventsUseCase {
    List<Event> getAllEvents() throws NotAuthenticatedException;
}
