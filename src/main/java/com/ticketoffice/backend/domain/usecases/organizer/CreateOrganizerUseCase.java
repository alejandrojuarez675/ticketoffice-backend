package com.ticketoffice.backend.domain.usecases.organizer;

import com.ticketoffice.backend.domain.exception.ErrorOnPersistDataException;
import com.ticketoffice.backend.domain.exception.NotAuthenticatedException;
import com.ticketoffice.backend.domain.models.Organizer;
import com.ticketoffice.backend.domain.usecases.UseCase;
import io.javalin.http.Context;

@FunctionalInterface
public interface CreateOrganizerUseCase extends UseCase {
    void accept(Context context, Organizer organizer) throws NotAuthenticatedException, ErrorOnPersistDataException;
}
