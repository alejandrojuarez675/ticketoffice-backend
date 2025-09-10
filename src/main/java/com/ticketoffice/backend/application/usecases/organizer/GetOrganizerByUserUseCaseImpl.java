package com.ticketoffice.backend.application.usecases.organizer;

import com.google.inject.Inject;
import com.ticketoffice.backend.domain.enums.UserRole;
import com.ticketoffice.backend.domain.exception.NotAuthenticatedException;
import com.ticketoffice.backend.domain.exception.ResourceDoesntExistException;
import com.ticketoffice.backend.domain.models.Organizer;
import com.ticketoffice.backend.domain.models.User;
import com.ticketoffice.backend.domain.usecases.organizer.GetOrganizerByUserUseCase;
import com.ticketoffice.backend.domain.usecases.users.GetAuthenticatedUserUseCase;

public class GetOrganizerByUserUseCaseImpl implements GetOrganizerByUserUseCase {

    private final GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;

    @Inject
    public GetOrganizerByUserUseCaseImpl(GetAuthenticatedUserUseCase getAuthenticatedUserUseCase) {
        this.getAuthenticatedUserUseCase = getAuthenticatedUserUseCase;
    }

    @Override
    public Organizer get() throws NotAuthenticatedException, ResourceDoesntExistException {
        User userLogged = getAuthenticatedUserUseCase.get()
                .orElseThrow(() -> new NotAuthenticatedException("User is not authenticated"));

        if (userLogged.getRole().stream().noneMatch(UserRole.SELLER::equals)) {
            throw new ResourceDoesntExistException("User is not an organizer");
        }

        return userLogged.getOrganizer()
                .orElse(new Organizer(userLogged.getId(), null, null, null));
    }
}
