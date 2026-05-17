package com.example.demo.Proyecto.Auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.Proyecto.DTO.RestablecerPasswordRequest;
import com.example.demo.Proyecto.Enum.Rol;
import com.example.demo.Proyecto.Model.Usuario;
import com.example.demo.Proyecto.Security.JwtUtils;
import com.example.demo.Proyecto.Service.UsuarioService;
import com.example.demo.Proyecto.Service.EmailService;
import java.time.LocalDateTime;
import java.util.UUID;

import com.example.demo.Proyecto.Model.PasswordResetToken;
import com.example.demo.Proyecto.Repository.PasswordResetTokenRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final EmailService emailService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public AuthController(UsuarioService usuarioService, PasswordEncoder passwordEncoder, JwtUtils jwtUtils,
            EmailService emailService, PasswordResetTokenRepository passwordResetTokenRepository) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.emailService = emailService;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        Usuario usuario = usuarioService.buscarPorCorreo(request.getCorreo())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas");
        }

        String token = jwtUtils.generarToken(usuario);

        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<Usuario> register(@RequestBody Usuario u) {
        if (usuarioService.existePorCorreo(u.getCorreo())) {
            return ResponseEntity.status(409).body(null);
        }

        u.setRol(Rol.CLIENTE);
        return ResponseEntity.ok(usuarioService.guardarUsuario(u));
    }

    @PostMapping("/recuperar-password")
    public ResponseEntity<String> recuperarPassword(@RequestBody RecuperarPasswordRequest request) {
        usuarioService.buscarPorCorreo(request.getCorreo())
                .ifPresent(usuario -> {
                    passwordResetTokenRepository.findByUsuario(usuario).ifPresent(passwordResetTokenRepository::delete);

                    String token = UUID.randomUUID().toString();

                    PasswordResetToken resetToken = new PasswordResetToken(token, LocalDateTime.now().plusMinutes(30),
                            usuario);

                    passwordResetTokenRepository.save(resetToken);

                    String url = "http://localhost:4200/restablecer-password?token=" + token;

                    emailService.enviarCorreo(
                            usuario.getCorreo(),
                            "Restablecer contraseña Coviran Avenida Portugal",
                            "Pulse en este enlace para restablecer la contraseña:\n\n" + url
                                    + "\n\nSi usted no ha solicitado el restablecimiento de la contraseña, puede ignorar este mensaje con seguridad.");
                });

        return ResponseEntity.ok(
                "Si el correo esta registrado, se enviará un enlace de recuperación");
    }

    @PostMapping("/restablecer-password")
    public ResponseEntity<String> restablecerPassword(@RequestBody RestablecerPasswordRequest request) {
        PasswordResetToken resetToken = passwordResetTokenRepository
                .findByToken(request.getToken())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Token inválido"));

        if (resetToken.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            passwordResetTokenRepository.delete(resetToken);

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token expirado");
        }

        Usuario usuario = resetToken.getUsuario();
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuarioService.guardarUsuario(usuario);
        passwordResetTokenRepository.delete(resetToken);

        return ResponseEntity.ok("Contraseña actualizada correctamente");
    }
}