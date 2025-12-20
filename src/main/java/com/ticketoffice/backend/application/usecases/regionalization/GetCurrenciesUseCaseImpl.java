package com.ticketoffice.backend.application.usecases.regionalization;

import com.ticketoffice.backend.domain.models.Currency;
import com.ticketoffice.backend.domain.ports.RegionalizationRepository;
import com.ticketoffice.backend.domain.usecases.regionalization.GetCurrenciesUseCase;

import jakarta.inject.Inject;
import java.util.List;

public class GetCurrenciesUseCaseImpl implements GetCurrenciesUseCase {

    private final RegionalizationRepository regionalizationRepository;

    @Inject
    public GetCurrenciesUseCaseImpl(RegionalizationRepository regionalizationRepository) {
        this.regionalizationRepository = regionalizationRepository;
    }

    @Override
    public List<Currency> apply(String countryCode) {
        return regionalizationRepository.findCurrenciesByCountryCode(countryCode);
    }
}
