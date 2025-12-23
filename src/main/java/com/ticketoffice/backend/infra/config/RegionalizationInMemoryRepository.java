package com.ticketoffice.backend.infra.config;

import com.ticketoffice.backend.domain.models.City;
import com.ticketoffice.backend.domain.models.Country;
import com.ticketoffice.backend.domain.models.Currency;
import com.ticketoffice.backend.domain.models.DocumentType;
import com.ticketoffice.backend.domain.ports.RegionalizationRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RegionalizationInMemoryRepository implements RegionalizationRepository {

    @Override
    public List<Country> findAllCountries() {
        return Stream.of(AvailableSites.values())
                .map(AvailableSites::getCountry)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Country> findCountryByCode(String countryCode) {
        return Stream.of(AvailableSites.values())
                .filter(site -> site.getCountry().code().equalsIgnoreCase(countryCode))
                .findFirst()
                .map(AvailableSites::getCountry);
    }

    @Override
    public List<City> findCitiesByCountryCode(String countryCode) {
        return Stream.of(AvailableSites.values())
                .filter(site -> site.getCountry().code().equalsIgnoreCase(countryCode))
                .findFirst()
                .map(AvailableSites::getCities)
                .orElse(List.of());
    }

    @Override
    public List<Currency> findCurrenciesByCountryCode(String countryCode) {
        return Stream.of(AvailableSites.values())
                .filter(site -> site.getCountry().code().equalsIgnoreCase(countryCode))
                .findFirst()
                .map(AvailableSites::getCurrencies)
                .orElse(List.of());
    }

    @Override
    public List<DocumentType> findDocumentTypesByCountryCode(String countryCode) {
        return Stream.of(AvailableSites.values())
                .filter(site -> site.getCountry().code().equalsIgnoreCase(countryCode))
                .findFirst()
                .map(AvailableSites::getDocumentTypes)
                .orElse(List.of());
    }

    @Override
    public String findLanguageByCountryCode(String countryCode) {
        return Stream.of(AvailableSites.values())
                .filter(site -> site.getCountry().code().equalsIgnoreCase(countryCode))
                .findFirst()
                .map(AvailableSites::getLanguage)
                .orElse("es"); // Default to English if not found
    }
}
