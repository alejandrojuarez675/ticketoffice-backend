package com.ticketoffice.backend.infra.adapters.in.dto.mapper;

import com.ticketoffice.backend.domain.models.Location;
import com.ticketoffice.backend.infra.adapters.in.dto.shared.LocationDTO;

public class LocationDtoMapper {

    public static LocationDTO getFromLocation(Location location) {
        return new LocationDTO(
                location.name(),
                location.address(),
                location.city()
        );
    }
}
