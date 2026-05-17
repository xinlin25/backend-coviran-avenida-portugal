package com.example.demo.Proyecto.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Proyecto.Model.PasswordResetToken;
import com.example.demo.Proyecto.Model.Usuario;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);

    void deleteByToken(String token);

    Optional<PasswordResetToken> findByUsuario(Usuario usuario);
}