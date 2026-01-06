package com.ticketoffice.backend.infra.adapters.in.handlers;

import com.ticketoffice.backend.domain.models.City;
import com.ticketoffice.backend.domain.models.Country;
import com.ticketoffice.backend.domain.models.Currency;
import com.ticketoffice.backend.domain.models.DocumentType;
import com.ticketoffice.backend.domain.usecases.regionalization.GetAvailableCountriesUseCase;
import com.ticketoffice.backend.domain.usecases.regionalization.GetCountryConfigUseCase;
import com.ticketoffice.backend.domain.usecases.regionalization.GetCountryConfigUseCase.CountryConfig;
import com.ticketoffice.backend.infra.adapters.in.dto.response.config.CountryConfigDto;
import com.ticketoffice.backend.infra.adapters.in.dto.response.config.CountryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegionalizationHandlerTest {

    @Mock
    private GetAvailableCountriesUseCase getAvailableCountriesUseCase;
    
    @Mock
    private GetCountryConfigUseCase getCountryConfigUseCase;
    
    private RegionalizationHandler regionalizationHandler;

    @BeforeEach
    void setUp() {
        regionalizationHandler = new RegionalizationHandler(
            getAvailableCountriesUseCase,
            getCountryConfigUseCase
        );
    }

    @Test
    void shouldReturnListOfCountryDtos_WhenGettingCountryList() {
        // Arrange
        List<Country> countries = List.of(
            new Country("US", "United States"),
            new Country("MX", "Mexico")
        );
        
        when(getAvailableCountriesUseCase.get()).thenReturn(countries);
        
        // Act
        List<CountryDto> result = regionalizationHandler.getCountryList();
        
// Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        
        // Verify first country
        CountryDto usDto = result.getFirst();
        assertEquals("US", usDto.getCode());
        assertEquals("United States", usDto.getName());
        
        // Verify second country
        CountryDto mxDto = result.get(1);
        assertEquals("MX", mxDto.getCode());
        assertEquals("Mexico", mxDto.getName());
    }

    @Test
    void shouldReturnEmptyList_WhenNoCountriesAvailable() {
        // Arrange
        when(getAvailableCountriesUseCase.get()).thenReturn(List.of());
        
        // Act
        List<CountryDto> result = regionalizationHandler.getCountryList();
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    void shouldReturnCountryConfigDto_WhenCountryCodeIsValid() {
        // Arrange
        String countryCode = "US";
        Country country = new Country("US", "United States");
        List<City> cities = List.of(new City("NYC", "New York City"));
        List<Currency> currencies = List.of(new Currency("USD", "US Dollar", "$"));
        List<DocumentType> documentTypes = List.of(
            new DocumentType("PASSPORT", "Passport", "Passport document", "^[A-Z0-9]{6,9}$", "[A-Z0-9]{6,9}")
        );
        
        CountryConfig config = new CountryConfig(
            country,
            cities,
            "en",
            currencies,
            documentTypes
        );
        
        when(getCountryConfigUseCase.apply(countryCode)).thenReturn(Optional.of(config));
        
        // Act
        Optional<CountryConfigDto> result = regionalizationHandler.getCountryConfig(countryCode);
        
        // Assert
        assertTrue(result.isPresent());
        CountryConfigDto dto = result.get();
        
        // Verify country
        assertEquals("US", dto.data().getCode());
        assertEquals("United States", dto.data().getName());
        
        // Verify cities
        assertEquals(1, dto.cities().size());
        assertEquals("NYC", dto.cities().getFirst().getCode());
        assertEquals("New York City", dto.cities().getFirst().getName());
        
        // Verify language
        assertEquals("en", dto.language());
        
        // Verify currencies
        assertEquals(1, dto.availableCurrencies().size());
        assertEquals("USD", dto.availableCurrencies().getFirst().getCode());
        
        // Verify document types
        assertEquals(1, dto.documentType().size());
        assertEquals("PASSPORT", dto.documentType().getFirst().code());
    }

    @Test
    void shouldReturnEmptyOptional_WhenCountryCodeIsInvalid() {
        // Arrange
        String countryCode = "INVALID";
        when(getCountryConfigUseCase.apply(countryCode)).thenReturn(Optional.empty());
        
        // Act
        Optional<CountryConfigDto> result = regionalizationHandler.getCountryConfig(countryCode);
        
        // Assert
        assertTrue(result.isEmpty());
    }
}
