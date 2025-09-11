package com.ticketoffice.backend.infra.config;

import com.ticketoffice.backend.infra.adapters.out.security.JwtTokenProvider;
import com.ticketoffice.backend.infra.adapters.out.security.SecurityConstants;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import io.javalin.security.RouteRole;
import java.util.Map;

public enum SecurityConfig implements RouteRole {
    ANYONE,
    AUTHENTICATED;

    public static void configure(Javalin app) {
        // Configurar manejador de acceso
        app.before(validateRequest());
        
        // Configurar manejador de errores de autenticación
        app.exception(SecurityException.class, (e, ctx) -> {
            ctx.status(401).json(Map.of(
                "status", 401,
                "message", "Acceso no autorizado: " + e.getMessage()
            ));
        });
    }

    private static Handler validateRequest() {
        return ctx -> {
            // Obtener la ruta de la solicitud
            String path = ctx.path();
            
            // Verificar si la ruta es pública
            if (isPublicPath(path)) {
                return;
            }
            
            // Validar token JWT para rutas protegidas
            String authHeader = ctx.header(SecurityConstants.HEADER_STRING);
            
            if (authHeader == null || !authHeader.startsWith(SecurityConstants.TOKEN_PREFIX)) {
                throw new SecurityException("Token no proporcionado o formato inválido");
            }
            
            String token = authHeader.replace(SecurityConstants.TOKEN_PREFIX, "");
            
            if (!JwtTokenProvider.validateToken(token)) {
                throw new SecurityException("Token inválido o expirado");
            }
            
            // Agregar el nombre de usuario al contexto para uso posterior
            String username = JwtTokenProvider.getUsernameFromToken(token);
            ctx.attribute("username", username);
        };
    }
    
    private static boolean isPublicPath(String path) {
        for (String publicPath : SecurityConstants.PUBLIC_ENDPOINTS) {
            if (path.startsWith(publicPath)) {
                return true;
            }
        }
        return false;
    }
    
}
