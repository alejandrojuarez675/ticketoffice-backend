package com.ticketoffice.backend.infra.adapters.out.db.repository;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface InMemoryRepository<T> {

    List<T> findAll();

    Optional<T> getById(String id);

    Optional<T> save(T obj, @NotNull String id);

    Optional<T> update(String id, T obj);

    void delete(String id);
}
