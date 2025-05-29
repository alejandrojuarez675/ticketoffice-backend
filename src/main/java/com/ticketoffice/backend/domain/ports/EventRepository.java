package com.ticketoffice.backend.domain.ports;

import com.ticketoffice.backend.domain.models.Event;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface EventRepository {
    Optional<Event> getById(String id);

    Optional<Event> save(Event event);

    List<Event> findAll();

    Optional<Event> update(String id, Event event);

    Optional<Event> getByIdAndOrganizerId(String id, String id1);

    List<Event> findByUserId(String userId);

    List<Event> search(List<Predicate<Event>> predicates, Integer pageSize, Integer pageNumber);

    Integer count(List<Predicate<Event>> predicates);
}
