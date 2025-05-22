package com.ticketoffice.backend.infra.adapters.in.dto.mapper;

import com.ticketoffice.backend.domain.models.Organizer;
import com.ticketoffice.backend.infra.adapters.in.dto.shared.OrganizerDTO;

public class OrganizerDtoMapper {

    public static OrganizerDTO getFromOrganizer(Organizer organizer) {
        return new OrganizerDTO(
                organizer.id(),
                organizer.name(),
                organizer.url(),
                organizer.logo() != null ? ImageDtoMapper.getFromImage(organizer.logo()) : null
        );
    }
}
