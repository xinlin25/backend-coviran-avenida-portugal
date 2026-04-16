package com.example.demo.Proyecto.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Proyecto.DTO.AgregarProductoCarritoDTO;
import com.example.demo.Proyecto.Model.Carrito;
import com.example.demo.Proyecto.Model.Pedido;
import com.example.demo.Proyecto.Service.CarritoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/carrito")
public class CarritoController {
    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @GetMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<Carrito> verCarrito(Authentication auth) {
        return ResponseEntity.ok(carritoService.obtenerCarritoActivo(auth.getName()));
    }

    @PostMapping("/agregar")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<Carrito> agregarProducto(
            Authentication auth,
            @Valid @RequestBody AgregarProductoCarritoDTO dto
    ) {
        Carrito carrito = carritoService.añadirProducto(
                auth.getName(),      
                dto.getProductoId(),
                dto.getCantidad()
        );

        return ResponseEntity.ok(carrito);
    }

    @PostMapping("/confirmar")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<Pedido> confirmarPedido(Authentication auth) {
        Pedido pedido = carritoService.confirmarCarrito(auth.getName());

        return ResponseEntity.ok(pedido);
    }
}