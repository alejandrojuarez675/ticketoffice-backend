package com.ticketoffice.backend.application.usecases.events;

import com.ticketoffice.backend.domain.exception.NotAuthenticatedException;
import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.models.User;
import com.ticketoffice.backend.domain.ports.EventRepository;
import com.ticketoffice.backend.domain.usecases.events.GetAllEventsUseCase;
import com.ticketoffice.backend.domain.usecases.users.GetAuthenticatedUserUseCase;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GetAllEventsUseCaseImpl implements GetAllEventsUseCase {

    final private EventRepository eventRepository;
    final private GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;

    public GetAllEventsUseCaseImpl(
            EventRepository eventRepository,
            GetAuthenticatedUserUseCase getAuthenticatedUserUseCase
    ) {
        this.eventRepository = eventRepository;
        this.getAuthenticatedUserUseCase = getAuthenticatedUserUseCase;
    }

    @Override
    public List<Event> getAllEvents() throws NotAuthenticatedException {
        String userId = getAuthenticatedUserUseCase.getAuthenticatedUser()
                .map(User::getId)
                .orElseThrow(() -> new NotAuthenticatedException("User not authenticated"));

        return eventRepository.findByUserId(userId);
    }
}
