package com.ticketoffice.backend.infra.adapters.in.handlers;

import com.google.inject.Inject;
import com.ticketoffice.backend.application.services.PasswordEncoder;
import com.ticketoffice.backend.domain.enums.UserRole;
import com.ticketoffice.backend.domain.models.User;
import com.ticketoffice.backend.domain.ports.UserRepository;
import com.ticketoffice.backend.infra.adapters.in.dto.request.UserLoginRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.request.UserSignupRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.response.LoginResponse;
import com.ticketoffice.backend.infra.adapters.in.exception.BadRequestException;
import com.ticketoffice.backend.infra.adapters.out.security.JwtTokenProvider;

import java.util.List;
import java.util.UUID;

public class AuthenticationHandler {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Inject
    public AuthenticationHandler(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
                passwordEncoder.encode(input.password()),
                List.of(UserRole.USER, UserRole.SELLER),
                null
        );
        userRepository.save(user);

        return userRepository.findByUsername(input.username())
                .map(x -> JwtTokenProvider.generateToken(x.getUsername()))
                .map(x -> new LoginResponse(x, JwtTokenProvider.getExpirationTime()))
                .orElseThrow();
    }

    public LoginResponse authenticate(UserLoginRequest input) {
        return userRepository.findByUsername(input.username())
                .filter(x -> passwordEncoder.matches(input.password(), x.getPassword()))
                .map(x -> JwtTokenProvider.generateToken(x.getUsername()))
                .map(x -> new LoginResponse(x, JwtTokenProvider.getExpirationTime()))
                .orElseThrow();
    }
}
