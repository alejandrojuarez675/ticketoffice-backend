package com.ticketoffice.backend.infra.adapters.in.controller.checkout;

import com.ticketoffice.backend.infra.adapters.in.dto.mocks.EventMocks;
import com.ticketoffice.backend.infra.adapters.in.dto.response.events.EventForVipResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/public/v1/vip")
public class VipController {

    @GetMapping("/{id}")
    @Operation(
            description = "Endpoint to get a specific event by its ID formatted for VIP page",
            summary = "Get all the information of an event by ID",
            tags = {"public-endpoints"},
            parameters = {@Parameter(name = "id", description = "The ID of the event to be retrieved", required = true)}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Event not found"),
    })
    public ResponseEntity<EventForVipResponse> getEvent(@PathVariable Long id) {
        return new ResponseEntity<>(EventMocks.eventForVipResponse, HttpStatus.OK);
    }

}
