package com.ticketoffice.backend.application.usecases.regionalization;

import com.ticketoffice.backend.domain.models.City;
import com.ticketoffice.backend.domain.models.Country;
import com.ticketoffice.backend.domain.models.Currency;
import com.ticketoffice.backend.domain.models.DocumentType;
import com.ticketoffice.backend.domain.ports.RegionalizationRepository;
import com.ticketoffice.backend.domain.usecases.regionalization.GetCountryConfigUseCase.CountryConfig;
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
class GetCountryConfigUseCaseImplTest {

    @Mock
    private RegionalizationRepository regionalizationRepository;
    
    private GetCountryConfigUseCaseImpl getCountryConfigUseCase;

    @BeforeEach
    void setUp() {
        getCountryConfigUseCase = new GetCountryConfigUseCaseImpl(regionalizationRepository);
    }

    @Test
    void apply_WithValidCountryCode_ShouldReturnCountryConfig() {
        // Arrange
        String countryCode = "US";
        Country country = new Country(countryCode, "United States");
        List<City> cities = List.of(new City("NYC", "New York City"));
        List<Currency> currencies = List.of(new Currency("USD", "US Dollar", "$"));
        List<DocumentType> documentTypes = List.of(
            new DocumentType("PASSPORT", "Passport", "Passport document", "^[A-Z0-9]{6,9}$", "[A-Z0-9]{6,9}")
        );

        when(regionalizationRepository.findCountryByCode(countryCode)).thenReturn(Optional.of(country));
        when(regionalizationRepository.findCitiesByCountryCode(countryCode)).thenReturn(cities);
        when(regionalizationRepository.findCurrenciesByCountryCode(countryCode)).thenReturn(currencies);
        when(regionalizationRepository.findDocumentTypesByCountryCode(countryCode)).thenReturn(documentTypes);
        when(regionalizationRepository.findLanguageByCountryCode(countryCode)).thenReturn("en");

        // Act
        Optional<CountryConfig> result = getCountryConfigUseCase.apply(countryCode);
        
        // Assert
        assertTrue(result.isPresent());
        CountryConfig config = result.get();
        
        assertEquals(countryCode, config.country().code());
        assertEquals("United States", config.country().name());
        assertEquals(1, config.cities().size());
        assertEquals("NYC", config.cities().getFirst().code());
        assertEquals("en", config.language());
        assertEquals(1, config.currencies().size());
        assertEquals("USD", config.currencies().getFirst().code());
        assertEquals(1, config.documentTypes().size());
        assertEquals("PASSPORT", config.documentTypes().getFirst().code());
    }

    @Test
    void apply_WithInvalidCountryCode_ShouldReturnEmpty() {
        // Arrange
        String countryCode = "INVALID";
        when(regionalizationRepository.findCountryByCode(countryCode)).thenReturn(Optional.empty());
        
        // Act
        Optional<CountryConfig> result = getCountryConfigUseCase.apply(countryCode);
        
        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void apply_WithNullCountryCode_ShouldReturnEmpty() {
        // Act
        Optional<CountryConfig> result = getCountryConfigUseCase.apply(null);
        
        // Assert
        assertTrue(result.isEmpty());
    }
}
