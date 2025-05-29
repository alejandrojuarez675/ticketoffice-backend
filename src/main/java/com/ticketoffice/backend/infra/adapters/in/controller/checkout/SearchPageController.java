package com.ticketoffice.backend.infra.adapters.in.controller.checkout;

import com.ticketoffice.backend.domain.usecases.events.GetEventsByParamsUseCase;
import com.ticketoffice.backend.infra.adapters.in.dto.response.SearchResponse;
import com.ticketoffice.backend.infra.adapters.in.handlers.SearchPageHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/public/v1/event/search")
public class SearchPageController {

    private final SearchPageHandler searchPageHandler;

    public SearchPageController(SearchPageHandler searchPageHandler) {
        this.searchPageHandler = searchPageHandler;
    }

    @GetMapping()
    @Operation(
        description = "Endpoint to search for events and fill the search page.\n" +
                "This endpoint should be called when the user charge the page or types something in the search bar.",
        summary = "Get the events for home page",
        tags = {"Public Endpoints"},
        parameters = {
                @Parameter(name = "country", description = "The country to search events in", example = "Colombia"),
                @Parameter(name = "city", description = "The city to search events in", example = "Bogotá"),
                @Parameter(name = "query", description = "The search query"),
                @Parameter(name = "pageSize", description = "The number of results to return per page", example = "9"),
                @Parameter(name = "pageNumber", description = "The page number to return. Start at page number 0.", example = "1")
        }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search performed successfully"),
    })
    public ResponseEntity<SearchResponse> search(
            @RequestParam String country,
            @RequestParam(required = false, defaultValue = "") String city,
            @RequestParam(required = false, defaultValue = "") String query,
            @RequestParam(required = false, defaultValue = "9") Integer pageSize,
            @RequestParam(required = false, defaultValue = "1") Integer pageNumber
    ) {
        return ResponseEntity.ok(searchPageHandler.getEventsByParams(country, city, query, pageSize, pageNumber));
    }
}
