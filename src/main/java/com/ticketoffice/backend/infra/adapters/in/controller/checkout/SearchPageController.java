package com.ticketoffice.backend.infra.adapters.in.controller.checkout;

import com.google.inject.Inject;
import com.ticketoffice.backend.infra.adapters.in.controller.CustomController;
import com.ticketoffice.backend.infra.adapters.in.dto.response.SearchResponse;
import com.ticketoffice.backend.infra.adapters.in.handlers.SearchPageHandler;
import io.javalin.Javalin;

import java.util.Optional;

public class SearchPageController implements CustomController {

    private final static String PATH = "/api/public/v1/event/search";
    private final SearchPageHandler searchPageHandler;

    @Inject
    public SearchPageController(SearchPageHandler searchPageHandler) {
        this.searchPageHandler = searchPageHandler;
    }

    @Override
    public void registeredRoutes(Javalin app) {
        app.get(PATH, ctx -> {
            String country = ctx.queryParam("country");
            String city = ctx.queryParam("city");
            String query = ctx.queryParam("query");
            Integer pageSize = Optional.ofNullable(ctx.queryParam("pageSize"))
                    .map(Integer::valueOf).orElse(9);
            Integer pageNumber = Optional.ofNullable(ctx.queryParam("pageNumber"))
                    .map(Integer::valueOf).orElse(1);

            ctx.json(search(country, city, query, pageSize, pageNumber));
        });
    }

//    @Operation(
//        description = "Endpoint to search for events and fill the search page.\n" +
//                "This endpoint should be called when the user charge the page or types something in the search bar.",
//        summary = "Get the events for home page",
//        tags = {"Public Endpoints"},
//        parameters = {
//                @Parameter(name = "country", description = "The country to search events in", example = "Colombia"),
//                @Parameter(name = "city", description = "The city to search events in", example = "Bogotá"),
//                @Parameter(name = "query", description = "The search query"),
//                @Parameter(name = "pageSize", description = "The number of results to return per page", example = "9"),
//                @Parameter(name = "pageNumber", description = "The page number to return. Start at page number 0.", example = "1")
//        }
//    )
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Search performed successfully"),
//    })
    public SearchResponse search(
            String country,
            String city,
            String query,
            Integer pageSize,
            Integer pageNumber
    ) {
        return searchPageHandler.getEventsByParams(country, city, query, pageSize, pageNumber);
    }
}
