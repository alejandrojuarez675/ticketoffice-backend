package com.ticketoffice.backend.infra.config;

import com.ticketoffice.backend.domain.models.City;
import com.ticketoffice.backend.domain.models.Country;
import com.ticketoffice.backend.domain.models.Currency;
import com.ticketoffice.backend.domain.models.DocumentType;
import java.util.List;

public enum AvailableSites {

    /// --------------------------------------------------------------------------------------------------------------
    ///
    /// ARGENTINA
    ///
    /// --------------------------------------------------------------------------------------------------------------
    ARGENTINA(
            new Country("ARG", "Argentina"),
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
            ),
            List.of(
                    new DocumentType(
                            "ARG_DNI",
                            "DNI",
                            "Documento Nacional de Identidad",
                            "xx.xxx.xxx",
                            "^\\d{8}$"
                    ),
                    new DocumentType(
                            "ARG_CUIT",
                            "CUIT",
                            "Código Único de Identificación Tributaria",
                            "xx-xx.xxx.xxx-x",
                            "^[\\d]{1,3}\\.?[\\d]{3,3}\\.?[\\d]{3,3}$"
                    )
            )
    ),

    /// --------------------------------------------------------------------------------------------------------------
    ///
    /// COLOMBIA
    ///
    /// --------------------------------------------------------------------------------------------------------------
    COLOMBIA(
            new Country("COL", "Colombia"),
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
            ),
            List.of(
                    new DocumentType(
                            "COL_DNI",
                            "DNI",
                            "Documento Nacional de Identidad",
                            "xx.xxx.xxx",
                            "^[\\d]{1,3}\\.?[\\d]{3,3}\\.?[\\d]{3,3}$"
                    )
            )
    );

    private final Country country;
    private final List<City> cities;
    private final String language;
    private final List<Currency> currencies;
    private final List<DocumentType> documentTypes;

    AvailableSites(
            Country country,
            List<City> cities,
            String language,
            List<Currency> currencies,
            List<DocumentType> documentTypes
    ) {
        this.country = country;
        this.cities = cities;
        this.language = language;
        this.currencies = currencies;
        this.documentTypes = documentTypes;
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

    public List<DocumentType> getDocumentTypes() {
        return documentTypes;
    }
}
