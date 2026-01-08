package com.ticketoffice.backend.application.usecases.auth;

import com.ticketoffice.backend.domain.models.PasswordResetToken;
import com.ticketoffice.backend.domain.models.User;
import com.ticketoffice.backend.domain.ports.PasswordResetTokenRepository;
import com.ticketoffice.backend.domain.ports.UserRepository;
import com.ticketoffice.backend.domain.usecases.auth.ConfirmUserAccountUseCase;
import jakarta.inject.Inject;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public class ConfirmUserAccountUseCaseImpl implements ConfirmUserAccountUseCase {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;

    @Inject
    public ConfirmUserAccountUseCaseImpl(
            PasswordResetTokenRepository passwordResetTokenRepository,
            UserRepository userRepository
    ) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> apply(String token) {
        return passwordResetTokenRepository.findByHashToken(token)
                .filter(PasswordResetToken::isValidToken)
                .flatMap(x -> {
                    Optional<User> userOptional = markUserAsConfirmed(x);
                    markPasswordTokenAsUsed(x);
                    return userOptional;
                });
    }

    @NotNull
    private Optional<User> markUserAsConfirmed(PasswordResetToken x) {
        return userRepository.findByUsername(x.username())
                .map(u -> {
                    u.setConfirmed(true);
                    return u;
                })
                .flatMap(userRepository::save);
    }

    private void markPasswordTokenAsUsed(PasswordResetToken x) {
        PasswordResetToken passToken = new PasswordResetToken(x.id(), x.username(), x.email(), x.tokenHash(), x.expiresAt(), true);
        passwordResetTokenRepository.save(passToken);
    }
}
