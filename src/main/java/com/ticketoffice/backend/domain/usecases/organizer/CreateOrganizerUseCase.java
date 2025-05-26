package com.ticketoffice.backend.domain.usecases.organizer;

import com.ticketoffice.backend.domain.exception.ErrorOnPersistDataException;
import com.ticketoffice.backend.domain.exception.NotAuthenticatedException;
import com.ticketoffice.backend.domain.models.Organizer;

public interface CreateOrganizerUseCase {
    void createOrganizer(Organizer organizer) throws NotAuthenticatedException, ErrorOnPersistDataException;
}
