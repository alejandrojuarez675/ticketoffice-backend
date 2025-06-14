package com.ticketoffice.backend.infra.adapters.in.controller.auth;

import com.ticketoffice.backend.infra.adapters.in.dto.request.UserLoginRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.request.UserSignupRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.response.LoginResponse;
import com.ticketoffice.backend.infra.adapters.in.exception.BadRequestException;
import com.ticketoffice.backend.infra.adapters.in.handlers.AuthenticationHandler;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {

    private final AuthenticationHandler authenticationHandler;

    public AuthenticationController(AuthenticationHandler authenticationHandler) {
        this.authenticationHandler = authenticationHandler;
    }

    @Operation(
            summary = "User Signup",
            description = "Endpoint to register a new user",
            tags = {"Authentication"}
    )
    @PostMapping("/signup")
    public ResponseEntity<LoginResponse> register(@RequestBody UserSignupRequest registerUserDto) throws BadRequestException {
        LoginResponse registeredUser = authenticationHandler.signup(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    @Operation(
            summary = "User Login",
            description = "Endpoint to authenticate a user",
            tags = {"Authentication"}
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody UserLoginRequest loginUserDto) {
        LoginResponse authenticatedUser = authenticationHandler.authenticate(loginUserDto);
        return ResponseEntity.ok(authenticatedUser);
    }
}
