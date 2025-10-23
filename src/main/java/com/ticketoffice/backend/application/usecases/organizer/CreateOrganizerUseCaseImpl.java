package com.ticketoffice.backend.application.usecases.organizer;

import com.google.inject.Inject;
import com.ticketoffice.backend.domain.exception.ErrorOnPersistDataException;
import com.ticketoffice.backend.domain.exception.NotAuthenticatedException;
import com.ticketoffice.backend.domain.models.Organizer;
import com.ticketoffice.backend.domain.models.User;
import com.ticketoffice.backend.domain.usecases.organizer.CreateOrganizerUseCase;
import com.ticketoffice.backend.domain.usecases.users.GetAuthenticatedUserUseCase;
import com.ticketoffice.backend.domain.usecases.users.UpdateOrganizerDataOnUserUseCase;
import io.javalin.http.Context;

public class CreateOrganizerUseCaseImpl implements CreateOrganizerUseCase {

    private final GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;
    private final UpdateOrganizerDataOnUserUseCase updateOrganizerDataOnUserUseCase;

    @Inject
    public CreateOrganizerUseCaseImpl(
            GetAuthenticatedUserUseCase getAuthenticatedUserUseCase,
            UpdateOrganizerDataOnUserUseCase updateOrganizerDataOnUserUseCase) {
        this.getAuthenticatedUserUseCase = getAuthenticatedUserUseCase;
        this.updateOrganizerDataOnUserUseCase = updateOrganizerDataOnUserUseCase;
    }

    @Override
    public void accept(Context context, Organizer organizer) throws NotAuthenticatedException, ErrorOnPersistDataException {
        User userLogged = getAuthenticatedUserUseCase.apply(context)
                .orElseThrow(() -> new NotAuthenticatedException("User is not authenticated"));

        Organizer organizerDomain = updateOrganizer(organizer, userLogged);

        updateOrganizerDataOnUserUseCase.apply(userLogged, organizerDomain)
                .orElseThrow(() -> new ErrorOnPersistDataException("Organizer could not be created"));
    }

    private Organizer updateOrganizer(Organizer organizer, User userLogged) {
        return new Organizer(
                userLogged.getId(),
                organizer.name(),
                organizer.url(),
                organizer.logo()
        );
    }
}
