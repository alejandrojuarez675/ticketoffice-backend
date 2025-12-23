package com.ticketoffice.backend.infra.adapters.in.dto.response.config;

import java.util.List;

public record CountryConfigDto(

        CountryDto data,
        List<CityDto> cities,
        String language,
        List<CurrencyDto> availableCurrencies,
        List<DocumentTypeDto> documentType
) {
}
