package com.ticketoffice.backend.infra.adapters.in.controller.auth;

import com.google.inject.Inject;
import com.ticketoffice.backend.infra.adapters.in.controller.CustomController;
import com.ticketoffice.backend.infra.adapters.in.dto.request.UserLoginRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.request.UserSignupRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.response.LoginResponse;
import com.ticketoffice.backend.infra.adapters.in.exception.BadRequestException;
import com.ticketoffice.backend.infra.adapters.in.handlers.AuthenticationHandler;
import com.ticketoffice.backend.infra.adapters.out.security.JwtTokenProvider;
import com.ticketoffice.backend.infra.config.SecurityConfig;
import io.javalin.Javalin;
import java.util.Map;

public class AuthenticationController implements CustomController {

    private final static String PATH = "/auth";
    private final AuthenticationHandler authenticationHandler;

    @Inject
    public AuthenticationController(AuthenticationHandler authenticationHandler) {
        this.authenticationHandler = authenticationHandler;
    }

    @Override
    public void registeredRoutes(Javalin app) {
        // Ruta pública para registro de usuarios
        app.post(PATH + "/signup", ctx -> {
            var body = ctx.bodyAsClass(UserSignupRequest.class);
            ctx.json(register(body));
        }, SecurityConfig.ANYONE);
        
        // Ruta pública para inicio de sesión
        app.post(PATH + "/login", ctx -> {
            var body = ctx.bodyAsClass(UserLoginRequest.class);
            // Autenticar al usuario
            try {
                authenticate(body);
            } catch (Exception e) {
                ctx.status(401);
                ctx.json(Map.of("message", "Invalid credentials"));
                return;
            }

            // Generar token JWT
            String token = JwtTokenProvider.generateToken(body.username());
            
            // Crear la respuesta con el token y tiempo de expiración
            var loginResponse = new LoginResponse(
                token,
                JwtTokenProvider.getExpirationTime()
            );
            
            ctx.json(loginResponse);
        }, SecurityConfig.ANYONE);
        
        // Ruta de cierre de sesión (el cliente debe eliminar el token)
        app.post(PATH + "/logout", ctx -> {
            // El cliente debe eliminar el token del almacenamiento local
            ctx.json(Map.of("message", "Sesión cerrada exitosamente"));
            ctx.status(200); // Agregar el código de estado HTTP
        }, SecurityConfig.AUTHENTICATED);
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
        // Primero autenticamos al usuario
        var response = authenticationHandler.authenticate(loginUserDto);
        
        // Si llegamos aquí, la autenticación fue exitosa
        return response;
    }
}
