package com.ticketoffice.backend.domain.ports;

import com.ticketoffice.backend.domain.models.City;
import com.ticketoffice.backend.domain.models.Country;
import com.ticketoffice.backend.domain.models.Currency;
import com.ticketoffice.backend.domain.models.DocumentType;

import java.util.List;
import java.util.Optional;

public interface RegionalizationRepository {
    List<Country> findAllCountries();
    Optional<Country> findCountryByCode(String countryCode);
    List<City> findCitiesByCountryCode(String countryCode);
    List<Currency> findCurrenciesByCountryCode(String countryCode);
    List<DocumentType> findDocumentTypesByCountryCode(String countryCode);
    String findLanguageByCountryCode(String countryCode);
}
