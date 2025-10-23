package com.ticketoffice.backend.infra.adapters.in.controller.auth;

import com.google.inject.Inject;
import com.ticketoffice.backend.infra.adapters.in.controller.CustomController;
import com.ticketoffice.backend.infra.adapters.in.dto.request.UserLoginRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.request.UserSignupRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.response.LoginResponse;
import com.ticketoffice.backend.infra.adapters.in.exception.BadRequestException;
import com.ticketoffice.backend.infra.adapters.in.handlers.AuthenticationHandler;
import io.javalin.Javalin;

public class AuthenticationController implements CustomController {

    private final static String PATH = "/auth";
    private final AuthenticationHandler authenticationHandler;

    @Inject
    public AuthenticationController(AuthenticationHandler authenticationHandler) {
        this.authenticationHandler = authenticationHandler;
    }

    @Override
    public void registeredRoutes(Javalin app) {
        app.post(PATH + "/signup", ctx -> {
            var body = ctx.bodyAsClass(UserSignupRequest.class);
            ctx.json(register(body));
        });
        app.post(PATH + "/login", ctx -> {
            var body = ctx.bodyAsClass(UserLoginRequest.class);
            try {
                ctx.json(authenticate(body));
            } catch (Exception e) {
                ctx.status(401);
                ctx.json("Invalid credentials");
            }
        });
    }


    public LoginResponse register(UserSignupRequest registerUserDto) throws BadRequestException {
        return authenticationHandler.signup(registerUserDto);
    }


    public LoginResponse authenticate(UserLoginRequest loginUserDto) {
        return authenticationHandler.authenticate(loginUserDto);
    }
}
