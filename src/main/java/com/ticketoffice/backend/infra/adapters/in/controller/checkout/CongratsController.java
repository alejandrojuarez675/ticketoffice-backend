package com.ticketoffice.backend.infra.adapters.in.controller.checkout;

import com.ticketoffice.backend.infra.adapters.in.dto.mocks.SalesMocks;
import com.ticketoffice.backend.infra.adapters.in.dto.response.checkout.CongratsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/public/v1/congrats")
public class CongratsController {

    @GetMapping()
    @Operation(
            description = "This endpoint is used to get the congrats page after a successful purchase.\n" +
                    "This endpoint should be called when the user charge the page.",
            summary = "Get the congrats page",
            tags = {"public-endpoints"},
            security = {
                    @SecurityRequirement(name = "bearerAuth")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Congrats page retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Sales not found"),
    })
    public ResponseEntity<CongratsResponse> getCongrats(
            @RequestParam String salesId
    ) {
        return ResponseEntity.ok(SalesMocks.congratsResponse);
    }
}
