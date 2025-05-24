package com.ticketoffice.backend.infra.adapters.in.controller.admin;

import com.ticketoffice.backend.infra.adapters.in.dto.response.UserResponse;
import com.ticketoffice.backend.infra.adapters.in.handlers.UserHandler;
import io.swagger.v3.oas.annotations.Operation;
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

    public UserController(UserHandler userHandler) {
        this.userHandler = userHandler;
    }


    @Operation(
            summary = "Get authenticated user",
            description = "Endpoint to retrieve the currently authenticated user",
            tags = {"MVP", "User Management"},
            security = {
                    @SecurityRequirement(name = "bearerAuth")
            }
    )
    @GetMapping("/me")
    public ResponseEntity<UserResponse> authenticatedUser() {
        return ResponseEntity.ok(userHandler.getAuthenticatedUser());
    }

    @Operation(
            summary = "Get all users",
            description = "Endpoint to retrieve all users in the system",
            tags = {"admin-endpoints", "User Management"},
            security = {
                    @SecurityRequirement(name = "Authorization"),
            }
    )
    @GetMapping()
    public ResponseEntity<List<UserResponse>> allUsers() {
        return ResponseEntity.ok(userHandler.getAllUsers());
    }
}
