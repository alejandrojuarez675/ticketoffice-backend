package com.ticketoffice.backend.infra.adapters.in.controller.admin;

import com.google.inject.Inject;
import com.ticketoffice.backend.domain.exception.NotAuthenticatedException;
import com.ticketoffice.backend.infra.adapters.in.controller.CustomController;
import com.ticketoffice.backend.infra.adapters.in.controller.UserRoleValidator;
import com.ticketoffice.backend.infra.adapters.in.dto.response.tickets.SalesListResponse;
import com.ticketoffice.backend.infra.adapters.in.exception.BadRequestException;
import com.ticketoffice.backend.infra.adapters.in.exception.NotFoundException;
import com.ticketoffice.backend.infra.adapters.in.exception.UnauthorizedUserException;
import com.ticketoffice.backend.infra.adapters.in.handlers.SalesHandler;
import com.ticketoffice.backend.infra.adapters.in.utils.IdValidator;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.jetbrains.annotations.NotNull;

public class SalesController implements CustomController {

    private final SalesHandler salesHandler;
    private final UserRoleValidator userRoleValidator;

    @Inject
    public SalesController(
            SalesHandler salesHandler,
            UserRoleValidator userRoleValidator
    ) {
        this.salesHandler = salesHandler;
        this.userRoleValidator = userRoleValidator;
    }

    @Override
    public void registeredRoutes(Javalin app) {
        app.get("/api/v1/events/{eventId}/sales", context -> {
            String eventId = context.pathParam("eventId");
            context.json(getAllTicketsByEventId(context, eventId));
        });
        app.post("/api/v1/events/{eventId}/sales/{saleId}/validate", context -> {
            String saleId = context.pathParam("saleId");
            String eventId = context.pathParam("eventId");
            validateSale(context, saleId, eventId);
            context.status(HttpStatus.NO_CONTENT);
        });
    }

    public SalesListResponse getAllTicketsByEventId(
            @NotNull Context context, String id
    ) throws NotAuthenticatedException, NotFoundException, UnauthorizedUserException, BadRequestException {
        IdValidator.validateIdFromParams(id, "event_id", true);
        userRoleValidator.validateThatUserIsSeller(context);
        return salesHandler.getAllSalesByEventId(context, id);
    }

    public void validateSale(@NotNull Context context, String saleId, String id)
            throws NotAuthenticatedException, NotFoundException, UnauthorizedUserException, BadRequestException {
        IdValidator.validateIdFromParams(id, "event_id", true);
        IdValidator.validateIdFromParams(saleId, "sale_id", true);
        userRoleValidator.validateThatUserIsSeller(context);
        salesHandler.validateSale(context, saleId, id);
    }
}
