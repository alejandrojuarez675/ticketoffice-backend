package com.ticketoffice.backend.infra.adapters.in.controller.checkout;

import com.ticketoffice.backend.infra.adapters.in.dto.mocks.TicketMocks;
import com.ticketoffice.backend.infra.adapters.in.dto.request.BuyTicketsRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.response.checkout.AvailableTicketListResponse;
import com.ticketoffice.backend.infra.adapters.in.dto.response.checkout.BuyTicketResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/v1/buy-tickets")
public class BuyTicketsController {

    @GetMapping
    @Operation(
            summary = "Get the buy tickets page",
            description = "This endpoint is used to get the buy tickets page.\n" +
                    "This endpoint should be called when the user charge the page.",
            tags = {"public-endpoints"}
    )
    public ResponseEntity<AvailableTicketListResponse> getBuyTickets() {
        return ResponseEntity.ok(TicketMocks.availableTicketListResponse);
    }

    @PostMapping
    @Operation(
            summary = "Buy tickets for an event",
            description = "Endpoint to buy tickets for an event. You have to be logged in as buyer to buy tickets.",
            tags = {"public-endpoints"}
    )
    public ResponseEntity<BuyTicketResponse> buyTickets(
            @RequestBody BuyTicketsRequest buyTicketsRequest
    ) {
        return ResponseEntity.ok(TicketMocks.buyTicketResponse);
    }
}
