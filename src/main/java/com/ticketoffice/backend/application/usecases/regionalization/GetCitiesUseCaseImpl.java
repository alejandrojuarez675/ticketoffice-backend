package com.ticketoffice.backend.application.usecases.regionalization;

import com.ticketoffice.backend.domain.models.City;
import com.ticketoffice.backend.domain.ports.RegionalizationRepository;
import com.ticketoffice.backend.domain.usecases.regionalization.GetCitiesUseCase;
import jakarta.inject.Inject;

import java.util.List;

public class GetCitiesUseCaseImpl implements GetCitiesUseCase {

    private final RegionalizationRepository regionalizationRepository;

    @Inject
    public GetCitiesUseCaseImpl(RegionalizationRepository regionalizationRepository) {
        this.regionalizationRepository = regionalizationRepository;
    }

    @Override
    public List<City> apply(String countryCode) {
        return regionalizationRepository.findCitiesByCountryCode(countryCode);
    }
}
