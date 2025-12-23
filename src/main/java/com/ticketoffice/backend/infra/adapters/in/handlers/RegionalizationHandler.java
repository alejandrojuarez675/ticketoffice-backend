package com.ticketoffice.backend.infra.adapters.in.handlers;

import com.ticketoffice.backend.domain.usecases.regionalization.GetAvailableCountriesUseCase;
import com.ticketoffice.backend.domain.usecases.regionalization.GetCountryConfigUseCase;
import com.ticketoffice.backend.interfaces.dto.CityDto;
import com.ticketoffice.backend.interfaces.dto.CountryConfigDto;
import com.ticketoffice.backend.interfaces.dto.CountryDto;
import com.ticketoffice.backend.interfaces.dto.CurrencyDto;
import com.ticketoffice.backend.interfaces.dto.DocumentTypeDto;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;

public class RegionalizationHandler {

    private final GetAvailableCountriesUseCase getAvailableCountriesUseCase;
    private final GetCountryConfigUseCase getCountryConfigUseCase;

    @Inject
    public RegionalizationHandler(
            GetAvailableCountriesUseCase getAvailableCountriesUseCase,
            GetCountryConfigUseCase getCountryConfigUseCase
    ) {
        this.getAvailableCountriesUseCase = getAvailableCountriesUseCase;
        this.getCountryConfigUseCase = getCountryConfigUseCase;
    }

    public List<CountryDto> getCountryList() {
        return getAvailableCountriesUseCase.get().stream()
                .map(country -> CountryDto.builder()
                        .code(country.code())
                        .name(country.name())
                        .build())
                .toList();
    }

    public Optional<CountryConfigDto> getCountryConfig(String countryCode) {
        return getCountryConfigUseCase.apply(countryCode)
                .map(countryConfig -> new CountryConfigDto(
                        CountryDto.builder()
                                .code(countryConfig.country().code())
                                .name(countryConfig.country().name())
                                .build(),
                        countryConfig.cities().stream()
                                .map(city -> CityDto.builder()
                                        .code(city.code())
                                        .name(city.name())
                                        .build())
                                .toList(),
                        countryConfig.language(),
                        countryConfig.currencies().stream()
                                .map(currency -> CurrencyDto.builder()
                                        .code(currency.code())
                                        .name(currency.name())
                                        .symbol(currency.symbol())
                                        .build()
                                )
                                .toList(),
                        countryConfig.documentTypes().stream()
                                .map(documentType -> DocumentTypeDto.builder()
                                        .code(documentType.code())
                                        .name(documentType.name())
                                        .description(documentType.description())
                                        .format(documentType.format())
                                        .regex(documentType.regex())
                                        .build()
                                ).toList()

                ));
    }
}
