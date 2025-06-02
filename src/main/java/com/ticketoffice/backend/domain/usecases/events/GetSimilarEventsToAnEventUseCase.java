package com.ticketoffice.backend.domain.usecases.events;

import com.ticketoffice.backend.domain.exception.ResourceDoesntExistException;
import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.usecases.UseCase;
import java.util.List;

@FunctionalInterface
public interface GetSimilarEventsToAnEventUseCase extends UseCase {
    List<Event> apply(String eventId, Integer quantity) throws ResourceDoesntExistException;
}
