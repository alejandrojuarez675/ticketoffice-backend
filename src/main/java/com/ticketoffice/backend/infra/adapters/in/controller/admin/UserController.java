package com.ticketoffice.backend.infra.adapters.in.controller.admin;

import com.google.inject.Inject;
import com.ticketoffice.backend.infra.adapters.in.controller.CustomController;
import com.ticketoffice.backend.infra.adapters.in.controller.UserRoleValidator;
import com.ticketoffice.backend.infra.adapters.in.dto.response.UserResponse;
import com.ticketoffice.backend.infra.adapters.in.exception.UnauthorizedUserException;
import com.ticketoffice.backend.infra.adapters.in.handlers.UserHandler;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UserController implements CustomController {

    private static final String PATH = "/api/v1/users";
    private final UserHandler userHandler;
    private final UserRoleValidator userRoleValidator;

    @Inject
    public UserController(UserHandler userHandler, UserRoleValidator userRoleValidator) {
        this.userHandler = userHandler;
        this.userRoleValidator = userRoleValidator;
    }

    @Override
    public void registeredRoutes(Javalin app) {
        app.get(PATH + "/me", context -> context.json(authenticatedUser(context)));
        app.get(PATH, ctx -> ctx.json(allUsers(ctx)));
    }


//    @Operation(
//            summary = "Get authenticated user",
//            description = "Endpoint to retrieve the currently authenticated user",
//            tags = {"User Management"},
//            security = {
//                    @SecurityRequirement(name = "Authorization")
//            },
//            responses = {
//                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
//                            responseCode = "200",
//                            description = "User retrieved successfully",
//                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))
//                    ),
//                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
//                            responseCode = "401",
//                            description = "Unauthorized"
//                    )
//            }
//    )
    public UserResponse authenticatedUser(@NotNull Context context) throws UnauthorizedUserException {
        userRoleValidator.validateThatUserIsLogged(context);
        return userHandler.getAuthenticatedUser(context);
    }

//    @Operation(
//            summary = "Get all users of the system",
//            description = "Endpoint to retrieve all users in the system",
//            tags = {"SUPER_ADMIN users"},
//            security = {
//                    @SecurityRequirement(name = "Authorization"),
//            },
//            responses = {
//                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
//                            responseCode = "200",
//                            description = "Users retrieved successfully",
//                            content = @Content(mediaType = "application/json")
//                    ),
//                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
//                            responseCode = "401",
//                            description = "Unauthorized"
//                    )
//            }
//    )
    public List<UserResponse> allUsers(@NotNull Context ctx) throws UnauthorizedUserException {
        userRoleValidator.validateThatUserIsAdmin(ctx);
        return userHandler.getAllUsers(ctx);
    }
}
