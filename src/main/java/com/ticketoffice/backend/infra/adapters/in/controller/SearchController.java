package com.ticketoffice.backend.infra.adapters.in.controller;

import com.ticketoffice.backend.infra.adapters.in.dto.response.SearchResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/v1/search")
public class SearchController {

    @GetMapping()
    @Operation(
        description = "Endpoint to search for events and fill the search page.\n" +
                "This endpoint should be called when the user charge the page or types something in the search bar.",
        summary = "Search for events",
        tags = {"public-endpoints"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search performed successfully"),
    })
    public ResponseEntity<SearchResponse> search() {
        return ResponseEntity.ok(new SearchResponse(List.of(), true));
    }
}
