package com.ticketoffice.backend.infra.adapters.in.handlers;

import com.ticketoffice.backend.domain.enums.UserRole;
import com.ticketoffice.backend.domain.models.User;
import com.ticketoffice.backend.domain.ports.UserRepository;
import com.ticketoffice.backend.infra.adapters.in.dto.request.UserLoginRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.request.UserSignupRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.response.LoginResponse;
import com.ticketoffice.backend.infra.adapters.in.exception.BadRequestException;
import com.ticketoffice.backend.infra.auth.JwtService;
import java.util.List;
import java.util.UUID;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationHandler {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthenticationHandler(
             UserRepository userRepository,
             PasswordEncoder passwordEncoder,
             AuthenticationManager authenticationManager,
             JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
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
                List.of(UserRole.USER)
        );

        return userRepository.save(user)
                .map(jwtService::generateToken)
                .map(x -> new LoginResponse(x, jwtService.getExpirationTime()))
                .orElseThrow();
    }

    public LoginResponse authenticate(UserLoginRequest input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.username(),
                        input.password()
                )
        );

        return userRepository.findByUsername(input.username())
                .map(jwtService::generateToken)
                .map(x -> new LoginResponse(x, jwtService.getExpirationTime()))
                .orElseThrow();
    }
}
