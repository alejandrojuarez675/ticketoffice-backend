package com.ticketoffice.backend.infra.adapters.in.controller.admin;

import com.ticketoffice.backend.domain.exception.NotAuthenticatedException;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/v1/events")
public class EventsController {

    private final EventCrudHandler eventCrudHandler;
    private final UserRoleValidator userRoleValidator;

    public EventsController(EventCrudHandler eventCrudHandler, UserRoleValidator userRoleValidator) {
        this.eventCrudHandler = eventCrudHandler;
        this.userRoleValidator = userRoleValidator;
    }

    @GetMapping()
    @Operation(
            summary = "Get my events",
            description = "Endpoint to get all events for the logged in user",
            tags = {"Events Management"},
            security = {
                    @SecurityRequirement(name = "Authorization")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Events retrieved successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = """
                                    {
                                      "code": "unauthorized",
                                      "message": "You are not authorized to access this resource"
                                    }
                            """)}))
    })
    public ResponseEntity<EventListResponse> getEvents() throws UnauthorizedUserException {
        userRoleValidator.validateThatUserIsSeller();
        return ResponseEntity.ok(new EventListResponse(eventCrudHandler.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get my event with id",
            description = "Endpoint to get all events for the logged in user",
            tags = {"Events Management"},
            security = {
                    @SecurityRequirement(name = "Authorization")
            },
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Events retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EventDetailPageResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = """
                                    {
                                      "code": "unauthorized",
                                      "message": "You are not authorized to access this resource"
                                    }
                            """)}))
            }
    )
    public ResponseEntity<EventDetailPageResponse> getEventById(@PathVariable String id) throws UnauthorizedUserException, BadRequestException, NotAuthenticatedException, NotFoundException {
        userRoleValidator.validateThatUserIsSeller();
        IdValidator.validateIdFromParams(id, "seler_id", true);
        return ResponseEntity.ok(eventCrudHandler.getEventById(id));
    }

    @PostMapping()
    @Operation(
            description = "Endpoint to create a new event. You have to be logged as ADMIN to create an event.",
            summary = "Create a new event",
            tags = {"Events Management"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(),
            security = {
                    @SecurityRequirement(name = "Authorization")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Event created successfully"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Event is not valid",
                    content = {
                            @Content(mediaType = "application/json", examples = {@ExampleObject(value = """
                                    {
                                      "code": "bad_error",
                                      "message": "Event name is too large"
                                    }
                            """)})
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = {
                            @Content(mediaType = "application/json", examples = {@ExampleObject(value = """
                                    {
                                      "code": "unauthorized",
                                      "message": "You are not authorized to access this resource"
                                    }
                            """)})
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = {
                            @Content(mediaType = "application/json", examples = {@ExampleObject(value = """
                                    {
                                      "code": "internal_server_error",
                                      "message": "Internal Server Error"
                                    }
                            """)})
                    }
            )
    })
    public ResponseEntity<Void> postEvents(
            @RequestBody EventCrudRequest event
    ) throws BadRequestException, UnauthorizedUserException {
        userRoleValidator.validateThatUserIsSeller();
        new EventCrudRequestValidator().validate(event);
        eventCrudHandler.create(event);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    @Operation(
            description = "Endpoint to update an event. You can update only your events.",
            summary = "Update an event",
            tags = {"Events Management"},
            parameters = {@Parameter(name = "id", description = "The ID of the event to be retrieved", required = true)},
            security = {
                    @SecurityRequirement(name = "Authorization")
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "The event to update",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EventCrudRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Event updated successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EventLightResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = """
                                    {
                                      "code": "bad_request",
                                      "message": "Event name is too large"
                                    }
                            """)})
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = """
                                    {
                                      "code": "unauthorized",
                                      "message": "You are not authorized to access this resource"
                                    }
                            """)})
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = """
                                    {
                                      "code": "internal_server_error",
                                      "message": "Internal Server Error"
                                    }
                            """)})
                    )
            }
    )
    public ResponseEntity<EventLightResponse> putEvents(
            @PathVariable String id, @RequestBody EventCrudRequest event
    ) throws BadRequestException, UnauthorizedUserException {
        userRoleValidator.validateThatUserIsSeller();
        IdValidator.validateIdFromParams(id, "id", true);
        return ResponseEntity.ok(eventCrudHandler.updateMyEvent(id, event));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete an event",
            description = "Endpoint to delete an event. You have to be logged as ADMIN to delete an event.",
            tags = {"Events Management"},
            parameters = {@Parameter(name = "id", description = "The ID of the event to be retrieved", required = true)},
            security = {
                    @SecurityRequirement(name = "Authorization")
            },
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "204",
                            description = "Event deleted successfully"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "Bad Request"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error"
                    )
            }
    )
    public ResponseEntity<Void> deleteEvent(@PathVariable String id)
            throws UnauthorizedUserException, BadRequestException, NotAuthenticatedException {
        userRoleValidator.validateThatUserIsSeller();
        IdValidator.validateIdFromParams(id, "id", true);
        eventCrudHandler.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
