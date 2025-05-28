package com.ticketoffice.backend.infra.adapters.in.handlers;

import com.ticketoffice.backend.domain.exception.ErrorOnPersistDataException;
import com.ticketoffice.backend.domain.exception.NotAuthenticatedException;
import com.ticketoffice.backend.domain.exception.ResourceDoesntExistException;
import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.models.Organizer;
import com.ticketoffice.backend.domain.usecases.events.CreateEventUseCase;
import com.ticketoffice.backend.domain.usecases.events.DeleteMyEventUseCase;
import com.ticketoffice.backend.domain.usecases.events.GetAllMyEventsUseCase;
import com.ticketoffice.backend.domain.usecases.events.GetMyEventUseCase;
import com.ticketoffice.backend.domain.usecases.events.UpdateMyEventUseCase;
import com.ticketoffice.backend.domain.usecases.organizer.GetOrganizerByUserIdUseCase;
import com.ticketoffice.backend.infra.adapters.in.dto.mapper.EventCrudRequestMapper;
import com.ticketoffice.backend.infra.adapters.in.dto.mapper.EventDetailPageResponseMapper;
import com.ticketoffice.backend.infra.adapters.in.dto.mapper.EventLightResponseMapper;
import com.ticketoffice.backend.infra.adapters.in.dto.request.EventCrudRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.response.events.EventDetailPageResponse;
import com.ticketoffice.backend.infra.adapters.in.dto.response.events.EventLightResponse;
import com.ticketoffice.backend.infra.adapters.in.exception.BadRequestException;
import com.ticketoffice.backend.infra.adapters.in.exception.NotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EventCrudHandler {

    private final CreateEventUseCase createEventUseCase;
    private final GetAllMyEventsUseCase getAllMyEventsUseCase;
    private final UpdateMyEventUseCase updateMyEventUseCase;
    private final GetMyEventUseCase getMyEventUseCase;
    private final GetOrganizerByUserIdUseCase getOrganizerByUserIdUseCase;
    private final DeleteMyEventUseCase deleteMyEventUseCase;

    public EventCrudHandler(
            CreateEventUseCase createEventUseCase,
            GetAllMyEventsUseCase getAllMyEventsUseCase,
            UpdateMyEventUseCase updateMyEventUseCase,
            GetMyEventUseCase getMyEventUseCase,
            GetOrganizerByUserIdUseCase getOrganizerByUserIdUseCase,
            DeleteMyEventUseCase deleteMyEventUseCase
    ) {
        this.createEventUseCase = createEventUseCase;
        this.getAllMyEventsUseCase = getAllMyEventsUseCase;
        this.updateMyEventUseCase = updateMyEventUseCase;
        this.getMyEventUseCase = getMyEventUseCase;
        this.getOrganizerByUserIdUseCase = getOrganizerByUserIdUseCase;
        this.deleteMyEventUseCase = deleteMyEventUseCase;
    }

    public List<EventLightResponse> findAll() {
        try {
            return getAllMyEventsUseCase.getAllEvents()
                .stream()
                .map(EventLightResponseMapper::getFromEvent)
                .toList();
        } catch (NotAuthenticatedException e) {
            return List.of();
        }
    }

    public void create(EventCrudRequest event) throws BadRequestException {
        try {
            createEventUseCase.createEvent(EventCrudRequestMapper.toDomain(event));
        } catch (NotAuthenticatedException e) {
            throw new BadRequestException("User is not authenticated");
        } catch (ResourceDoesntExistException e) {
            throw new BadRequestException("User is not an organizer");
        } catch (ErrorOnPersistDataException e) {
            throw new RuntimeException(e);
        }
    }

    public EventLightResponse updateMyEvent(
            String id, EventCrudRequest event
    ) throws BadRequestException {
        Event eventDomain = EventCrudRequestMapper.toDomain(event);
        Event eventResponse;
        try {
            eventResponse = updateMyEventUseCase.updateMyEvent(id, eventDomain);
        } catch (NotAuthenticatedException e) {
            throw new BadRequestException("User is not authenticated");
        } catch (ResourceDoesntExistException e) {
            throw new BadRequestException("Event with id " + id + " does not exist for the authenticated user");
        } catch (ErrorOnPersistDataException e) {
            throw new RuntimeException(e);
        }
        return EventLightResponseMapper.getFromEvent(eventResponse);
    }

    public EventDetailPageResponse getEventById(String id) throws NotAuthenticatedException, NotFoundException {
        Event event = getMyEventUseCase.getMyEventById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id %s not found", id)));

        Organizer organizer = getOrganizerByUserIdUseCase.findByUserId(event.organizerId())
                .orElse(new Organizer(event.organizerId(), null, null, null));

        return EventDetailPageResponseMapper.toResponse(event, organizer);
    }

    public void deleteById(String id) throws NotAuthenticatedException {
        try {
            deleteMyEventUseCase.deleteMyEvent(id);
        } catch (ErrorOnPersistDataException e) {
            throw new RuntimeException(e);
        }
    }
}
