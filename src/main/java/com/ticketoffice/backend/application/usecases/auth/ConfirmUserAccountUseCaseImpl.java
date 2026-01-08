package com.ticketoffice.backend.application.usecases.auth;

import com.ticketoffice.backend.domain.models.UserToken;
import com.ticketoffice.backend.domain.models.User;
import com.ticketoffice.backend.domain.ports.UserTokenRepository;
import com.ticketoffice.backend.domain.ports.UserRepository;
import com.ticketoffice.backend.domain.usecases.auth.ConfirmUserAccountUseCase;
import jakarta.inject.Inject;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public class ConfirmUserAccountUseCaseImpl implements ConfirmUserAccountUseCase {

    private final UserTokenRepository userTokenRepository;
    private final UserRepository userRepository;

    @Inject
    public ConfirmUserAccountUseCaseImpl(
            UserTokenRepository userTokenRepository,
            UserRepository userRepository
    ) {
        this.userTokenRepository = userTokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> apply(String token) {
        return userTokenRepository.findByHashToken(token)
                .filter(UserToken::isValidToken)
                .flatMap(x -> {
                    Optional<User> userOptional = markUserAsConfirmed(x);
                    markPasswordTokenAsUsed(x);
                    return userOptional;
                });
    }

    @NotNull
    private Optional<User> markUserAsConfirmed(UserToken x) {
        return userRepository.findByUsername(x.username())
                .map(u -> {
                    u.setConfirmed(true);
                    return u;
                })
                .flatMap(userRepository::save);
    }

    private void markPasswordTokenAsUsed(UserToken x) {
        UserToken passUserToken = new UserToken(x.id(), x.username(), x.email(), x.tokenHash(), x.expiresAt(), true);
        userTokenRepository.save(passUserToken);
    }
}
