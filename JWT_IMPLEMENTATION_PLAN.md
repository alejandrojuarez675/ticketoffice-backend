# Plan de Implementación de Autenticación JWT con Javalin Nativo

## 1. Configuración Inicial

### 1.1 Dependencias
Verificar que se tengan las dependencias mínimas necesarias en `build.gradle` o `pom.xml`:
- Javalin (ya incluido)
- JSON (Jackson para serialización)
- JWT (usaremos implementación nativa de Java)

### 1.2 Estructura de Paquetes
```
com.ticketoffice.backend
├── infra
│   ├── adapters
│   │   ├── in
│   │   │   └── controller
│   │   │       └── auth
│   │   │           ├── AuthenticationController.java (existente)
│   │   │           └── SecurityConfig.java (nuevo)
│   │   └── out
│   │       └── security
│   │           ├── JwtTokenProvider.java (nuevo)
│   │           └── SecurityConstants.java (nuevo)
│   └── config
│       └── SecurityConfig.java (nuevo)
└── domain
    └── model
        └── security
            └── UserPrincipal.java (nuevo)
```

## 2. Implementación de Componentes de Seguridad

### 2.1 SecurityConstants.java
```java
package com.ticketoffice.backend.infra.adapters.out.security;

public class SecurityConstants {
    public static final String SECRET = "your-secret-key"; // Cambiar en producción
    public static final long EXPIRATION_TIME = 864_000_000; // 10 días
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}
```

### 2.2 JwtTokenProvider.java
```java
package com.ticketoffice.backend.infra.adapters.out.security;

import io.jsonwebtoken.*;
import java.util.Date;

public class JwtTokenProvider {
    
    public static String generateToken(String username) {
        return Jwts.builder()
            .setSubject(username)
            .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
            .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET)
            .compact();
    }

    public static String getUsernameFromToken(String token) {
        return Jwts.parser()
            .setSigningKey(SecurityConstants.SECRET)
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
```

### 2.3 UserPrincipal.java
```java
package com.ticketoffice.backend.domain.model.security;

import java.util.Collection;
import java.util.Collections;

public class UserPrincipal {
    private String username;
    private Collection<String> roles;

    public UserPrincipal(String username, Collection<String> roles) {
        this.username = username;
        this.roles = roles != null ? roles : Collections.emptyList();
    }

    // Getters y setters
}
```

## 3. Configuración de Seguridad

### 3.1 SecurityConfig.java
```java
package com.ticketoffice.backend.infra.config;

import com.ticketoffice.backend.infra.adapters.out.security.JwtTokenProvider;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class SecurityConfig {
    
    private static final String[] PUBLIC_ENDPOINTS = {
        "/auth/login",
        "/auth/signup",
        // Agregar más endpoints públicos según sea necesario
    };

    public static void configure(Javalin app) {
        // Configurar manejador de acceso
        app.before(validateRequest());
        
        // Configurar manejador de autenticación
        app.exception(Exception.class, (e, ctx) -> {
            if (e instanceof SecurityException) {
                ctx.status(401).result("Acceso no autorizado");
            }
        });
    }

    private static Handler validateRequest() {
        return ctx -> {
            String path = ctx.path();
            
            // Permitir acceso a endpoints públicos
            for (String endpoint : PUBLIC_ENDPOINTS) {
                if (path.startsWith(endpoint)) {
                    return;
                }
            }
            
            // Validar token JWT para rutas protegidas
            String token = ctx.header("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                throw new SecurityException("Token no proporcionado");
            }
            
            token = token.substring(7); // Eliminar "Bearer "
            if (!JwtTokenProvider.validateToken(token)) {
                throw new SecurityException("Token inválido o expirado");
            }
            
            // Agregar usuario al contexto si es necesario
            String username = JwtTokenProvider.getUsernameFromToken(token);
            ctx.attribute("username", username);
        };
    }
}
```

## 4. Actualización del Controlador de Autenticación

### 4.1 AuthenticationController.java (actualizado)
```java
// ... imports existentes ...
import com.ticketoffice.backend.infra.adapters.out.security.JwtTokenProvider;
import com.ticketoffice.backend.domain.model.security.UserPrincipal;

public class AuthenticationController implements CustomController {
    // ... código existente ...

    @Override
    public void registeredRoutes(Javalin app) {
        app.post(PATH + "/signup", ctx -> {
            var body = ctx.bodyAsClass(UserSignupRequest.class);
            ctx.json(register(body));
        });
        
        app.post(PATH + "/login", ctx -> {
            var body = ctx.bodyAsClass(UserLoginRequest.class);
            var response = authenticate(body);
            // Generar token JWT
            String token = JwtTokenProvider.generateToken(body.getUsername());
            response.setToken(token);
            ctx.json(response);
        });
        
        app.post(PATH + "/logout", ctx -> {
            // El cliente debe eliminar el token del almacenamiento local
            ctx.status(200).result("Sesión cerrada exitosamente");
        }, Role.ANYONE);
    }
    
    // ... resto del código existente ...
}
```

## 5. Integración con la Aplicación Principal

### 5.1 Main.java
```java
// ... imports existentes ...
import com.ticketoffice.backend.infra.config.SecurityConfig;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.enableCorsForAllOrigins();
            // Otras configuraciones...
        });
        
        // Configurar seguridad
        SecurityConfig.configure(app);
        
        // Inicializar controladores
        // ...
        
        app.start(8080);
    }
}
```

## 6. Pruebas

### 6.1 Prueba de Login
```http
POST /auth/login
Content-Type: application/json

{
    "username": "usuario",
    "password": "contraseña"
}
```

### 6.2 Acceso a Ruta Protegida
```http
GET /api/protected-route
Authorization: Bearer <token-jwt>
```

## 7. Consideraciones de Seguridad

1. **Almacenamiento de Contraseñas**: Asegúrate de usar BCrypt o similar para hashear las contraseñas.
2. **HTTPS**: Siempre usa HTTPS en producción.
3. **Refresh Tokens**: Considera implementar refresh tokens para una mejor experiencia de usuario.
4. **Seguridad de Headers**: Configura los headers de seguridad apropiados (CORS, CSP, etc.).
5. **Rate Limiting**: Implementa límites de tasa para prevenir ataques de fuerza bruta.
6. **Rotación de Claves**: Considera implementar rotación de claves JWT en producción.

## 8. Siguientes Pasos

1. Implementar el almacenamiento seguro de tokens en el cliente
2. Agregar manejo de roles y permisos
3. Implementar refresh tokens
4. Configurar logs de seguridad
5. Realizar pruebas de penetración
