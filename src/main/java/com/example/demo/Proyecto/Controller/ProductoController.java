package com.example.demo.Proyecto.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Proyecto.DTO.CrearProductoDTO;
import com.example.demo.Proyecto.Model.Producto;
import com.example.demo.Proyecto.Service.ProductoService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/productos")
public class ProductoController {
    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    public ResponseEntity<Producto> crearProducto(@RequestBody CrearProductoDTO datos) {

        if (productoService.existePorNombre(datos.nombre())) 
            return ResponseEntity.badRequest().build();

        Producto producto = new Producto();
        producto.setNombre(datos.nombre());
        producto.setPrecio(datos.precio());
        producto.setDescripcion(datos.descripcion());
        producto.setMarca(datos.marca());
        producto.setStock(datos.stock());

        producto.setCategoria(productoService.obtenerCategoria(datos.categoriaId()));

        return ResponseEntity.ok(productoService.guardarProducto(producto));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    public ResponseEntity<List<Producto>> listarTodos() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> buscarPorId(@PathVariable Long id) {
        Optional<Producto> producto = productoService.buscarPorId(id);

        return producto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    public ResponseEntity<Producto> buscarPorNombre(@RequestParam String nombre) {
        Optional<Producto> producto = productoService.buscarPorNombre(nombre);

        return producto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar/{nombre}")
    public ResponseEntity<List<Producto>> buscarPorNombreParcial(@PathVariable String nombre) {
        return ResponseEntity.ok(productoService.buscarPorNombreParcial(nombre));
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<Producto>> buscarPorCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(productoService.buscarPorCategoria(categoriaId));
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Producto>> listarActivos() {
        return ResponseEntity.ok(productoService.listarActivos());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> borrarProducto(@PathVariable Long id) {
        if (productoService.buscarPorId(id).isEmpty()) 
            return ResponseEntity.notFound().build();    
        
        productoService.borrarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
