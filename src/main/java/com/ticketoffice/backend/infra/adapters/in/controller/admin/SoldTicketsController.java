package com.ticketoffice.backend.infra.adapters.in.controller.admin;

import com.ticketoffice.backend.infra.adapters.in.dto.mocks.TicketMocks;
import com.ticketoffice.backend.infra.adapters.in.dto.request.TicketCrudRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.response.tickets.TicketLightResponse;
import com.ticketoffice.backend.infra.adapters.in.dto.response.tickets.TicketListResponse;
import com.ticketoffice.backend.infra.adapters.in.dto.response.tickets.TicketResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v1/sold-tickets")
public class SoldTicketsController {

    @GetMapping()
    @Operation(
            description = "Endpoint to get all Tickets for the logged in user",
            tags = {"admin-sold-tickets"},
            security = {
                    @SecurityRequirement(name = "Authorization")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tickets retrieved successfully"),
    })
    public TicketListResponse getTickets() {
        return TicketMocks.ticketListResponse;
    }

    @GetMapping("/{id}")
    @Operation(
            description = "Endpoint to get a specific Ticket by its ID for the logged in user",
            summary = "Get all the information of an Ticket by ID",
            tags = {"admin-sold-tickets"},
            parameters = {@Parameter(name = "id", description = "The ID of the Ticket to be retrieved", required = true)},
            security = {
                    @SecurityRequirement(name = "Authorization")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Ticket not found"),
    })
    public ResponseEntity<TicketResponse> getTicket(@PathVariable Long id) {
        return new ResponseEntity<>(TicketMocks.TicketResponse, HttpStatus.OK);
    }

    @PostMapping()
    @Operation(
            description = "Endpoint to create a new Ticket. You have to be logged as ADMIN to create an Ticket.",
            tags = {"admin-sold-tickets"},
            security = {
                    @SecurityRequirement(name = "Authorization")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ticket created successfully"),
    })
    public ResponseEntity<TicketLightResponse> postTickets(@RequestBody TicketCrudRequest Ticket) {
        return new ResponseEntity<>(TicketMocks.ticketLightResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(
            description = "Endpoint to update an Ticket. You have to be logged as ADMIN to update an Ticket.",
            tags = {"admin-sold-tickets"},
            parameters = {@Parameter(name = "id", description = "The ID of the Ticket to be retrieved", required = true)},
            security = {
                    @SecurityRequirement(name = "Authorization")
            }
    )
    public ResponseEntity<TicketLightResponse> putTickets(@PathVariable Long id, @RequestBody TicketCrudRequest Ticket) {
        return new ResponseEntity<>(TicketMocks.ticketLightResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(
            description = "Endpoint to delete an Ticket. You have to be logged as ADMIN to delete an Ticket.",
            tags = {"admin-sold-tickets"},
            parameters = {@Parameter(name = "id", description = "The ID of the Ticket to be retrieved", required = true)},
            security = {
                    @SecurityRequirement(name = "Authorization")
            }
    )
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
