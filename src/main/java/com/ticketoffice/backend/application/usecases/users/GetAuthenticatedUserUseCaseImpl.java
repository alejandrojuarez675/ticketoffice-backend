package com.ticketoffice.backend.application.usecases.users;

import com.google.inject.Inject;
import com.ticketoffice.backend.domain.models.User;
import com.ticketoffice.backend.domain.ports.UserRepository;
import com.ticketoffice.backend.domain.usecases.users.GetAuthenticatedUserUseCase;
import com.ticketoffice.backend.infra.adapters.out.security.JwtTokenProvider;
import io.javalin.http.Context;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetAuthenticatedUserUseCaseImpl implements GetAuthenticatedUserUseCase {

    private static final Logger log = LoggerFactory.getLogger(GetAuthenticatedUserUseCaseImpl.class);
    private final UserRepository userRepository;

    @Inject
    public GetAuthenticatedUserUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> apply(Context context) {
        String authHeader  = context.header("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Optional.empty();
        }

        String token = authHeader.substring(7); // "Bearer ".length() == 7

        String usernameFromToken = null;
        try {
            usernameFromToken = JwtTokenProvider.getUsernameFromToken(token);
        } catch (Exception e) {
            log.error("Error al obtener el username del token", e);
        }

        return Optional.ofNullable(usernameFromToken)
                .flatMap(userRepository::findByUsername);
    }
}
