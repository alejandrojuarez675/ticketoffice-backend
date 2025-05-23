package com.ticketoffice.backend.infra.adapters.out.db.repository;

import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.models.Image;
import com.ticketoffice.backend.domain.models.Location;
import com.ticketoffice.backend.domain.models.Organizer;
import com.ticketoffice.backend.domain.models.TicketPrice;
import com.ticketoffice.backend.domain.ports.EventRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class EventInMemoryRepository extends InMemoryRepository<Event> implements EventRepository {

    private EventInMemoryRepository() {
        data.put(
                "cd85b222-2adf-414d-aa26-6a0fb7c87beb",
                new Event(
                        "cd85b222-2adf-414d-aa26-6a0fb7c87beb",
                        "Concierto de Trueno",
                        LocalDateTime.of(2025, 6, 7, 19, 0),
                        new Location(
                                "1",
                                "Movistar Arena",
                                "Dg. 61c #26-36",
                                "Bogotá"
                        ),
                        new Image(
                                "1",
                                "https://movistararena.co/wp-content/uploads/2025/02/trueno-2025-4.jpg",
                                "Concierto de Trueno"
                        ),
                        List.of(
                                new TicketPrice(
                                        "001b2f30-9a84-45e1-9345-518bea8a77c8",
                                        100000.0,
                                        "$",
                                        "VIP",
                                        false
                                ),
                                new TicketPrice(
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
                        new Organizer(
                                "78cd80e2-023f-4b38-a409-d4a20f2d4ac7",
                                "Movistar Arena",
                                "https://movistararena.co/",
                                new Image(
                                        "1",
                                        "https://movistararena.co/wp-content/uploads/2023/08/logo_blanco.png",
                                        "Organizer Logo"
                                )
                        )
                )
        );
    }

    @Override
    public Optional<Event> save(Event event) {
        String id = Optional.ofNullable(event.id()).orElse(UUID.randomUUID().toString());
        return super.save(event, id);
    }
}
