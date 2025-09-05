package com.ticketoffice.backend.infra.adapters.out.db.repository;

import com.ticketoffice.backend.domain.models.Sale;
import com.ticketoffice.backend.domain.ports.SaleRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public class SaleInMemoryRepository implements SaleRepository, InMemoryRepository<Sale> {
    private static final Map<String, Sale> data = new HashMap<>();

    @Override
    public List<Sale> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Optional<Sale> getById(String id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Optional<Sale> save(Sale obj, String id) {
        data.put(id, obj);
        return getById(id);
    }

    @Override
    public Optional<Sale> save(Sale obj) {
        String id = Optional.ofNullable(obj.id()).orElse(UUID.randomUUID().toString());
        if (obj.id() == null || obj.id().isEmpty()) {
            obj = obj.getCopyWithUpdatedId(id);
        }
        return save(obj, id);
    }

    @Override
    public Integer count(Predicate<Sale> predicate) {
        return findAll().stream().filter(predicate).toList().size();
    }

    @Override
    public List<Sale> findByEventId(String eventId) {
        return findAll().stream().filter(ticket -> ticket.eventId().equals(eventId)).toList();
    }

    @Override
    public Optional<Sale> update(String id, Sale obj) {
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
