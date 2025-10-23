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
