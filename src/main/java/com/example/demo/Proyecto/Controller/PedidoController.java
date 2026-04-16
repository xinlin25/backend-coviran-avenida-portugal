package com.example.demo.Proyecto.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Proyecto.Enum.Estado;
import com.example.demo.Proyecto.Enum.Rol;
import com.example.demo.Proyecto.Model.Pedido;
import com.example.demo.Proyecto.Model.Usuario;
import com.example.demo.Proyecto.Service.PedidoService;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<Pedido> crearPedido(@RequestBody Pedido pedido) {
        Pedido p = pedidoService.crearPedido(pedido);

        return ResponseEntity.ok(p);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    public ResponseEntity<List<Pedido>> listarPedidos() {
        return ResponseEntity.ok(pedidoService.listarTodos());
    }
    
    @GetMapping("/mis-pedidos")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<Pedido>> listarPorCliente(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(pedidoService.listarPorCliente(usuario.getId()));
    }

    @GetMapping("/empleado/{empleadoId}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    public ResponseEntity<List<Pedido>> listarPorEmpleado(@PathVariable Long empleadoId) {
        return ResponseEntity.ok(pedidoService.listarPorEmpleado(empleadoId));
    }

    @GetMapping("/estado")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    public ResponseEntity<List<Pedido>> listarPorEstado(@RequestParam Estado estado) {
        return ResponseEntity.ok(pedidoService.buscarPorEstado(estado));
    }

    @PutMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    public ResponseEntity<Pedido> cambiarEstado(@PathVariable Long id, @RequestParam Estado estado) {
        Pedido pedido = pedidoService.buscarPorId(id)
        .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        if (pedido.getEstado() == Estado.COMPLETADO) 
            throw new IllegalStateException("Un pedido completado no puede cambiar de estado");


        Pedido actualizado = pedidoService.cambiarEstado(id, estado);

        return ResponseEntity.ok(actualizado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> verPedido(@PathVariable Long id, @AuthenticationPrincipal Usuario usuario) {
        Pedido pedido = pedidoService.buscarPorId(id).orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        if (usuario.getRol() == Rol.CLIENTE &&
            !pedido.getCliente().getId().equals(usuario.getId())) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(pedido);
    }
}