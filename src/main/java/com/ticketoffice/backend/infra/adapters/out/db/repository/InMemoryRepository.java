package com.ticketoffice.backend.infra.adapters.out.db.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryRepository<T> {

    @SuppressWarnings("rawtypes")
    protected static final Map<String, Object> data = new HashMap();

    public Optional<T> getById(String id) {
        return Optional.ofNullable((T) data.get(id));
    }

    public Optional<T> save(T obj) {
        String id = UUID.randomUUID().toString();
        data.put(id, obj);

        return getById(id);
    }

    public Optional<T> update(String id, T obj) {
        data.put(id, obj);
        return getById(id);
    }

    public void delete(String id) {
        data.remove(id);
    }
}
