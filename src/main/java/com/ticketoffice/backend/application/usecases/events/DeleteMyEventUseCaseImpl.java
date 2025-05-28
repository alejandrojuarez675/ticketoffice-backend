package com.ticketoffice.backend.application.usecases.events;

import com.ticketoffice.backend.domain.enums.EventStatus;
import com.ticketoffice.backend.domain.exception.ErrorOnPersistDataException;
import com.ticketoffice.backend.domain.exception.NotAuthenticatedException;
import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.models.User;
import com.ticketoffice.backend.domain.ports.EventRepository;
import com.ticketoffice.backend.domain.usecases.events.DeleteMyEventUseCase;
import com.ticketoffice.backend.domain.usecases.users.GetAuthenticatedUserUseCase;
import org.springframework.stereotype.Service;

@Service
public class DeleteMyEventUseCaseImpl implements DeleteMyEventUseCase {

    private final GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;
    private final EventRepository eventRepository;

    public DeleteMyEventUseCaseImpl(
            GetAuthenticatedUserUseCase getAuthenticatedUserUseCase,
            EventRepository eventRepository
    ) {
        this.getAuthenticatedUserUseCase = getAuthenticatedUserUseCase;
        this.eventRepository = eventRepository;
    }

    @Override
    public void deleteMyEvent(String id) throws NotAuthenticatedException, ErrorOnPersistDataException {
        User userLogged = getAuthenticatedUserUseCase.getAuthenticatedUser()
                .orElseThrow(() -> new NotAuthenticatedException("User not authenticated"));

        eventRepository.getById(id)
                .filter(event -> userLogged.isAdmin() || event.organizerId().equals(userLogged.getId()))
                .map(this::updateEventToInactive)
                .map(event -> eventRepository.update(event.id(), event))
                .orElseThrow(() -> new ErrorOnPersistDataException("Event could not be deleted"));
    }

    private Event updateEventToInactive(Event event) {
        return event.getCopyWithUpdatedStatus(EventStatus.INACTIVE);
    }
}
