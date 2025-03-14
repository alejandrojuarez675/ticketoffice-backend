package com.ticketoffice.backend.infra.adapters.in.controller;

import com.ticketoffice.backend.infra.adapters.in.dto.mocks.EventMocks;
import com.ticketoffice.backend.infra.adapters.in.dto.request.EventCrudRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.response.EventForVipResponse;
import com.ticketoffice.backend.infra.adapters.in.dto.response.EventListResponse;
import com.ticketoffice.backend.infra.adapters.in.dto.shared.EventLightDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
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
@RequestMapping("/v1/events")
public class EventsController {

    @GetMapping()
    @Operation(description = "Endpoint to get all events for the logged in user", tags = {"owner-endpoints"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Events retrieved successfully"),
    })
    public EventListResponse getEvents() {
        return new EventListResponse(List.of());
    }

    @GetMapping("/{id}")
    @Operation(
            description = "Endpoint to get a specific event by its ID",
            summary = "Get all the information of an event by ID",
            tags = {"public-endpoints"},
            parameters = {@Parameter(name = "id", description = "The ID of the event to be retrieved", required = true)}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Event not found"),
    })
    public ResponseEntity<EventForVipResponse> getEvent(@PathVariable Long id) {
        return new ResponseEntity<>(EventMocks.eventForVipResponse, HttpStatus.OK);
    }

    @PostMapping()
    @Operation(description = "Endpoint to create a new event. You have to be logged as ADMIN to create an event.", tags = {"owner-endpoints"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Event created successfully"),
    })
    public ResponseEntity<EventLightDTO> postEvents(@RequestBody EventCrudRequest event) {
        return new ResponseEntity<>(EventMocks.eventLightDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(
            description = "Endpoint to update an event. You have to be logged as ADMIN to update an event.",
            tags = {"owner-endpoints"},
            parameters = {@Parameter(name = "id", description = "The ID of the event to be retrieved", required = true)}
    )
    public ResponseEntity<EventLightDTO> putEvents(@PathVariable Long id, @RequestBody EventCrudRequest event) {
        return new ResponseEntity<>(EventMocks.eventLightDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(
            description = "Endpoint to delete an event. You have to be logged as ADMIN to delete an event.",
            tags = {"owner-endpoints"},
            parameters = {@Parameter(name = "id", description = "The ID of the event to be retrieved", required = true)}
    )
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
