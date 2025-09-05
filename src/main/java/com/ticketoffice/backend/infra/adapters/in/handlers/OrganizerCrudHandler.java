package com.ticketoffice.backend.infra.adapters.in.handlers;

import com.ticketoffice.backend.domain.exception.ErrorOnPersistDataException;
import com.ticketoffice.backend.domain.exception.NotAuthenticatedException;
import com.ticketoffice.backend.domain.models.Organizer;
import com.ticketoffice.backend.domain.usecases.organizer.CreateOrganizerUseCase;
import com.ticketoffice.backend.infra.adapters.in.dto.mapper.ImageDtoMapper;
import com.ticketoffice.backend.infra.adapters.in.dto.request.OrganizerCrudRequest;
import com.ticketoffice.backend.infra.adapters.in.exception.BadRequestException;

public class OrganizerCrudHandler {

    private final CreateOrganizerUseCase createOrganizerUseCase;

    public OrganizerCrudHandler(CreateOrganizerUseCase createOrganizerUseCase) {
        this.createOrganizerUseCase = createOrganizerUseCase;
    }

    public void createOrganizer(OrganizerCrudRequest organizer) throws BadRequestException {
        try {
            createOrganizerUseCase.accept(
                    new Organizer(
                            null,
                            organizer.name(),
                            organizer.url(),
                            ImageDtoMapper.getFromImageDTO(organizer.logo())
                    )
            );
        } catch (NotAuthenticatedException e) {
            throw new BadRequestException(e.getMessage());
        } catch (ErrorOnPersistDataException e) {
            throw new RuntimeException(e);
        }
    }
}
