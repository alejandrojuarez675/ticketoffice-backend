package com.ticketoffice.backend.infra.adapters.out.db.repository;

import com.ticketoffice.backend.domain.models.User;
import com.ticketoffice.backend.domain.ports.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class UserInMemoryRepository extends InMemoryRepository<User> implements UserRepository {

    @Override
    public Optional<User> findByEmail(String email) {
        return findAll()
                .stream()
                .filter(u -> u.getEmail().equals(email))
                .findAny();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return findAll()
                .stream()
                .filter(u -> u.getUsername().equals(username))
                .findAny();
    }

    @Override
    public Optional<User> save(User user) {
        if (user.getId() == null || user.getId().isEmpty()) {
            user.setId(UUID.randomUUID().toString());
        }
        return super.save(user, user.getId());
    }
}
