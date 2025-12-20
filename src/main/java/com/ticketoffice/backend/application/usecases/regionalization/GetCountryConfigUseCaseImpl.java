package com.ticketoffice.backend.application.usecases.regionalization;

import com.ticketoffice.backend.domain.models.City;
import com.ticketoffice.backend.domain.models.Currency;
import com.ticketoffice.backend.domain.models.DocumentType;
import com.ticketoffice.backend.domain.ports.RegionalizationRepository;
import com.ticketoffice.backend.domain.usecases.regionalization.GetCountryConfigUseCase;

import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;

public class GetCountryConfigUseCaseImpl implements GetCountryConfigUseCase {

    private final RegionalizationRepository regionalizationRepository;

    @Inject
    public GetCountryConfigUseCaseImpl(RegionalizationRepository regionalizationRepository) {
        this.regionalizationRepository = regionalizationRepository;
    }

    @Override
    public Optional<CountryConfig> apply(String countryCode) {
        return regionalizationRepository.findCountryByCode(countryCode)
                .map(country -> {
                    List<City> cities = regionalizationRepository.findCitiesByCountryCode(countryCode);
                    String language = regionalizationRepository.findLanguageByCountryCode(countryCode);
                    List<Currency> currencies = regionalizationRepository.findCurrenciesByCountryCode(countryCode);
                    List<DocumentType> documentTypes = regionalizationRepository.findDocumentTypesByCountryCode(countryCode);
                    
                    return new CountryConfig(country, cities, language, currencies, documentTypes);
                });
    }
}
