package com.example.demo.Proyecto.Auth;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.Proyecto.Model.Usuario;
import com.example.demo.Proyecto.Repository.UsuarioRepository;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UsuarioRepository usuarioRepository;

    @Value("${jwt.refresh-expiration}")
    private Long refreshTokenDurationMs;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,
                               UsuarioRepository usuarioRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // 🔹 Crear refresh token
    public RefreshToken crearRefreshToken(Long usuarioId) {
        RefreshToken refreshToken = new RefreshToken();

        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        refreshToken.setUsuario(usuario);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));

        return refreshTokenRepository.save(refreshToken);
    }

    // 🔹 Buscar por token
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    // 🔹 Verificar expiración
    public RefreshToken verificarExpiracion(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token expirado. Login requerido.");
        }
        return token;
    }

    // 🔹 Borrar por usuario (logout)
    public void deleteByUsuarioId(Long usuarioId) {
        refreshTokenRepository.deleteByUsuarioId(usuarioId);
    }
}