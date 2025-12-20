package com.ticketoffice.backend.domain.usecases.regionalization;

import com.ticketoffice.backend.domain.models.Country;

import java.util.List;
import java.util.function.Supplier;

@FunctionalInterface
public interface GetAvailableCountriesUseCase extends Supplier<List<Country>> {
}
