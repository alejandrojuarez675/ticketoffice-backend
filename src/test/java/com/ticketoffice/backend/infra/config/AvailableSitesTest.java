package com.ticketoffice.backend.infra.config;

import static org.junit.jupiter.api.Assertions.*;
import com.ticketoffice.backend.domain.models.City;
import com.ticketoffice.backend.domain.models.Country;
import java.util.Arrays;
import java.util.HashSet;
import org.junit.jupiter.api.Test;

class AvailableSitesTest {

    @Test
    void couldNotHaveDuplicateCountryCodes() {
        var countries = AvailableSites.values();
        var countryCodes = Arrays.stream(countries)
                .map(AvailableSites::getCountry)
                .map(Country::code)
                .toList();

        assertEquals(countries.length, new HashSet<>(countryCodes).size());
    }


    @Test
    void citiesCouldNotHaveDuplicateCodes() {
        var cities = Arrays.stream(AvailableSites.values())
                .flatMap(site -> site.getCities().stream())
                .toList();

        var cityCodes = cities.stream()
                .map(City::code)
                .toList();

        assertEquals(cities.size(), new HashSet<>(cityCodes).size());
    }
}