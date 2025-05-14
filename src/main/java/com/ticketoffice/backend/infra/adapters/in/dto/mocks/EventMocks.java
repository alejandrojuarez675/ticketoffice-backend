package com.ticketoffice.backend.infra.adapters.in.dto.mocks;

import com.ticketoffice.backend.infra.adapters.in.dto.response.events.EventForVipResponse;
import com.ticketoffice.backend.infra.adapters.in.dto.response.events.EventListResponse;
import com.ticketoffice.backend.infra.adapters.in.dto.response.events.EventResponse;
import com.ticketoffice.backend.infra.adapters.in.dto.response.events.EventLightResponse;
import com.ticketoffice.backend.infra.adapters.in.dto.response.events.SocialMediasResponse;
import com.ticketoffice.backend.infra.adapters.in.dto.shared.EventListItemDTO;
import com.ticketoffice.backend.infra.adapters.in.dto.shared.ImageDTO;
import com.ticketoffice.backend.infra.adapters.in.dto.shared.LocationDTO;
import com.ticketoffice.backend.infra.adapters.in.dto.shared.OrganizerDTO;
import com.ticketoffice.backend.infra.adapters.in.dto.shared.PriceDTO;
import java.time.LocalDateTime;
import java.util.List;

public class EventMocks {

    public final static EventLightResponse eventLightDTO = new EventLightResponse(
            "1",
            "Event 1",
            "20215-12-12",
            "Location 1",
            "City 1",
            "Category 1",
            "Banner URL 1",
            0.0
    );

    public static EventResponse eventResponse = new EventResponse(
            "1",
            "Event 1",
            "Description 1",
            "20215-12-12",
            "Time 1",
            "Location 1",
            "City 1",
            "Country 1",
            "Banner URL 1",
            "Category 1",
            "Status 1",
            "Type 1",
            "Organizer 1",
            "Organizer ID 1",
            "Organizer Email 1",
            "Organizer Phone 1",
            "Organizer Address 1",
            "Organizer City 1",
            "Organizer Country 1",
            "Organizer Image 1",
            "Organizer Description 1",
            "Organizer Website 1",
            "Organizer Social Media 1"
    );

    public static SocialMediasResponse socialMediasResponse = new SocialMediasResponse(
            "https://facebook.com/event1",
            "https://instagram.com/event1",
            "https://twitter.com/event1",
            "https://youtube.com/event1",
            "https://tiktok.com/event1",
            "https://website.com/event1"
    );

    public final static String LSDF = "https://facebook.com/event1";

    public static final EventForVipResponse eventForVipResponse = new EventForVipResponse(
            "cbb46e0f-1014-41f9-bf63-ba1203197ce8",
            "Concierto de Trueno",
            LocalDateTime.of(2025, 6, 7, 19, 0),
            new LocationDTO(
                    "Movistar Arena",
                    "Dg. 61c #26-36",
                    "Bogotá"
            ),
            new ImageDTO(
                "https://movistararena.co/wp-content/uploads/2025/02/trueno-2025-4.jpg",
                "Concierto de Trueno"
            ),
            List.of(
                    new PriceDTO(
                            "001b2f30-9a84-45e1-9345-518bea8a77c8",
                            100000.0,
                            "$",
                            "VIP",
                            false
                    ),
                    new PriceDTO(
                            "cd85b222-2adf-414d-aa26-6a0fb7c87beb",
                            30000.0,
                            "$",
                            "General",
                            false
                    )
            ),
            "Trueno es uno de los grandes artistas latinoamericanos de nuestros tiempos. Un rapero que comenzó destacándose en las batallas de freestyle en su natal Buenos Aires y hoy ha logrado el reconocimiento internacional además de acumular millones de oyentes en plataformas digitales. Este 7 de junio el Movistar Arena será testigo del prime de uno de los nuevos grandes del universo urbano.",
            List.of(
                    "Prohibido el ingreso de menores de edad",
                    "No se permite el ingreso de alimentos y bebidas",
                    "No se permite el ingreso de cámaras fotográficas"
            ),
            new OrganizerDTO(
                    "78cd80e2-023f-4b38-a409-d4a20f2d4ac7",
                    "Movistar Arena",
                    "https://movistararena.co/",
                    new ImageDTO(
                            "https://movistararena.co/wp-content/uploads/2023/08/logo_blanco.png",
                            "Organizer Logo"
                    )
            )
    );

    public static EventListItemDTO eventListItemDTO = new EventListItemDTO(
            "1",
            "Event 1",
            "20215-12-12",
            "Location 1",
            "Status 1",
            "Type 1"
    );

    public static EventListResponse eventListResponse = new EventListResponse(List.of(eventListItemDTO));
}
