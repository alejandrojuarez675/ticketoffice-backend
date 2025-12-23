package com.ticketoffice.backend.domain.usecases.regionalization;

import com.ticketoffice.backend.domain.models.City;

import java.util.List;
import java.util.function.Function;

@FunctionalInterface
public interface GetCitiesUseCase extends Function<String, List<City>> {
}
