package com.ticketoffice.backend.infra.adapters.in.dto.request;

import com.ticketoffice.backend.infra.adapters.in.dto.shared.ImageDTO;
import com.ticketoffice.backend.infra.adapters.in.dto.shared.LocationDTO;
import com.ticketoffice.backend.infra.adapters.in.dto.shared.PriceDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

public record EventCrudRequest(

        @Schema(description = "The title of the event", example = "Concierto de Trueno")
        String title,

        @Schema(description = "The date of the event", example = "2022-01-01T20:00:00")
        LocalDateTime date,

        @Schema(description = "The location of the event")
        LocationDTO location,

        @Schema(description = "The image of the event")
        ImageDTO image,

        @Schema(description = "The prices of the event")
        List<PriceDTO> prices,

        @Schema(description = "The description of the event", examples = "Trueno es uno de los grandes artistas latinoamericanos de nuestros tiempos. Un rapero que comenzó destacándose en las batallas de freestyle en su natal Buenos Aires y hoy ha logrado el reconocimiento internacional además de acumular millones de oyentes en plataformas digitales. Este 7 de junio el Movistar Arena será testigo del prime de uno de los nuevos grandes del universo urbano.")
        String description,

        @Schema(
                description = "The additional info of the event",
                example = "[\"Prohibido el ingreso de menores de edad\", \"No se permite el ingreso de alimentos y bebidas\", \"No se permite el ingreso de cámaras fotográficas\"]"
        )
        List<String> additionalInfo,

        @Schema(
                description = "The organizer of the event information",
                example = "001b2f30-9a84-45e1-9345-518bea8a77c8"
        )
        String organizerId
) {
}
