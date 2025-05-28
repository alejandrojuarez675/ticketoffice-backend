package com.ticketoffice.backend.infra.adapters.in.controller.admin;

import com.ticketoffice.backend.infra.adapters.in.controller.UserRoleValidator;
import com.ticketoffice.backend.infra.adapters.in.dto.response.UserResponse;
import com.ticketoffice.backend.infra.adapters.in.exception.UnauthorizedUserException;
import com.ticketoffice.backend.infra.adapters.in.handlers.UserHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/users")
@RestController
public class UserController {

    private final UserHandler userHandler;
    private final UserRoleValidator userRoleValidator;

    public UserController(UserHandler userHandler, UserRoleValidator userRoleValidator) {
        this.userHandler = userHandler;
        this.userRoleValidator = userRoleValidator;
    }


    @Operation(
            summary = "Get authenticated user",
            description = "Endpoint to retrieve the currently authenticated user",
            tags = {"MVP", "User Management"},
            security = {
                    @SecurityRequirement(name = "Authorization")
            },
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "User retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    )
            }
    )
    @GetMapping("/me")
    public ResponseEntity<UserResponse> authenticatedUser() throws UnauthorizedUserException {
        userRoleValidator.validateThatUserIsLogged();
        return ResponseEntity.ok(userHandler.getAuthenticatedUser());
    }

    @Operation(
            summary = "Get all users",
            description = "Endpoint to retrieve all users in the system",
            tags = {"admin-endpoints", "User Management"},
            security = {
                    @SecurityRequirement(name = "Authorization"),
            },
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Users retrieved successfully",
                            content = @Content(mediaType = "application/json")
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    )
            }
    )
    @GetMapping()
    public ResponseEntity<List<UserResponse>> allUsers() throws UnauthorizedUserException {
        userRoleValidator.validateThatUserIsAdmin();
        return ResponseEntity.ok(userHandler.getAllUsers());
    }
}
