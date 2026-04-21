package com.example.demo.Proyecto.Auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.Proyecto.Enum.Rol;
import com.example.demo.Proyecto.Model.Usuario;
import com.example.demo.Proyecto.Security.JwtUtils;
import com.example.demo.Proyecto.Service.UsuarioService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;

    public AuthController(UsuarioService usuarioService, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, RefreshTokenService refreshTokenService) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        Usuario usuario = usuarioService.buscarPorCorreo(request.getCorreo())
                .orElseThrow(() -> 
                    new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas")
                );

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas");
        }

        //Generar access token (JWT)
        String accessToken = jwtUtils.generarToken(usuario);

        //Borrar refresh token anterior (si existe)
        refreshTokenService.deleteByUsuarioId(usuario.getId());

        //Crear nuevo refresh token
        RefreshToken refreshToken = refreshTokenService.crearRefreshToken(usuario.getId());

        //Devolver ambos
        return ResponseEntity.ok(
            new LoginResponse(accessToken, refreshToken.getToken())
        );
    }

    @PostMapping("/register")
    public ResponseEntity<Usuario> register(@RequestBody Usuario u) {
        if (usuarioService.existePorCorreo(u.getCorreo())) {
            return ResponseEntity.status(409).body(null);
        }

        u.setRol(Rol.CLIENTE);
        return ResponseEntity.ok(usuarioService.guardarUsuario(u));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        String requestToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestToken)
            .map(refreshTokenService::verificarExpiracion)
            .map(rt -> {
                Usuario usuario = rt.getUsuario();

                // generar nuevo access token
                String newAccessToken = jwtUtils.generarToken(usuario);

                return ResponseEntity.ok(
                    new LoginResponse(newAccessToken, requestToken)
                );
            })
            .orElseThrow(() -> 
                new RuntimeException("Refresh token no válido")
            );
    }
}