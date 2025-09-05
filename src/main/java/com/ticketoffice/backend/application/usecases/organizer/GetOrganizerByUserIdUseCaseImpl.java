package com.ticketoffice.backend.application.usecases.organizer;

import com.ticketoffice.backend.domain.models.Organizer;
import com.ticketoffice.backend.domain.models.User;
import com.ticketoffice.backend.domain.usecases.organizer.GetOrganizerByUserIdUseCase;
import com.ticketoffice.backend.domain.usecases.users.GetUserByIdUseCase;
import java.util.Optional;

public class GetOrganizerByUserIdUseCaseImpl implements GetOrganizerByUserIdUseCase {

    private final GetUserByIdUseCase getUserByIdUseCase;

    public GetOrganizerByUserIdUseCaseImpl(GetUserByIdUseCase getUserByIdUseCase) {
        this.getUserByIdUseCase = getUserByIdUseCase;
    }

    @Override
    public Optional<Organizer> apply(String userId) {
        return getUserByIdUseCase.apply(userId)
                .flatMap(User::getOrganizer);
    }
}
