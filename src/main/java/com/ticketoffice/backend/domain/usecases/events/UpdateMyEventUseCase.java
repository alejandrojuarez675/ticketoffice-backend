package com.ticketoffice.backend.domain.usecases.events;

import com.ticketoffice.backend.domain.exception.ErrorOnPersistDataException;
import com.ticketoffice.backend.domain.exception.NotAuthenticatedException;
import com.ticketoffice.backend.domain.exception.ResourceDoesntExistException;
import com.ticketoffice.backend.domain.models.Event;

public interface UpdateMyEventUseCase {
    /**
     * Updates an event with the given ID using the provided event data.
     *
     * @param id    The ID of the event to update.
     * @param event The event data to update.
     * @return The updated event.
     */
    Event updateMyEvent(String id, Event event)
            throws NotAuthenticatedException, ResourceDoesntExistException, ErrorOnPersistDataException;
}
