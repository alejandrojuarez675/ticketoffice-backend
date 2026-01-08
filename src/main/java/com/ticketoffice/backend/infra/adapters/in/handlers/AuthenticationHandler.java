package com.ticketoffice.backend.infra.adapters.in.handlers;

import com.google.inject.Inject;
import com.ticketoffice.backend.application.services.PasswordEncoder;
import com.ticketoffice.backend.domain.enums.UserRole;
import com.ticketoffice.backend.domain.models.User;
import com.ticketoffice.backend.domain.ports.UserRepository;
import com.ticketoffice.backend.domain.usecases.auth.ConfirmUserAccountUseCase;
import com.ticketoffice.backend.domain.usecases.emails.SendConfirmAccountEmail;
import com.ticketoffice.backend.infra.adapters.in.dto.request.ConfirmAccountRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.request.UserLoginRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.request.UserSignupRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.response.LoginResponse;
import com.ticketoffice.backend.infra.adapters.in.exception.BadRequestException;
import com.ticketoffice.backend.infra.adapters.in.exception.NotFoundException;
import com.ticketoffice.backend.infra.adapters.out.security.JwtTokenProvider;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthenticationHandler {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SendConfirmAccountEmail sendConfirmAccountEmail;
    private final ConfirmUserAccountUseCase confirmUserAccountUseCase;

    @Inject
    public AuthenticationHandler(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            SendConfirmAccountEmail sendConfirmAccountEmail,
            ConfirmUserAccountUseCase confirmUserAccountUseCase
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.sendConfirmAccountEmail = sendConfirmAccountEmail;
        this.confirmUserAccountUseCase = confirmUserAccountUseCase;
    }

    public void signup(UserSignupRequest input) throws BadRequestException {

        Optional<User> userOnDB = userRepository.findByUsername(input.username());
        if (userOnDB.isPresent()) {
            throw new BadRequestException("Username already exists");
        }

        Optional<User> userByEmail = userRepository.findByEmail(input.email());
        if (userByEmail.isPresent()) {
            throw new BadRequestException("Email already exists");
        }

        User user = new User(
                UUID.randomUUID().toString(),
                input.username(),
                input.email(),
                passwordEncoder.encode(input.password()),
                List.of(UserRole.USER, UserRole.SELLER),
                null
        );

        User saved = userRepository.save(user)
                .orElseThrow(() -> new RuntimeException("User not saved"));

        sendConfirmAccountEmail.accept(saved);
    }

    public LoginResponse authenticate(UserLoginRequest input) {
        return userRepository.findByUsername(input.username())
                .filter(x -> passwordEncoder.matches(input.password(), x.getPassword()))
                .map(x -> JwtTokenProvider.generateToken(x.getUsername()))
                .map(x -> new LoginResponse(x, JwtTokenProvider.getExpirationTime()))
                .orElseThrow();
    }

    public void confirmAccount(ConfirmAccountRequest body) throws NotFoundException {
        confirmUserAccountUseCase.apply(body.token())
                .orElseThrow(() -> new NotFoundException("token is invalid"));
    }
}
