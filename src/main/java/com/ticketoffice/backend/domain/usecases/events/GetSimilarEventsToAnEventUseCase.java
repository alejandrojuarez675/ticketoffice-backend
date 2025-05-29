package com.ticketoffice.backend.domain.usecases.events;

import com.ticketoffice.backend.domain.exception.ResourceDoesntExistException;
import com.ticketoffice.backend.domain.models.Event;
import java.util.List;

public interface GetSimilarEventsToAnEventUseCase {
    List<Event> getSimilarEventsToAnEvent(String eventId, Integer quantity) throws ResourceDoesntExistException;
}
