package com.ticketoffice.backend.infra.adapters.in.controller.admin;

import com.ticketoffice.backend.infra.adapters.in.controller.UserRoleValidator;
import com.ticketoffice.backend.infra.adapters.in.dto.request.OrganizerCrudRequest;
import com.ticketoffice.backend.infra.adapters.in.exception.BadRequestException;
import com.ticketoffice.backend.infra.adapters.in.exception.UnauthorizedUserException;
import com.ticketoffice.backend.infra.adapters.in.handlers.OrganizerCrudHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/organizer")
public class OrganizerController {

    private final OrganizerCrudHandler organizerCrudHandler;
    private final UserRoleValidator userRoleValidator;

    public OrganizerController(OrganizerCrudHandler organizerCrudHandler, UserRoleValidator userRoleValidator) {
        this.organizerCrudHandler = organizerCrudHandler;
        this.userRoleValidator = userRoleValidator;
    }

    @PostMapping
    @Operation(
            summary = "Create your organizer data",
            description = "Endpoint to create an organizer data for a user",
            tags = {"User Management"},
            security = {
                    @SecurityRequirement(name = "Authorization"),
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "The organizer data to create",
                    required = true,
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = OrganizerCrudRequest.class)
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "201",
                            description = "Organizer created successfully"
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
    public ResponseEntity<Void> createOrganizer(@RequestBody OrganizerCrudRequest organizer)
            throws BadRequestException, UnauthorizedUserException {
        userRoleValidator.validateThatUserIsSeller();
        organizerCrudHandler.createOrganizer(organizer);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
