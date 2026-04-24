package com.example.demo.Proyecto.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Proyecto.DTO.ActualizarPerfilDTO;
import com.example.demo.Proyecto.DTO.ActualizarUsuarioDTO;
import com.example.demo.Proyecto.DTO.CrearUsuarioDTO;
import com.example.demo.Proyecto.DTO.UsuarioPerfilDTO;
import com.example.demo.Proyecto.Enum.Rol;
import com.example.demo.Proyecto.Model.Usuario;
import com.example.demo.Proyecto.Service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/mi-perfil")
    @PreAuthorize("hasAnyRole('CLIENTE','EMPLEADO','ADMIN')")
    public ResponseEntity<UsuarioPerfilDTO> miPerfil(Authentication auth) {
        Usuario u = usuarioService.buscarPorCorreo(auth.getName())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return ResponseEntity.ok(new UsuarioPerfilDTO(
            u.getId(),
            u.getNombreCompleto(),
            u.getCorreo(),
            u.getTlf(),
            u.getDireccion(),
            u.getRol()
        ));
    }

    @PutMapping("/mi-perfil")
    @PreAuthorize("hasAnyRole('CLIENTE','EMPLEADO','ADMIN')")
    public ResponseEntity<Usuario> actualizarPerfil(
            Authentication auth,
            @RequestBody ActualizarPerfilDTO datos) {

        return ResponseEntity.ok(
            usuarioService.actualizarPerfil(auth.getName(), datos)
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Usuario> actualizarUsuario(
        @PathVariable Long id,
        @RequestBody ActualizarUsuarioDTO actualizado
    ) {
        Usuario usuario = usuarioService.buscarPorId(id)
            .orElseThrow();

        usuario.setNombreCompleto(actualizado.nombreCompleto());
        usuario.setTlf(actualizado.tlf());
        usuario.setDireccion(actualizado.direccion());
        usuario.setRol(actualizado.rol());
        usuario.setEnabled(actualizado.enabled());

        return ResponseEntity.ok(usuarioService.actualizarUsuario(id, actualizado));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    public ResponseEntity<Usuario> crearUsuario(@RequestBody CrearUsuarioDTO datos) {

        Usuario usuario = new Usuario();
        usuario.setNombreCompleto(datos.nombreCompleto());
        usuario.setCorreo(datos.correo());
        usuario.setPassword(datos.password());
        usuario.setTlf(datos.tlf());
        usuario.setDireccion(datos.direccion());
        usuario.setRol(datos.rol());
        usuario.setEnabled(datos.enabled());

        return ResponseEntity.ok(usuarioService.guardarUsuario(usuario));
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/clientes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Usuario>> listarClientes() {
        return ResponseEntity.ok(usuarioService.listarPorRol(Rol.CLIENTE));
    }

    @GetMapping("/correo/{correo}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    public ResponseEntity<Usuario> buscarPorCorreo(@PathVariable String correo) {
        Optional<Usuario> usuario = usuarioService.buscarPorCorreo(correo);

        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/tlf/{tlf}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    public ResponseEntity<Usuario> buscarPorTlf(@PathVariable String tlf) {
        Optional<Usuario> usuario = usuarioService.buscarPorTlf(tlf);

        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    } 

    @PutMapping("/{id}/desactivar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> desactivarUsuario(@PathVariable Long id) {
        usuarioService.desactivarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    public ResponseEntity<List<Usuario>> buscar(@RequestParam String query) {
        return ResponseEntity.ok(usuarioService.buscar(query));
    }
}
