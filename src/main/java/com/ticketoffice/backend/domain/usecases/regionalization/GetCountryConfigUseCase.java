package com.ticketoffice.backend.domain.usecases.regionalization;

import com.ticketoffice.backend.domain.models.City;
import com.ticketoffice.backend.domain.models.Country;
import com.ticketoffice.backend.domain.models.Currency;
import com.ticketoffice.backend.domain.models.DocumentType;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@FunctionalInterface
public interface GetCountryConfigUseCase extends Function<String, Optional<GetCountryConfigUseCase.CountryConfig>> {
    record CountryConfig(
        Country country,
        List<City> cities,
        String language,
        List<Currency> currencies,
        List<DocumentType> documentTypes
    ) {}
}
