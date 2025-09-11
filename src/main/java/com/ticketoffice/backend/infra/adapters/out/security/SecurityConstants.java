package com.ticketoffice.backend.infra.adapters.out.security;

public class SecurityConstants {
    // Cambiar esta clave en producción por una clave segura y mantenerla en un lugar seguro
    // La clave debe tener al menos 256 bits (32 caracteres) para HS512
    public static final String SECRET = "r7YKgmLOrq6hzP89fxTyw2O/VejRbFQMTeNdHpYogEWqXFSUcIFzzPUSdjHhF3b83mGi4AT6hNk0JE+Te+6j2Q==";
    // 10 días en milisegundos
    public static final long EXPIRATION_TIME = 10 * 24 * 60 * 60 * 1000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    
    // Endpoints públicos que no requieren autenticación
    public static final String[] PUBLIC_ENDPOINTS = {
        "/auth/login",
        "/auth/signup",
        "/swagger",
        "/swagger-docs",
        "/swagger-ui",
        "/api/public"
    };
}
