package com.example.demo.Proyecto.Security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.demo.Proyecto.Model.Usuario;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
    private final Key key;
    private final long expirationTime;
    private final long refreshExpirationTime;

    public JwtUtils(
        @Value("${jwt.secret}") String secret,
        @Value("${jwt.expiration}") long expirationTime,
        @Value("${jwt.refresh-expiration}") long refreshExpirationTime
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationTime = expirationTime;
        this.refreshExpirationTime = refreshExpirationTime;
    }

    //Crear el JWT al hacer login
    public String generarToken(Usuario usuario) {
        return Jwts.builder().setSubject(usuario.getCorreo()).claim("rol", usuario.getRol().name())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
        .signWith(key).compact();
    } 

    public String generarRefreshToken(Usuario usuario) {
        return Jwts.builder()
            .setSubject(usuario.getCorreo())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + refreshExpirationTime))
            .signWith(key)
            .compact();
    }

    //Extrae el usuario el token
    public String obtenerCorreo(String token) {
        return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
    }

    //Ve si el token es válido
    public boolean validarToken(String token) {
        try {
            Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Date obtenerExpiracion(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getExpiration();
    }
}