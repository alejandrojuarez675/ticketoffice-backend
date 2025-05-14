package com.ticketoffice.backend.infra.adapters.in.dto.mapper;

import com.ticketoffice.backend.domain.models.Image;
import com.ticketoffice.backend.infra.adapters.in.dto.shared.ImageDTO;

public class ImageDtoMapper {

    public static ImageDTO getFromImage(Image image) {
        return new ImageDTO(
                image.url(),
                image.alt()
        );
    }
}
