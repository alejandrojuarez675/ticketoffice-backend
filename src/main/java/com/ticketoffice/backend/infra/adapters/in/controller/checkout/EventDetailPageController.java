package com.ticketoffice.backend.infra.adapters.in.controller.checkout;

import com.google.inject.Inject;
import com.ticketoffice.backend.infra.adapters.in.controller.CustomController;
import com.ticketoffice.backend.infra.adapters.in.dto.response.events.EventDetailPageResponse;
import com.ticketoffice.backend.infra.adapters.in.dto.response.events.EventLightResponse;
import com.ticketoffice.backend.infra.adapters.in.exception.BadRequestException;
import com.ticketoffice.backend.infra.adapters.in.exception.NotFoundException;
import com.ticketoffice.backend.infra.adapters.in.handlers.EventDetailPageHandler;
import com.ticketoffice.backend.infra.adapters.in.utils.IdValidator;
import io.javalin.Javalin;

import java.util.List;

public class EventDetailPageController implements CustomController {

    private static final String PATH = "/api/public/v1/event";
    final private EventDetailPageHandler eventDetailPageHandler;

    @Inject
    public EventDetailPageController(EventDetailPageHandler eventDetailPageHandler) {
        this.eventDetailPageHandler = eventDetailPageHandler;
    }

    @Override
    public void registeredRoutes(Javalin app) {
        app.get(PATH + "/{id}", ctx -> {
            String id = ctx.pathParam("id");
            ctx.json(getEvent(id));
        });
        app.get(PATH + "/{id}/recommendations", ctx -> {
            String id = ctx.pathParam("id");
            ctx.json(getEventRecommendations(id));
        });
    }


    public EventDetailPageResponse getEvent(String id) throws NotFoundException, BadRequestException {
        IdValidator.validateIdFromParams(id, "Event id", true);
        return eventDetailPageHandler.getEvent(id);
    }

    public List<EventLightResponse> getEventRecommendations(String id) throws NotFoundException, BadRequestException {
        IdValidator.validateIdFromParams(id, "Event id", true);
        return eventDetailPageHandler.getRecommendationByEvent(id);
    }
}
