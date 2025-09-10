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

//    @Operation(
//            description = "Endpoint to get a specific event by its ID formatted for detail page",
//            summary = "Get all the information of an event by ID",
//            tags = {"Public Endpoints"},
//            parameters = {@Parameter(name = "id", description = "The ID of the event to be retrieved", required = true)}
//    )
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Event retrieved successfully",
//                    content = { @Content(mediaType = "application/json", schema = @Schema( implementation = EventDetailPageResponse.class) ) }
//            ),
//            @ApiResponse(
//                    responseCode = "400",
//                    description = "Event id is not valid",
//                    content = {
//                            @Content(mediaType = "application/json", examples = {@ExampleObject(value = """
//                                    {
//                                      "code": "bad_found",
//                                      "message": "Event id is not valid"
//                                    }
//                            """)})
//                    }
//            ),
//            @ApiResponse(
//                    responseCode = "404",
//                    description = "Event not found",
//                    content = {
//                            @Content(mediaType = "application/json", examples = {@ExampleObject(value = """
//                                    {
//                                      "code": "not_found",
//                                      "message": "Event not found"
//                                    }
//                            """)})
//                    }
//            ),
//    })
    public EventDetailPageResponse getEvent(String id) throws NotFoundException, BadRequestException {
        IdValidator.validateIdFromParams(id, "Event id", true);
        return eventDetailPageHandler.getEvent(id);
    }

//    @Operation(
//            description = "Endpoint to get recommendations for a event by its ID formatted for detail page",
//            summary = "Get events to show as recommendation for an event",
//            tags = {"Public Endpoints"},
//            parameters = {@Parameter(name = "id", description = "The ID of the event to be retrieved", required = true)}
//    )
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Events retrieved successfully"
//            ),
//            @ApiResponse(
//                    responseCode = "400",
//                    description = "Event id is not valid",
//                    content = {
//                            @Content(mediaType = "application/json", examples = {@ExampleObject(value = """
//                                    {
//                                      "code": "bad_found",
//                                      "message": "Event id is not valid"
//                                    }
//                            """)})
//                    }
//            ),
//            @ApiResponse(
//                    responseCode = "404",
//                    description = "Event not found",
//                    content = {
//                            @Content(mediaType = "application/json", examples = {@ExampleObject(value = """
//                                    {
//                                      "code": "not_found",
//                                      "message": "Event not found"
//                                    }
//                            """)})
//                    }
//            ),
//    })
    public List<EventLightResponse> getEventRecommendations(String id) throws NotFoundException, BadRequestException {
        IdValidator.validateIdFromParams(id, "Event id", true);
        return eventDetailPageHandler.getRecommendationByEvent(id);
    }
}
