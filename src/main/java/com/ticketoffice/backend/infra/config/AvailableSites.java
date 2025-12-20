package com.ticketoffice.backend.infra.config;

import com.ticketoffice.backend.domain.models.City;
import com.ticketoffice.backend.domain.models.Country;
import com.ticketoffice.backend.domain.models.Currency;
import java.util.List;

public enum AvailableSites {

    /// --------------------------------------------------------------------------------------------------------------
    ///
    /// ARGENTINA
    ///
    /// --------------------------------------------------------------------------------------------------------------
    ARGENTINA(
            new Country("ARGENTINA", "Argentina"),
            List.of(
                    new City("BUENOS_AIRES", "Buenos Aires"),
                    new City("CORDOBA", "Córdoba"),
                    new City("ROSARIO", "Rosario"),
                    new City("MENDOZA", "Mendoza"),
                    new City("PARANA", "Paraná")
            ),
            "es",
            List.of(
                    new Currency("ARS", "Pesos", "$")
            )
    ),

    /// --------------------------------------------------------------------------------------------------------------
    ///
    /// COLOMBIA
    ///
    /// --------------------------------------------------------------------------------------------------------------
    COLOMBIA(
            new Country("COLOMBIA", "Colombia"),
            List.of(
                    new City("BOGOTA", "Bogotá"),
                    new City("MEDELLIN", "Medellín"),
                    new City("CALI", "Cali"),
                    new City("BARRANQUILLA", "Barranquilla"),
                    new City("ARMENIA", "Armenia")
            ),
            "es",
            List.of(
                    new Currency("COP", "Pesos", "$")
            )
    );

    private final Country country;
    private final List<City> cities;
    private final String language;
    private final List<Currency> currencies;

    AvailableSites(
            Country country,
            List<City> cities,
            String language,
            List<Currency> currencies
    ) {
        this.country = country;
        this.cities = cities;
        this.language = language;
        this.currencies = currencies;
    }

    public Country getCountry() {
        return country;
    }

    public List<City> getCities() {
        return cities;
    }

    public String getLanguage() {
        return language;
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }
}
