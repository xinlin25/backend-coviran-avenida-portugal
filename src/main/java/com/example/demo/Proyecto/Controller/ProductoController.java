package com.example.demo.Proyecto.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Proyecto.DTO.ActualizarProductoDTO;
import com.example.demo.Proyecto.DTO.CrearProductoDTO;
import com.example.demo.Proyecto.Model.Categoria;
import com.example.demo.Proyecto.Model.Producto;
import com.example.demo.Proyecto.Service.CategoriaService;
import com.example.demo.Proyecto.Service.ProductoService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/productos")
public class ProductoController {
    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    public ProductoController(ProductoService productoService, CategoriaService categoriaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
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

        producto.setCategoria(categoriaService.buscarPorId(datos.categoriaId()).orElseThrow(() -> new RuntimeException("Categoría no encontrada")));

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

    @GetMapping("/buscar/{nombre}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    public ResponseEntity<List<Producto>> buscarPorNombreParcial(@PathVariable String nombre) {
        return ResponseEntity.ok(productoService.buscarPorNombreParcial(nombre));
    }

    @GetMapping("/categoria/{categoriaId}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    public ResponseEntity<List<Producto>> buscarPorCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(productoService.buscarPorCategoria(categoriaId));
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    public ResponseEntity<List<Producto>> buscar(@RequestParam String query) {
        return ResponseEntity.ok(productoService.buscar(query));
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

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    public ResponseEntity<Producto> actualizarProducto(
        @PathVariable Long id,
        @RequestBody ActualizarProductoDTO datos
    ) {
        Producto producto = productoService.buscarPorId(id).orElseThrow();

        producto.setNombre(datos.nombre());
        producto.setDescripcion(datos.descripcion());
        producto.setPrecio(datos.precio());
        producto.setMarca(datos.marca());
        producto.setStock(datos.stock());

        if (datos.categoriaId() != null) {
            Categoria categoria = categoriaService.buscarPorId(datos.categoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

            producto.setCategoria(categoria);
        }

        return ResponseEntity.ok(productoService.guardarProducto(producto));
    }
}
