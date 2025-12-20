package com.ticketoffice.backend.interfaces.dto;

public record CountryConfigDto(

        CountryDto build, java.util.List<CityDto> list, String language, java.util.List<CurrencyDto> currencyDtos,
        java.util.List<DocumentTypeDto> documentTypeDtos) {
}
