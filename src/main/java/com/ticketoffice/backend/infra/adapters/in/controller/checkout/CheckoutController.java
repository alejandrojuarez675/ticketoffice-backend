package com.ticketoffice.backend.infra.adapters.in.controller.checkout;

import com.ticketoffice.backend.infra.adapters.in.dto.request.BuyTicketsRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.request.CreateSessionRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.request.validators.BuyTicketsRequestValidator;
import com.ticketoffice.backend.infra.adapters.in.dto.request.validators.CreateSessionRequestValidator;
import com.ticketoffice.backend.infra.adapters.in.dto.response.SessionCreatedResponse;
import com.ticketoffice.backend.infra.adapters.in.exception.BadRequestException;
import com.ticketoffice.backend.infra.adapters.in.handlers.CheckoutHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/v1/checkout")
public class CheckoutController {

    private final CheckoutHandler checkoutHandler;

    public CheckoutController(CheckoutHandler checkoutHandler) {
        this.checkoutHandler = checkoutHandler;
    }

    @Operation(
            summary = "Create a checkout session to reserve the stock of the sales",
            description = "Endpoint to create a checkout session to reserve the stock of the sales.",
            tags = { "Checkout", "Public Endpoints" },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Session created",
                            content = @Content(
                                    schema = @Schema(implementation = SessionCreatedResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @PostMapping("/session")
    public ResponseEntity<SessionCreatedResponse> createSession(
            @RequestBody CreateSessionRequest body
    ) throws BadRequestException {
        new CreateSessionRequestValidator().validate(body);
        return ResponseEntity.ok(checkoutHandler.createSession(body));
    }

    @Operation(
            summary = "Buy a ticket",
            description = "Endpoint to buy a ticket.",
            tags = { "Checkout", "Public Endpoints" },
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Ticket bought",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @PostMapping("/session/{sessionId}/buy")
    public ResponseEntity<Void> buyTicket(
            @PathVariable String sessionId,
            @RequestBody BuyTicketsRequest request
    ) throws BadRequestException {
        new BuyTicketsRequestValidator().validate(request);
        checkoutHandler.buyTickets(sessionId, request);
        return ResponseEntity.noContent().build();
    }
}
