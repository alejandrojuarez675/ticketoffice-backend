package com.ticketoffice.backend.infra.adapters.out.db.repository;

import com.ticketoffice.backend.domain.enums.UserRole;
import com.ticketoffice.backend.domain.models.User;
import com.ticketoffice.backend.domain.ports.UserRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class UserInMemoryRepository implements InMemoryRepository<User>, UserRepository {

    private static final Map<String, User> data = new HashMap<>();

    public UserInMemoryRepository(PasswordEncoder passwordEncoder) {
        User userAdmin = new User(
                "admin",
                "admin",
                "admin",
                passwordEncoder.encode("admin"),
                List.of(UserRole.ADMIN, UserRole.USER),
                null
        );
        this.save(userAdmin, "admin");
    }

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
        return save(user, user.getId());
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Optional<User> getById(String id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Optional<User> save(User obj, String id) {
        data.put(id, obj);
        return getById(id);
    }

    @Override
    public Optional<User> update(String id, User obj) {
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
