package com.ticketoffice.backend.domain.usecases.organizer;

import com.ticketoffice.backend.domain.exception.NotAuthenticatedException;
import com.ticketoffice.backend.domain.exception.ResourceDoesntExistException;
import com.ticketoffice.backend.domain.models.Organizer;
import com.ticketoffice.backend.domain.usecases.UseCase;

@FunctionalInterface
public interface GetOrganizerByUserUseCase extends UseCase {
    Organizer get() throws NotAuthenticatedException, ResourceDoesntExistException;
}
