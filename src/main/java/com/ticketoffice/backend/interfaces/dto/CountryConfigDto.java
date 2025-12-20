package com.ticketoffice.backend.interfaces.dto;

import java.util.List;

public record CountryConfigDto(

        CountryDto data,
        List<CityDto> cities,
        String language,
        List<CurrencyDto> availableCurrencies,
        List<DocumentTypeDto> documentType
) {
}
