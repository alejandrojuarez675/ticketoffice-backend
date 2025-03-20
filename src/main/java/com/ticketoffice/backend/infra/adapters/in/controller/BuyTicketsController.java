package com.ticketoffice.backend.infra.adapters.in.controller;

import com.ticketoffice.backend.infra.adapters.in.dto.request.BuyTicketsRequest;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/buy-tickets")
public class BuyTicketsController {

    @PostMapping
    @Operation(
            summary = "Buy tickets for an event",
            description = "Endpoint to buy tickets for an event. You have to be logged in as buyer to buy tickets.",
            tags = {"buy-tickets"}
    )
    public ResponseEntity<Void> buyTickets(@RequestBody BuyTicketsRequest buyTicketsRequest) {
        return ResponseEntity.ok().build();
    }
}
