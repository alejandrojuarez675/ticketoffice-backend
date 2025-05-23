package com.ticketoffice.backend.infra.adapters.out.db.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryRepository<T> {

    @SuppressWarnings("rawtypes")
    protected static final Map<String, Object> data = new HashMap();

    public List<T> findAll() {
        return data.values().stream()
                .map(obj -> (T) obj)
                .toList();
    }

    public Optional<T> getById(String id) {
        Object o = data.get(id);
        T obj = (T) o;
        return Optional.ofNullable(obj);
    }

    public Optional<T> save(T obj, String id) {;
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
