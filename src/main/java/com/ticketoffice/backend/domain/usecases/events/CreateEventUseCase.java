package com.ticketoffice.backend.domain.usecases.events;

import com.ticketoffice.backend.domain.exception.ErrorOnPersistDataException;
import com.ticketoffice.backend.domain.exception.NotAuthenticatedException;
import com.ticketoffice.backend.domain.exception.ResourceDoesntExistException;
import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.usecases.UseCase;
import io.javalin.http.Context;

@FunctionalInterface
public interface CreateEventUseCase extends UseCase {
    Event apply(Context context, Event event) throws NotAuthenticatedException, ResourceDoesntExistException, ErrorOnPersistDataException;
}
