package com.ticketoffice.backend.domain.usecases.organizer;

import com.ticketoffice.backend.domain.exception.NotAuthenticatedException;
import com.ticketoffice.backend.domain.exception.ResourceDoesntExistException;
import com.ticketoffice.backend.domain.models.Organizer;

public interface GetOrganizerByUserUseCase {

    /**
     * Retrieves the organizer associated with the currently authenticated user.
     *
     * @return The organizer associated with the authenticated user.
     */
    Organizer getOrganizerByUser() throws NotAuthenticatedException, ResourceDoesntExistException;
}
