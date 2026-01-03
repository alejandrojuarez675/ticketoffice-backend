package com.ticketoffice.backend.infra.adapters.in.controller.checkout;

import com.google.inject.Inject;
import com.ticketoffice.backend.infra.adapters.in.controller.CustomController;
import com.ticketoffice.backend.infra.adapters.in.dto.request.BuyTicketsRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.request.CreateSessionRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.request.validators.BuyTicketsRequestValidator;
import com.ticketoffice.backend.infra.adapters.in.dto.request.validators.CreateSessionRequestValidator;
import com.ticketoffice.backend.infra.adapters.in.dto.response.CongratsCheckout;
import com.ticketoffice.backend.infra.adapters.in.dto.response.SessionCreatedResponse;
import com.ticketoffice.backend.infra.adapters.in.exception.BadRequestException;
import com.ticketoffice.backend.infra.adapters.in.handlers.CheckoutHandler;
import io.javalin.Javalin;

public class CheckoutController implements CustomController {

    private static final String PATH = "/api/public/v1/checkout";
    private final CheckoutHandler checkoutHandler;

    @Inject
    public CheckoutController(CheckoutHandler checkoutHandler) {
        this.checkoutHandler = checkoutHandler;
    }

    @Override
    public void registeredRoutes(Javalin app) {
        app.post(PATH + "/session", ctx -> {
            var body = ctx.bodyAsClass(CreateSessionRequest.class);
            ctx.json(createSession(body));
        });
        app.post(PATH + "/session/{sessionId}/buy", ctx -> {
            String id = ctx.pathParam("sessionId");
            var body = ctx.bodyAsClass(BuyTicketsRequest.class);
            ctx.json(buyTicket(id, body));
        });
    }

    public SessionCreatedResponse createSession(CreateSessionRequest body
    ) throws BadRequestException {
        new CreateSessionRequestValidator().validate(body);
        return checkoutHandler.createSession(body);
    }

    public CongratsCheckout buyTicket(String sessionId, BuyTicketsRequest request) throws BadRequestException {
        new BuyTicketsRequestValidator().validate(request);
        return checkoutHandler.buyTickets(sessionId, request);
    }
}
