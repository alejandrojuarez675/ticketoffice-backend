package com.ticketoffice.backend.infra.adapters.in.handlers;

import com.ticketoffice.backend.domain.exception.ErrorOnPersistDataException;
import com.ticketoffice.backend.domain.exception.NotAuthenticatedException;
import com.ticketoffice.backend.domain.exception.ResourceDoesntExistException;
import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.usecases.events.CreateEventUseCase;
import com.ticketoffice.backend.domain.usecases.events.GetAllEventsUseCase;
import com.ticketoffice.backend.domain.usecases.events.UpdateMyEventUseCase;
import com.ticketoffice.backend.infra.adapters.in.dto.mapper.EventCrudRequestMapper;
import com.ticketoffice.backend.infra.adapters.in.dto.mapper.EventLightResponseMapper;
import com.ticketoffice.backend.infra.adapters.in.dto.request.EventCrudRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.response.events.EventLightResponse;
import com.ticketoffice.backend.infra.adapters.in.exception.BadRequestException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EventCrudHandler {

    private final CreateEventUseCase createEventUseCase;
    private final GetAllEventsUseCase getAllEventsUseCase;
    private final UpdateMyEventUseCase updateMyEventUseCase;

    public EventCrudHandler(
            CreateEventUseCase createEventUseCase,
            GetAllEventsUseCase getAllEventsUseCase,
            UpdateMyEventUseCase updateMyEventUseCase) {
        this.createEventUseCase = createEventUseCase;
        this.getAllEventsUseCase = getAllEventsUseCase;
        this.updateMyEventUseCase = updateMyEventUseCase;
    }

    public List<EventLightResponse> findAll() {
        return getAllEventsUseCase.getAllEvents()
            .stream()
            .map(EventLightResponseMapper::getFromEvent)
            .toList();
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
}
