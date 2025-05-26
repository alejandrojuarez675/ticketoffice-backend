package com.ticketoffice.backend.domain.ports;

import com.ticketoffice.backend.domain.models.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> save(User user);
    List<User> findAll();
    Optional<User> getById(String id);
    Optional<User> update(String id, User user);
}
