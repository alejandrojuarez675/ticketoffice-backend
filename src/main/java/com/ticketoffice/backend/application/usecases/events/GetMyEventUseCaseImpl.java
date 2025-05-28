package com.ticketoffice.backend.application.usecases.events;

import com.ticketoffice.backend.domain.exception.NotAuthenticatedException;
import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.models.User;
import com.ticketoffice.backend.domain.usecases.events.GetEventUseCase;
import com.ticketoffice.backend.domain.usecases.events.GetMyEventUseCase;
import com.ticketoffice.backend.domain.usecases.users.GetAuthenticatedUserUseCase;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class GetMyEventUseCaseImpl implements GetMyEventUseCase {

    private final GetEventUseCase getEventUseCase;
    private final GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;

    public GetMyEventUseCaseImpl(
            GetEventUseCase getEventUseCase,
            GetAuthenticatedUserUseCase getAuthenticatedUserUseCase
    ) {
        this.getEventUseCase = getEventUseCase;
        this.getAuthenticatedUserUseCase = getAuthenticatedUserUseCase;
    }

    @Override
    public Optional<Event> getMyEventById(String id) throws NotAuthenticatedException {
        User user = getAuthenticatedUserUseCase.getAuthenticatedUser()
                .orElseThrow(() -> new NotAuthenticatedException("User not authenticated"));

        return getEventUseCase.getEventById(id)
                .filter(event -> user.isAdmin() || event.organizerId().equals(user.getId()));
    }
}
