package com.ticketoffice.backend.domain.usecases.events;

import com.ticketoffice.backend.domain.exception.ErrorOnPersistDataException;
import com.ticketoffice.backend.domain.exception.NotAuthenticatedException;
import com.ticketoffice.backend.domain.exception.ResourceDoesntExistException;
import com.ticketoffice.backend.domain.models.Event;

public interface CreateEventUseCase {
    Event createEvent(Event event) throws NotAuthenticatedException, ResourceDoesntExistException, ErrorOnPersistDataException;
}
