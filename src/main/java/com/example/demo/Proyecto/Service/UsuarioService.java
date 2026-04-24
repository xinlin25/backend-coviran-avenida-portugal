package com.example.demo.Proyecto.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.Proyecto.DTO.ActualizarPerfilDTO;
import com.example.demo.Proyecto.DTO.ActualizarUsuarioDTO;
import com.example.demo.Proyecto.Enum.Rol;
import com.example.demo.Proyecto.Model.Usuario;
import com.example.demo.Proyecto.Repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario guardarUsuario(Usuario u) {
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        return usuarioRepository.save(u);
    }

    public Optional<Usuario> buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    } 
    
    public Optional<Usuario> buscarPorTlf(String tlf) {
        return usuarioRepository.findByTlf(tlf);
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public boolean existePorCorreo(String correo) {
        return usuarioRepository.existsByCorreo(correo);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public List<Usuario> listarPorRol(Rol rol) {
        return usuarioRepository.findByRol(rol);
    }

    @Transactional
    public Usuario actualizarPerfil(String correo, ActualizarPerfilDTO datos) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (datos.nombreCompleto() != null) 
            usuario.setNombreCompleto(datos.nombreCompleto());

        if (datos.tlf() != null) 
            usuario.setTlf(datos.tlf());

        if (datos.direccion() != null) 
            usuario.setDireccion(datos.direccion());

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario actualizarUsuario(Long id, ActualizarUsuarioDTO u) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setNombreCompleto(u.nombreCompleto());
        usuario.setTlf(u.tlf());
        usuario.setDireccion(u.direccion());
        usuario.setRol(u.rol());
        usuario.setEnabled(u.enabled());

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario cambiarRol(Long id, Rol rol) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setRol(rol);
        return usuario;
    }

    @Transactional
    public void desactivarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setEnabled(false);
    }

    public void borrarPorId(Long id) {
        usuarioRepository.deleteById(id);
    }

    public List<Usuario> buscar(String query) {
        return usuarioRepository.buscar(query);
    }
}