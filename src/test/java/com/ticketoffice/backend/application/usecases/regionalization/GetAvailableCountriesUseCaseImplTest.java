package com.ticketoffice.backend.application.usecases.regionalization;

import com.ticketoffice.backend.domain.models.Country;
import com.ticketoffice.backend.domain.ports.RegionalizationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GetAvailableCountriesUseCaseImplTest {

    @Mock
    private RegionalizationRepository regionalizationRepository;
    
    private GetAvailableCountriesUseCaseImpl getAvailableCountriesUseCase;

    @BeforeEach
    void setUp() {
        getAvailableCountriesUseCase = new GetAvailableCountriesUseCaseImpl(regionalizationRepository);
    }

    @Test
    void shouldReturnListOfCountries_WhenRepositoryHasData() {
        // Arrange
        List<Country> expectedCountries = Arrays.asList(
            new Country("US", "United States"),
            new Country("MX", "Mexico")
        );
        
        when(regionalizationRepository.findAllCountries()).thenReturn(expectedCountries);
        
        // Act
        List<Country> result = getAvailableCountriesUseCase.get();
        
// Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(expectedCountries.size(), result.size(), "Should return the same number of countries");
        
        // Verify first country
        assertEquals("US", result.get(0).code(), "First country code should be US");
        assertEquals("United States", result.get(0).name(), "First country name should be United States");
        
        // Verify second country
        assertEquals("MX", result.get(1).code(), "Second country code should be MX");
        assertEquals("Mexico", result.get(1).name(), "Second country name should be Mexico");
    }

    @Test
    void shouldReturnEmptyList_WhenRepositoryReturnsEmptyList() {
        // Arrange
        when(regionalizationRepository.findAllCountries()).thenReturn(Collections.emptyList());
        
        // Act
        List<Country> result = getAvailableCountriesUseCase.get();
        
        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result.isEmpty(), "Result should be an empty list");
        verify(regionalizationRepository).findAllCountries();
    }
    
    @Test
    void shouldReturnEmptyList_WhenRepositoryReturnsNull() {
        // Arrange
        when(regionalizationRepository.findAllCountries()).thenReturn(null);
        
        // Act
        List<Country> result = getAvailableCountriesUseCase.get();
        
        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result.isEmpty(), "Result should be an empty list when repository returns null");
        verify(regionalizationRepository).findAllCountries();
    }
}
