package com.ticketoffice.backend.application.usecases.regionalization;

import com.ticketoffice.backend.domain.models.Country;
import com.ticketoffice.backend.domain.ports.RegionalizationRepository;
import com.ticketoffice.backend.domain.usecases.regionalization.GetAvailableCountriesUseCase;

import jakarta.inject.Inject;
import java.util.List;

public class GetAvailableCountriesUseCaseImpl implements GetAvailableCountriesUseCase {

    private final RegionalizationRepository regionalizationRepository;

    @Inject
    public GetAvailableCountriesUseCaseImpl(RegionalizationRepository regionalizationRepository) {
        this.regionalizationRepository = regionalizationRepository;
    }

    @Override
    public List<Country> get() {
        return regionalizationRepository.findAllCountries();
    }
}
