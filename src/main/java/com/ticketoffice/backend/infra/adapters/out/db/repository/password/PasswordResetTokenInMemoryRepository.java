package com.ticketoffice.backend.infra.adapters.out.db.repository.password;

import com.ticketoffice.backend.domain.models.PasswordResetToken;
import com.ticketoffice.backend.domain.ports.PasswordResetTokenRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PasswordResetTokenInMemoryRepository implements PasswordResetTokenRepository {

    private static final Map<String, PasswordResetToken> data = new HashMap<>();

    public Optional<PasswordResetToken> getById(String id) {
        return Optional.ofNullable(data.get(id));
    }
    public List<PasswordResetToken> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Optional<PasswordResetToken> save(PasswordResetToken model) {
        data.put(model.id(), model);
        return getById(model.id());
    }

    @Override
    public Optional<PasswordResetToken> findByHashToken(String hashToken) {
        return findAll().stream()
                .filter(x -> hashToken.equals(x.tokenHash()))
                .findFirst();
    }
}
