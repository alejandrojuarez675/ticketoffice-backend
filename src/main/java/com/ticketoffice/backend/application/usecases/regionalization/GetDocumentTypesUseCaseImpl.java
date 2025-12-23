package com.ticketoffice.backend.application.usecases.regionalization;

import com.ticketoffice.backend.domain.models.DocumentType;
import com.ticketoffice.backend.domain.ports.RegionalizationRepository;
import com.ticketoffice.backend.domain.usecases.regionalization.GetDocumentTypesUseCase;

import jakarta.inject.Inject;
import java.util.List;

public class GetDocumentTypesUseCaseImpl implements GetDocumentTypesUseCase {

    private final RegionalizationRepository regionalizationRepository;

    @Inject
    public GetDocumentTypesUseCaseImpl(RegionalizationRepository regionalizationRepository) {
        this.regionalizationRepository = regionalizationRepository;
    }

    @Override
    public List<DocumentType> apply(String countryCode) {
        return regionalizationRepository.findDocumentTypesByCountryCode(countryCode);
    }
}
