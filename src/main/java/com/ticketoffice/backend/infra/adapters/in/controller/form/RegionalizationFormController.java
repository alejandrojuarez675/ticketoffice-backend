package com.ticketoffice.backend.infra.adapters.in.controller.form;

import com.ticketoffice.backend.infra.adapters.in.controller.CustomController;
import com.ticketoffice.backend.infra.adapters.in.handlers.RegionalizationHandler;
import com.ticketoffice.backend.interfaces.dto.CountryConfigDto;
import com.ticketoffice.backend.interfaces.dto.CountryDto;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public class RegionalizationFormController implements CustomController {

    private final RegionalizationHandler regionalizationHandler;

    @Inject
    public RegionalizationFormController(RegionalizationHandler regionalizationHandler) {
        this.regionalizationHandler = regionalizationHandler;
    }

    @Override
    public void registeredRoutes(Javalin app) {
        app.get("/api/public/v1/form/country", this::getCountryList);
        app.get("/api/public/v1/form/country/{countryId}/config", this::getCountryConfig);
    }

    private void getCountryConfig(@NotNull Context ctx) {
        List<CountryDto> countries = regionalizationHandler.getCountryList();
        ctx.json(countries);
    }

    private void getCountryList(@NotNull Context ctx) {
        String countryCode = ctx.pathParam("countryId");
        Optional<CountryConfigDto> countryConfigDto = regionalizationHandler.getCountryConfig(countryCode);
        if (countryConfigDto.isEmpty()) {
            ctx.status(HttpStatus.NOT_FOUND);
            return;
        }
        ctx.json(countryConfigDto);
    }
}
