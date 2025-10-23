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


    public UserResponse authenticatedUser(@NotNull Context context) throws UnauthorizedUserException {
        userRoleValidator.validateThatUserIsLogged(context);
        return userHandler.getAuthenticatedUser(context);
    }

    public List<UserResponse> allUsers(@NotNull Context ctx) throws UnauthorizedUserException {
        userRoleValidator.validateThatUserIsAdmin(ctx);
        return userHandler.getAllUsers(ctx);
    }
}
