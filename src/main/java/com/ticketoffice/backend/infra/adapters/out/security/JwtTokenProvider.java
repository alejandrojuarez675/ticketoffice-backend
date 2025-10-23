package com.ticketoffice.backend.infra.adapters.out.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import java.security.Key;
import java.util.Date;

public class JwtTokenProvider {
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(SecurityConstants.SECRET.getBytes());
    
    private JwtTokenProvider() {
        // Constructor privado para evitar instanciación
    }
    
    public static String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + SecurityConstants.EXPIRATION_TIME);
        
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(SECRET_KEY, SignatureAlgorithm.HS512)
            .compact();
    }
    
    public static long getExpirationTime() {
        return SecurityConstants.EXPIRATION_TIME;
    }
    
    public static String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(SECRET_KEY)
            .build()
            .parseClaimsJws(token)
            .getBody();
            
        return claims.getSubject();
    }
    
    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (SignatureException | MalformedJwtException ex) {
            // Firma inválida o token mal formado
            System.err.println("Token JWT inválido: " + ex.getMessage());
        } catch (ExpiredJwtException ex) {
            // Token expirado
            System.err.println("Token JWT expirado: " + ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            // Token no soportado
            System.err.println("Token JWT no soportado: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            // Token vacío
            System.err.println("El token está vacío");
        }
        return false;
    }
}
