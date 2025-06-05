package com.ticketoffice.backend.infra.adapters.out.db.repository;

import com.ticketoffice.backend.domain.models.Ticket;
import com.ticketoffice.backend.domain.ports.TicketRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import org.springframework.stereotype.Repository;

@Repository
public class TicketInMemoryRepository implements TicketRepository, InMemoryRepository<Ticket> {
    private static final Map<String, Ticket> data = new HashMap<>();

    @Override
    public List<Ticket> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Optional<Ticket> getById(String id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Optional<Ticket> save(Ticket obj, String id) {
        data.put(id, obj);
        return getById(id);
    }

    @Override
    public Optional<Ticket> save(Ticket obj) {
        String id = Optional.ofNullable(obj.id()).orElse(UUID.randomUUID().toString());
        if (obj.id() == null || obj.id().isEmpty()) {
            obj = obj.getCopyWithUpdatedId(id);
        }
        return save(obj, id);
    }

    @Override
    public Integer count(Predicate<Ticket> predicate) {
        return findAll().stream().filter(predicate).toList().size();
    }

    @Override
    public List<Ticket> findByEventId(String eventId) {
        return findAll().stream().filter(ticket -> ticket.eventId().equals(eventId)).toList();
    }

    @Override
    public Optional<Ticket> update(String id, Ticket obj) {
        if (data.containsKey(id)) {
            data.put(id, obj);
            return getById(id);
        }
        return Optional.empty();
    }

    @Override
    public void delete(String id) {
        data.remove(id);
    }
}
