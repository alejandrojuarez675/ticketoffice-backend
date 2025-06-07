package com.ticketoffice.backend.infra.adapters.in.controller.admin;

import com.ticketoffice.backend.domain.exception.NotAuthenticatedException;
import com.ticketoffice.backend.infra.adapters.in.controller.UserRoleValidator;
import com.ticketoffice.backend.infra.adapters.in.dto.response.tickets.SalesListResponse;
import com.ticketoffice.backend.infra.adapters.in.exception.BadRequestException;
import com.ticketoffice.backend.infra.adapters.in.exception.NotFoundException;
import com.ticketoffice.backend.infra.adapters.in.exception.UnauthorizedUserException;
import com.ticketoffice.backend.infra.adapters.in.handlers.SalesHandler;
import com.ticketoffice.backend.infra.adapters.in.utils.IdValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/events/{id}/sales")
@Tag(name = "Sales Management", description = "Endpoints for managing sales")
public class SalesController {

    private final SalesHandler salesHandler;
    private final UserRoleValidator userRoleValidator;

    public SalesController(
            SalesHandler salesHandler,
            UserRoleValidator userRoleValidator
    ) {
        this.salesHandler = salesHandler;
        this.userRoleValidator = userRoleValidator;
    }

    @GetMapping
    @Operation(
            summary = "Get all sales for an event",
            description = "Retrieves all sales for a specific event",
            security = @SecurityRequirement(name = "Authorization"),
            parameters = @Parameter(name = "id", description = "The ID of the event", required = true),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved sales",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SalesListResponse.class))
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
    public ResponseEntity<SalesListResponse> getAllTicketsByEventId(
            @PathVariable String id
    ) throws NotAuthenticatedException, NotFoundException, UnauthorizedUserException, BadRequestException {
        IdValidator.validateIdFromParams(id, "event_id", true);
        userRoleValidator.validateThatUserIsSeller();
        return ResponseEntity.ok(salesHandler.getAllSalesByEventId(id));
    }
}
