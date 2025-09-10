package com.ticketoffice.backend.infra.adapters.in.handlers;

import com.google.inject.Inject;
import com.ticketoffice.backend.domain.enums.UserRole;
import com.ticketoffice.backend.domain.models.User;
import com.ticketoffice.backend.domain.ports.UserRepository;
import com.ticketoffice.backend.infra.adapters.in.dto.request.UserLoginRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.request.UserSignupRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.response.LoginResponse;
import com.ticketoffice.backend.infra.adapters.in.exception.BadRequestException;
import java.util.List;
import java.util.UUID;

public class AuthenticationHandler {

    private final UserRepository userRepository;

    @Inject
    public AuthenticationHandler(
             UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    public LoginResponse signup(UserSignupRequest input) throws BadRequestException {

        User userOnDB = userRepository.findByUsername(input.username()).orElse(null);
        if (userOnDB != null) {
            throw new BadRequestException("Username already exists");
        }

        User user = new User(
                UUID.randomUUID().toString(),
                input.username(),
                input.email(),
        "",
//                passwordEncoder.encode(input.password()),
                List.of(UserRole.USER, UserRole.SELLER),
                null
        );

        return userRepository.findByUsername(input.username())
//                .map(jwtService::generateToken)
                .map(x -> "TOKEN")
//                .map(x -> new LoginResponse(x, jwtService.getExpirationTime()))
                .map(x -> new LoginResponse(x, 123234))
                .orElseThrow();
    }

    public LoginResponse authenticate(UserLoginRequest input) {
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        input.username(),
//                        input.password()
//                )
//        );

        return userRepository.findByUsername(input.username())
//                .map(jwtService::generateToken)
                .map(x -> "TOKEN")
//                .map(x -> new LoginResponse(x, jwtService.getExpirationTime()))
                .map(x -> new LoginResponse(x, 123234))
                .orElseThrow();
    }
}
