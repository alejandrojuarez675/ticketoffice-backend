package com.ticketoffice.backend.infra.adapters.in.dto.response.events;

import com.ticketoffice.backend.infra.adapters.in.dto.shared.ImageDTO;
import com.ticketoffice.backend.infra.adapters.in.dto.shared.LocationDTO;
import com.ticketoffice.backend.infra.adapters.in.dto.shared.OrganizerDTO;
import com.ticketoffice.backend.infra.adapters.in.dto.shared.TicketDTO;
import java.time.LocalDateTime;
import java.util.List;

public record EventDetailPageResponse(
        String id,
        String title,
        LocalDateTime date,
        LocationDTO location,
        ImageDTO image,
        List<TicketDTO> tickets,
        String description,
        List<String> additionalInfo,
        OrganizerDTO organizer,
        String status
) {
}
