package com.ticketoffice.backend.infra.adapters.in.controller.admin;

import com.google.inject.Inject;
import com.ticketoffice.backend.infra.adapters.in.controller.CustomController;
import com.ticketoffice.backend.infra.adapters.in.controller.UserRoleValidator;
import com.ticketoffice.backend.infra.adapters.in.dto.request.OrganizerCrudRequest;
import com.ticketoffice.backend.infra.adapters.in.exception.BadRequestException;
import com.ticketoffice.backend.infra.adapters.in.exception.UnauthorizedUserException;
import com.ticketoffice.backend.infra.adapters.in.handlers.OrganizerCrudHandler;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.jetbrains.annotations.NotNull;

public class OrganizerController implements CustomController {

    private final OrganizerCrudHandler organizerCrudHandler;
    private final UserRoleValidator userRoleValidator;

    @Inject
    public OrganizerController(OrganizerCrudHandler organizerCrudHandler, UserRoleValidator userRoleValidator) {
        this.organizerCrudHandler = organizerCrudHandler;
        this.userRoleValidator = userRoleValidator;
    }

    @Override
    public void registeredRoutes(Javalin app) {
        app.post("/api/v1/organizer", context -> {
            OrganizerCrudRequest request = context.bodyAsClass(OrganizerCrudRequest.class);
            createOrganizer(context, request);
            context.status(HttpStatus.CREATED);
        });
    }

    private void createOrganizer(@NotNull Context context, OrganizerCrudRequest organizer)
            throws BadRequestException, UnauthorizedUserException {
        userRoleValidator.validateThatUserIsSeller(context);
        organizerCrudHandler.createOrganizer(context, organizer);
    }
}
