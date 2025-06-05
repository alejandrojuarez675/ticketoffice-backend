package com.ticketoffice.backend.infra.adapters.in.controller.admin;

import com.ticketoffice.backend.domain.exception.NotAuthenticatedException;
import com.ticketoffice.backend.infra.adapters.in.controller.UserRoleValidator;
import com.ticketoffice.backend.infra.adapters.in.dto.response.tickets.TicketListResponse;
import com.ticketoffice.backend.infra.adapters.in.exception.BadRequestException;
import com.ticketoffice.backend.infra.adapters.in.exception.NotFoundException;
import com.ticketoffice.backend.infra.adapters.in.exception.UnauthorizedUserException;
import com.ticketoffice.backend.infra.adapters.in.handlers.TicketHandler;
import com.ticketoffice.backend.infra.adapters.in.utils.IdValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/events/{id}/tickets")
@Tag(name = "Tickets Management", description = "Endpoints for managing tickets")
public class TicketController {

    private final TicketHandler ticketHandler;
    private final UserRoleValidator userRoleValidator;

    public TicketController(
            TicketHandler ticketHandler,
            UserRoleValidator userRoleValidator
    ) {
        this.ticketHandler = ticketHandler;
        this.userRoleValidator = userRoleValidator;
    }

    @GetMapping
    @Operation(
            summary = "Get all tickets for an event",
            description = "Retrieves all tickets for a specific event",
            security = @SecurityRequirement(name = "Authorization"),
            parameters = @Parameter(name = "id", description = "The ID of the event", required = true),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved tickets",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(mediaType = "application/json", examples = {
                                    @ExampleObject(value = """
                                    {
                                      "code": "bad_request",
                                      "message": "Event id is not valid"
                                    }
                                    """)
                            })
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(mediaType = "application/json", examples = {
                                    @ExampleObject(value = """
                                    {
                                      "code": "unauthorized",
                                      "message": "You are not authorized to access this resource"
                                    }
                                    """)
                            })
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(mediaType = "application/json", examples = {
                                    @ExampleObject(value = """
                                    {
                                      "code": "forbidden",
                                      "message": "You are not allowed to access this resource"
                                    }
                                    """)
                            })
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Event not found",
                            content = @Content(mediaType = "application/json", examples = {
                                    @ExampleObject(value = """
                                    {
                                      "code": "not_found",
                                      "message": "Event not found"
                                    }
                                    """)
                            })
                    )
            }
    )
    public ResponseEntity<TicketListResponse> getAllTicketsByEventId(
            @PathVariable String id
    ) throws NotAuthenticatedException, NotFoundException, UnauthorizedUserException, BadRequestException {
        IdValidator.validateIdFromParams(id, "event_id", true);
        userRoleValidator.validateThatUserIsSeller();
        return ResponseEntity.ok(ticketHandler.getAllTicketsByEventId(id));
    }
}
