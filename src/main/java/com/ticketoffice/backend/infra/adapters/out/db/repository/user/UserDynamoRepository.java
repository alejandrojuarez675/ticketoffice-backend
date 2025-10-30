package com.ticketoffice.backend.infra.adapters.out.db.repository.user;

import com.google.inject.Inject;
import com.ticketoffice.backend.domain.models.User;
import com.ticketoffice.backend.domain.ports.UserRepository;
import com.ticketoffice.backend.infra.adapters.out.db.dao.UserDynamoDao;
import com.ticketoffice.backend.infra.adapters.out.db.mapper.UserDynamoDBMapper;
import org.jetbrains.annotations.NotNull;

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
        return userDynamoDao.findOneByEmail(email)
                .map(UserDynamoDBMapper::fromMap);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userDynamoDao.findOneByUsername(username)
                .map(UserDynamoDBMapper::fromMap);
    }

    @Override
    public Optional<User> save(User user) {
        userDynamoDao.save(UserDynamoDBMapper.toMap(user));
        return Optional.of(user);
    }

    @Override
    public List<User> findAll() {
        return userDynamoDao.getAll().stream()
                .map(UserDynamoDBMapper::fromMap)
                .toList();
    }

    @Override
    public Optional<User> getById(String id) {
        return Optional.ofNullable(userDynamoDao.getById(id))
                .map(UserDynamoDBMapper::fromMap);
    }

    @Override
    public Optional<User> update(String id, @NotNull User user) {
        userDynamoDao.update(id, UserDynamoDBMapper.toMap(user));
        return Optional.of(user);
    }
}
