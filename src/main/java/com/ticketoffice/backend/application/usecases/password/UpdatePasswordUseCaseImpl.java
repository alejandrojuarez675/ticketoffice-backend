package com.ticketoffice.backend.application.usecases.password;

import com.ticketoffice.backend.application.services.PasswordEncoder;
import com.ticketoffice.backend.domain.models.User;
import com.ticketoffice.backend.domain.ports.UserRepository;
import com.ticketoffice.backend.domain.usecases.password.UpdatePasswordUseCase;
import jakarta.inject.Inject;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public class UpdatePasswordUseCaseImpl implements UpdatePasswordUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Inject
    public UpdatePasswordUseCaseImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> apply(String username, String password) {
        return userRepository.findByUsername(username)
                .map(user -> mapUserWithNewPassword(password, user))
                .flatMap(user -> userRepository.update(user.getId(), user));
    }

    @NotNull
    private User mapUserWithNewPassword(String password, User user) {
        return new User(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                passwordEncoder.encode(password),
                user.getRole(),
                user.getOrganizer().orElse(null)
        );
    }
}
