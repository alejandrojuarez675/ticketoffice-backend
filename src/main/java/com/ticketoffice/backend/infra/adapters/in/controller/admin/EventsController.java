package com.ticketoffice.backend.infra.adapters.in.controller.admin;

import com.ticketoffice.backend.infra.adapters.in.dto.mocks.EventMocks;
import com.ticketoffice.backend.infra.adapters.in.dto.request.EventCrudRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.request.validators.EventCrudRequestValidator;
import com.ticketoffice.backend.infra.adapters.in.dto.response.events.EventListResponse;
import com.ticketoffice.backend.infra.adapters.in.dto.response.events.EventResponse;
import com.ticketoffice.backend.infra.adapters.in.dto.response.events.EventLightResponse;
import com.ticketoffice.backend.infra.adapters.in.exception.BadRequestException;
import com.ticketoffice.backend.infra.adapters.in.handlers.EventCrudHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    public EventsController(EventCrudHandler eventCrudHandler) {
        this.eventCrudHandler = eventCrudHandler;
    }

    @GetMapping()
    @Operation(description = "Endpoint to get all events for the logged in user", tags = {"admin-events"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Events retrieved successfully"),
    })
    public ResponseEntity<EventListResponse> getEvents() {
        return ResponseEntity.ok(new EventListResponse(eventCrudHandler.findAll()));
    }

    @PostMapping()
    @Operation(
            description = "Endpoint to create a new event. You have to be logged as ADMIN to create an event.",
            summary = "Create a new event",
            tags = {"admin-events", "MVP"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody()
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
    })
    public ResponseEntity<Void> postEvents(
            @RequestBody EventCrudRequest event
    ) throws BadRequestException {
        new EventCrudRequestValidator().validate(event);
        eventCrudHandler.create(event);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    @Operation(
            description = "Endpoint to update an event. You have to be logged as ADMIN to update an event.",
            tags = {"admin-events"},
            parameters = {@Parameter(name = "id", description = "The ID of the event to be retrieved", required = true)}
    )
    public ResponseEntity<EventLightResponse> putEvents(@PathVariable Long id, @RequestBody EventCrudRequest event) {
        return new ResponseEntity<>(EventMocks.eventLightDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(
            description = "Endpoint to delete an event. You have to be logged as ADMIN to delete an event.",
            tags = {"admin-events"},
            parameters = {@Parameter(name = "id", description = "The ID of the event to be retrieved", required = true)}
    )
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
