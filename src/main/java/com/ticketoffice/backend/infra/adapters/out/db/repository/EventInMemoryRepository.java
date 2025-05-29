package com.ticketoffice.backend.infra.adapters.out.db.repository;

import com.ticketoffice.backend.domain.enums.EventStatus;
import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.models.Image;
import com.ticketoffice.backend.domain.models.Location;
import com.ticketoffice.backend.domain.models.TicketPrice;
import com.ticketoffice.backend.domain.ports.EventRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import org.springframework.stereotype.Repository;

@Repository
public class EventInMemoryRepository implements InMemoryRepository<Event>, EventRepository {

    private static final Map<String, Event> data = new HashMap<>();

    EventInMemoryRepository() {
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
                                "Bogotá",
                                "Colombia"
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
                        "78cd80e2-023f-4b38-a409-d4a20f2d4ac7",
                        EventStatus.ACTIVE
                )
        );
    }

    @Override
    public Optional<Event> save(Event event) {
        String id = Optional.ofNullable(event.id()).orElse(UUID.randomUUID().toString());
        if (event.id() == null || event.id().isEmpty()) {
            event = event.getCopyWithUpdatedId(id);
        }
        return save(event, id);
    }

    @Override
    public List<Event> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Optional<Event> getById(String id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Optional<Event> save(Event obj, String id) {
        data.put(id, obj);
        return getById(id);
    }

    @Override
    public Optional<Event> update(String id, Event obj) {
        if (data.containsKey(id)) {
            data.put(id, obj);
            return getById(id);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Event> getByIdAndOrganizerId(String id, String id1) {
        return getById(id).filter(event -> event.organizerId().equals(id1));
    }

    @Override
    public List<Event> findByUserId(String userId) {
        return findAll().stream()
                .filter(event -> event.organizerId().equals(userId))
                .toList();
    }

    @Override
    public List<Event> search(List<Predicate<Event>> predicates, Integer pageSize, Integer pageNumber) {
        List<Event> events = findAll().stream()
                .filter(event -> predicates.stream().allMatch(predicate -> predicate.test(event)))
                .toList();

        if (events.isEmpty()) {
            return List.of();
        }

        // If pageSize is greater than or equal to the total number of items, return all items
        if (pageSize >= events.size()) {
            return events;
        }

        int start = pageNumber * pageSize;
        int end = Math.min((pageNumber + 1) * pageSize, events.size());

        // If start is greater than size, return an empty list
        if (start >= events.size()) {
            return List.of();
        }

        return events.subList(start, end);
    }

    @Override
    public Integer count(List<Predicate<Event>> predicates) {
        return findAll().stream()
                .filter(event -> predicates.stream()
                        .allMatch(predicate -> predicate.test(event)))
                .toList().size();
    }

    @Override
    public void delete(String id) {
        data.remove(id);
    }
}
