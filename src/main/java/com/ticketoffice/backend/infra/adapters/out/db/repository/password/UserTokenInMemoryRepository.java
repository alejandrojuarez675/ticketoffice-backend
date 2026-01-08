package com.ticketoffice.backend.infra.adapters.out.db.repository.password;

import com.ticketoffice.backend.domain.models.UserToken;
import com.ticketoffice.backend.domain.ports.UserTokenRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserTokenInMemoryRepository implements UserTokenRepository {

    private static final Map<String, UserToken> data = new HashMap<>();

    public Optional<UserToken> getById(String id) {
        return Optional.ofNullable(data.get(id));
    }
    public List<UserToken> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Optional<UserToken> save(UserToken model) {
        data.put(model.id(), model);
        return getById(model.id());
    }

    @Override
    public Optional<UserToken> findByHashToken(String hashToken) {
        return findAll().stream()
                .filter(x -> hashToken.equals(x.tokenHash()))
                .findFirst();
    }
}
