package com.ticketoffice.backend.application.usecases.events;

import com.ticketoffice.backend.domain.exception.ErrorOnPersistDataException;
import com.ticketoffice.backend.domain.exception.NotAuthenticatedException;
import com.ticketoffice.backend.domain.exception.ResourceDoesntExistException;
import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.models.Organizer;
import com.ticketoffice.backend.domain.ports.EventRepository;
import com.ticketoffice.backend.domain.usecases.events.CreateEventUseCase;
import com.ticketoffice.backend.domain.usecases.organizer.GetOrganizerByUserUseCase;

public class CreateEventUseCaseImpl implements CreateEventUseCase {

    final private EventRepository eventRepository;
    final private GetOrganizerByUserUseCase getOrganizerByUserUseCase;

    public CreateEventUseCaseImpl(
            EventRepository eventRepository, GetOrganizerByUserUseCase getOrganizerByUserUseCase
    ) {
        this.eventRepository = eventRepository;
        this.getOrganizerByUserUseCase = getOrganizerByUserUseCase;
    }

    @Override
    public Event apply(Event event) throws NotAuthenticatedException, ResourceDoesntExistException, ErrorOnPersistDataException {
        Organizer organizer = getOrganizerByUserUseCase.get();
        Event eventToCreate = new Event(
                null,
                event.title(),
                event.date(),
                event.location(),
                event.image(),
                event.tickets(),
                event.description(),
                event.additionalInfo(),
                organizer.id(),
                event.status()
        );
        return eventRepository.save(eventToCreate)
                .orElseThrow(() -> new ErrorOnPersistDataException("Event could not be created"));
    }
}
