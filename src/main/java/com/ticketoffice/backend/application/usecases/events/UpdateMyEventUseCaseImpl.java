package com.ticketoffice.backend.application.usecases.events;

import com.ticketoffice.backend.domain.exception.ErrorOnPersistDataException;
import com.ticketoffice.backend.domain.exception.NotAuthenticatedException;
import com.ticketoffice.backend.domain.exception.ResourceDoesntExistException;
import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.models.User;
import com.ticketoffice.backend.domain.ports.EventRepository;
import com.ticketoffice.backend.domain.usecases.events.UpdateMyEventUseCase;
import com.ticketoffice.backend.domain.usecases.users.GetAuthenticatedUserUseCase;
import org.springframework.stereotype.Service;

@Service
public class UpdateMyEventUseCaseImpl implements UpdateMyEventUseCase {

    private final EventRepository eventRepository;
    private final GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;

    public UpdateMyEventUseCaseImpl(
            EventRepository eventRepository,
            GetAuthenticatedUserUseCase getAuthenticatedUserUseCase
    ) {
        this.eventRepository = eventRepository;
        this.getAuthenticatedUserUseCase = getAuthenticatedUserUseCase;
    }

    @Override
    public Event apply(
            String id, Event event
    ) throws NotAuthenticatedException, ResourceDoesntExistException, ErrorOnPersistDataException {
        User userLogged = getAuthenticatedUserUseCase.get()
                .orElseThrow(() -> new NotAuthenticatedException("User is not authenticated"));

        Event eventToUpdate = (
                userLogged.isAdmin()
                        ? eventRepository.getById(id)
                        : eventRepository.getByIdAndOrganizerId(id, userLogged.getId()))
                .orElseThrow(() -> new ResourceDoesntExistException("Event not found"));

        Event updatedEvent = overrideFields(eventToUpdate, event, userLogged);

        return eventRepository.update(id, updatedEvent)
                .orElseThrow(() -> new ErrorOnPersistDataException("Event could not be updated"));
    }

    private Event overrideFields(Event eventToUpdate, Event event, User userLogged) {
        return new Event(
                eventToUpdate.id(),
                event.title(),
                event.date(),
                event.location(),
                event.image(),
                event.prices(),
                event.description(),
                event.additionalInfo(),
                userLogged.isAdmin() ? eventToUpdate.organizerId() : userLogged.getId(),
                event.status()
        );
    }
}
