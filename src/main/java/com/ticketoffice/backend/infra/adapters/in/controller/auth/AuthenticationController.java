package com.ticketoffice.backend.infra.adapters.in.controller.auth;

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
            ctx.json(authenticate(body));
        });
    }

//    @Operation(
//            summary = "User Signup",
//            description = "Endpoint to register a new user",
//            tags = {"Authentication"}
//    )
    public LoginResponse register(UserSignupRequest registerUserDto) throws BadRequestException {
        return authenticationHandler.signup(registerUserDto);
    }

//    @Operation(
//            summary = "User Login",
//            description = "Endpoint to authenticate a user",
//            tags = {"Authentication"}
//    )
    public LoginResponse authenticate(UserLoginRequest loginUserDto) {
        return authenticationHandler.authenticate(loginUserDto);
    }
}
