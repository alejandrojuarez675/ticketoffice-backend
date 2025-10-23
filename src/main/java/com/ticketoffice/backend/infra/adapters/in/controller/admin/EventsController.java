package com.ticketoffice.backend.infra.adapters.in.controller.admin;

import com.google.inject.Inject;
import com.ticketoffice.backend.domain.exception.NotAuthenticatedException;
import com.ticketoffice.backend.infra.adapters.in.controller.CustomController;
import com.ticketoffice.backend.infra.adapters.in.controller.UserRoleValidator;
import com.ticketoffice.backend.infra.adapters.in.dto.request.EventCrudRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.request.validators.EventCrudRequestValidator;
import com.ticketoffice.backend.infra.adapters.in.dto.response.events.EventDetailPageResponse;
import com.ticketoffice.backend.infra.adapters.in.dto.response.events.EventListResponse;
import com.ticketoffice.backend.infra.adapters.in.dto.response.events.EventLightResponse;
import com.ticketoffice.backend.infra.adapters.in.exception.BadRequestException;
import com.ticketoffice.backend.infra.adapters.in.exception.NotFoundException;
import com.ticketoffice.backend.infra.adapters.in.exception.UnauthorizedUserException;
import com.ticketoffice.backend.infra.adapters.in.handlers.EventCrudHandler;
import com.ticketoffice.backend.infra.adapters.in.utils.IdValidator;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.jetbrains.annotations.NotNull;

public class EventsController implements CustomController {

    private static final String PATH = "/api/v1/events";
    private final EventCrudHandler eventCrudHandler;
    private final UserRoleValidator userRoleValidator;

    @Inject
    public EventsController(EventCrudHandler eventCrudHandler, UserRoleValidator userRoleValidator) {
        this.eventCrudHandler = eventCrudHandler;
        this.userRoleValidator = userRoleValidator;
    }

    @Override
    public void registeredRoutes(Javalin app) {
        app.get(PATH, context -> context.json(getEvents(context)));
        app.get(PATH + "/{eventId}", context -> {
            String id = context.pathParam("eventId");
            context.json(getEventById(context, id));
        });
        app.post(PATH, context -> {
            EventCrudRequest eventCrudRequest = context.bodyAsClass(EventCrudRequest.class);
            postEvents(context, eventCrudRequest);
            context.status(HttpStatus.CREATED);
        });
        app.put(PATH + "/{eventId}", context -> {
            String id = context.pathParam("eventId");
            EventCrudRequest eventCrudRequest = context.bodyAsClass(EventCrudRequest.class);
            context.json(putEvents(context, id, eventCrudRequest));
        });
        app.delete(PATH + "/{eventId}", context -> {
            String id = context.pathParam("eventId");
            deleteEvent(context, id);
            context.status(HttpStatus.NO_CONTENT);
        });
    }

    private EventListResponse getEvents(@NotNull Context context) throws UnauthorizedUserException {
        userRoleValidator.validateThatUserIsSeller(context);
        return new EventListResponse(eventCrudHandler.findAll(context));
    }

    private EventDetailPageResponse getEventById(@NotNull Context context, String id) throws UnauthorizedUserException, BadRequestException, NotAuthenticatedException, NotFoundException {
        userRoleValidator.validateThatUserIsSeller(context);
        IdValidator.validateIdFromParams(id, "seler_id", true);
        return eventCrudHandler.getEventById(context, id);
    }

    private void postEvents(@NotNull Context context, EventCrudRequest event) throws BadRequestException, UnauthorizedUserException {
        userRoleValidator.validateThatUserIsSeller(context);
        new EventCrudRequestValidator().validate(event);
        eventCrudHandler.create(context, event);
    }

    private EventLightResponse putEvents(@NotNull Context context, String id, EventCrudRequest event) throws BadRequestException, UnauthorizedUserException {
        userRoleValidator.validateThatUserIsSeller(context);
        IdValidator.validateIdFromParams(id, "id", true);
        return eventCrudHandler.updateMyEvent(context, id, event);
    }

    private void deleteEvent(@NotNull Context context, String id)
            throws UnauthorizedUserException, BadRequestException, NotAuthenticatedException {
        userRoleValidator.validateThatUserIsSeller(context);
        IdValidator.validateIdFromParams(id, "id", true);
        eventCrudHandler.deleteById(context, id);
    }
}
