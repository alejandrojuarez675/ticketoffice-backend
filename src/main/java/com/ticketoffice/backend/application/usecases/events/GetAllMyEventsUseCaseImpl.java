package com.ticketoffice.backend.application.usecases.events;

import com.google.inject.Inject;
import com.ticketoffice.backend.domain.exception.NotAuthenticatedException;
import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.models.User;
import com.ticketoffice.backend.domain.ports.EventRepository;
import com.ticketoffice.backend.domain.usecases.events.GetAllMyEventsUseCase;
import com.ticketoffice.backend.domain.usecases.users.GetAuthenticatedUserUseCase;
import io.javalin.http.Context;

import java.util.List;

public class GetAllMyEventsUseCaseImpl implements GetAllMyEventsUseCase {

    final private EventRepository eventRepository;
    final private GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;

    @Inject
    public GetAllMyEventsUseCaseImpl(
            EventRepository eventRepository,
            GetAuthenticatedUserUseCase getAuthenticatedUserUseCase
    ) {
        this.eventRepository = eventRepository;
        this.getAuthenticatedUserUseCase = getAuthenticatedUserUseCase;
    }

    @Override
    public List<Event> get(Context context) throws NotAuthenticatedException {
        User user = getAuthenticatedUserUseCase.apply(context)
                .orElseThrow(() -> new NotAuthenticatedException("User not authenticated"));

        return user.isAdmin() ? eventRepository.findAll() : eventRepository.findByUserId(user.getId());
    }
}
