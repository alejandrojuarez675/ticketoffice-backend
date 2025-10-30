package com.ticketoffice.backend.infra.adapters.out.db.repository.user;

import com.google.inject.Inject;
import com.ticketoffice.backend.domain.models.User;
import com.ticketoffice.backend.domain.ports.UserRepository;
import com.ticketoffice.backend.infra.adapters.out.db.dao.UserDynamoDao;

import java.util.List;
import java.util.Optional;

public class UserDynamoRepository implements UserRepository {

    private final UserDynamoDao userDynamoDao;

    @Inject
    public UserDynamoRepository(UserDynamoDao userDynamoDao) {
        this.userDynamoDao = userDynamoDao;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public Optional<User> save(User user) {
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return List.of();
    }

    @Override
    public Optional<User> getById(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> update(String id, User user) {
        return Optional.empty();
    }
}
