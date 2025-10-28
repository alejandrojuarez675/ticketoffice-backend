package com.ticketoffice.backend.infra.adapters.out.db.repository.event;

import com.google.inject.Inject;
import com.ticketoffice.backend.domain.enums.EventStatus;
import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.models.Image;
import com.ticketoffice.backend.domain.models.Location;
import com.ticketoffice.backend.domain.ports.EventRepository;
import com.ticketoffice.backend.infra.adapters.out.db.dao.EventDao;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public class EventDynamoRepository implements EventRepository {

    private final EventDao eventDao;

    @Inject
    public EventDynamoRepository(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    @Override
    public Optional<Event> getById(String id) {
        Map<String, AttributeValue> eventMap = eventDao.getEventById(id);
        return Optional.ofNullable(eventMap)
                .filter(map -> map.containsKey("id"))
                .map(this::fromMap);
    }

    private Event fromMap(Map<String, AttributeValue> em) {
        // TODO continue with the map
        return new Event(
                em.get("id").s(),
                em.get("title").s(),
                LocalDateTime.parse(em.get("date").s()),
                new Location(
                       "id-location",
                       "name-location",
                       "address-location",
                       em.get("city").s(),
                       em.get("country").s()
                ),
                new Image(
                        "id-image",
                        "url",
                        "alt"
                ),
                List.of(),
                em.get("description").s(),
                List.of(),
                em.get("organizerId").s(),
                EventStatus.valueOf(em.get("status").s())
        );
    }

    @Override
    public Optional<Event> save(Event event) {
        return Optional.empty();
    }

    @Override
    public List<Event> findAll() {
        return List.of();
    }

    @Override
    public Optional<Event> update(String id, Event event) {
        return Optional.empty();
    }

    @Override
    public Optional<Event> getByIdAndOrganizerId(String id, String id1) {
        return Optional.empty();
    }

    @Override
    public List<Event> findByUserId(String userId) {
        return List.of();
    }

    @Override
    public List<Event> search(Predicate<Event> predicate, Integer pageSize, Integer pageNumber) {
        return List.of();
    }

    @Override
    public Integer count(Predicate<Event> predicate) {
        return 0;
    }
}
