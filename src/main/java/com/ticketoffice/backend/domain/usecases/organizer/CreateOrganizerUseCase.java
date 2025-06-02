package com.ticketoffice.backend.domain.usecases.organizer;

import com.ticketoffice.backend.domain.exception.ErrorOnPersistDataException;
import com.ticketoffice.backend.domain.exception.NotAuthenticatedException;
import com.ticketoffice.backend.domain.models.Organizer;
import com.ticketoffice.backend.domain.usecases.UseCase;

@FunctionalInterface
public interface CreateOrganizerUseCase extends UseCase {
    void accept(Organizer organizer) throws NotAuthenticatedException, ErrorOnPersistDataException;
}
