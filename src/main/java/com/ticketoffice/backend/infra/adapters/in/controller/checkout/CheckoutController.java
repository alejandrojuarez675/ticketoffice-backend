package com.ticketoffice.backend.infra.adapters.in.controller.checkout;

import com.google.inject.Inject;
import com.ticketoffice.backend.infra.adapters.in.controller.CustomController;
import com.ticketoffice.backend.infra.adapters.in.dto.request.BuyTicketsRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.request.CreateSessionRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.request.validators.BuyTicketsRequestValidator;
import com.ticketoffice.backend.infra.adapters.in.dto.request.validators.CreateSessionRequestValidator;
import com.ticketoffice.backend.infra.adapters.in.dto.response.SessionCreatedResponse;
import com.ticketoffice.backend.infra.adapters.in.exception.BadRequestException;
import com.ticketoffice.backend.infra.adapters.in.handlers.CheckoutHandler;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

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
            buyTicket(id, body);
            ctx.status(HttpStatus.NO_CONTENT);
        });
    }

//    @Operation(
//            summary = "Create a checkout session to reserve the stock of the sales",
//            description = "Endpoint to create a checkout session to reserve the stock of the sales.",
//            tags = { "Checkout", "Public Endpoints" },
//            responses = {
//                    @ApiResponse(
//                            responseCode = "200",
//                            description = "Session created",
//                            content = @Content(
//                                    schema = @Schema(implementation = SessionCreatedResponse.class)
//                            )
//                    ),
//                    @ApiResponse(
//                            responseCode = "400",
//                            description = "Bad Request",
//                            content = @Content(mediaType = "application/json")
//                    )
//            }
//    )
    public SessionCreatedResponse createSession(CreateSessionRequest body
    ) throws BadRequestException {
        new CreateSessionRequestValidator().validate(body);
        return checkoutHandler.createSession(body);
    }

//    @Operation(
//            summary = "Buy a ticket",
//            description = "Endpoint to buy a ticket.",
//            tags = { "Checkout", "Public Endpoints" },
//            responses = {
//                    @ApiResponse(
//                            responseCode = "204",
//                            description = "Ticket bought",
//                            content = @Content(mediaType = "application/json")
//                    ),
//                    @ApiResponse(
//                            responseCode = "400",
//                            description = "Bad Request",
//                            content = @Content(mediaType = "application/json")
//                    )
//            }
//    )
    public void buyTicket(String sessionId, BuyTicketsRequest request) throws BadRequestException {
        new BuyTicketsRequestValidator().validate(request);
        checkoutHandler.buyTickets(sessionId, request);
    }
}
