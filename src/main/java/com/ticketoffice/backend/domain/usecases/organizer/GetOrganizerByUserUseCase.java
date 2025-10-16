package com.ticketoffice.backend.domain.usecases.organizer;

import com.ticketoffice.backend.domain.exception.NotAuthenticatedException;
import com.ticketoffice.backend.domain.exception.ResourceDoesntExistException;
import com.ticketoffice.backend.domain.models.Organizer;
import com.ticketoffice.backend.domain.usecases.UseCase;
import io.javalin.http.Context;

@FunctionalInterface
public interface GetOrganizerByUserUseCase extends UseCase {
    Organizer get(Context context) throws NotAuthenticatedException, ResourceDoesntExistException;
}
