package com.ticketoffice.backend.infra.adapters.out.db.repository;

import com.ticketoffice.backend.domain.models.Purchase;
import com.ticketoffice.backend.domain.models.User;
import com.ticketoffice.backend.domain.ports.PurchaseRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class PurchaseInMemoryRepository implements PurchaseRepository, InMemoryRepository<Purchase> {
    private static final Map<String, Purchase> data = new HashMap<>();

    @Override
    public List<Purchase> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Optional<Purchase> getById(String id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Optional<Purchase> save(Purchase obj, String id) {
        data.put(id, obj);
        return getById(id);
    }

    @Override
    public Optional<Purchase> save(Purchase obj) {
        String id = Optional.ofNullable(obj.id()).orElse(UUID.randomUUID().toString());
        if (obj.id() == null || obj.id().isEmpty()) {
            obj = obj.getCopyWithUpdatedId(id);
        }
        return save(obj, id);
    }

    @Override
    public Optional<Purchase> update(String id, Purchase obj) {
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
