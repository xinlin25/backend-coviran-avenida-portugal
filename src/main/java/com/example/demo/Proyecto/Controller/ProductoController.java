package com.example.demo.Proyecto.Controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Proyecto.Model.Categoria;
import com.example.demo.Proyecto.Model.Producto;
import com.example.demo.Proyecto.Service.CategoriaService;
import com.example.demo.Proyecto.Service.CloudinaryService;
import com.example.demo.Proyecto.Service.ProductoService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/productos")
public class ProductoController {
    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final CloudinaryService cloudinaryService;

    public ProductoController(ProductoService productoService, CategoriaService categoriaService, CloudinaryService cloudinaryService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    public ResponseEntity<Producto> crearProducto(
        @RequestParam String nombre,
        @RequestParam BigDecimal precio,
        @RequestParam String descripcion,
        @RequestParam String marca,
        @RequestParam int stock,
        @RequestParam Long categoriaId,
        @RequestParam(required = false) Boolean activo,
        @RequestParam(required = false) MultipartFile imagen,
        @RequestParam(required = false) Boolean enOferta,
        @RequestParam(required = false) BigDecimal precioOferta,
        @RequestParam(required = false) Boolean destacado
    ) {
        if (productoService.existePorNombre(nombre)) return ResponseEntity.badRequest().build();

        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setPrecio(precio);
        producto.setDescripcion(descripcion);
        producto.setMarca(marca);
        producto.setStock(stock);
        producto.setEnOferta(enOferta != null ? enOferta : false);
        producto.setPrecioOferta(precioOferta);
        producto.setDestacado(destacado != null ? destacado : false);
        producto.setCategoria(
            categoriaService.buscarPorId(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"))
        );
        producto.setActivo(activo != null ? activo : true);

        try {
            if (imagen != null && !imagen.isEmpty()) {
                String imageUrl = cloudinaryService.subirImagen(imagen);
                producto.setImagenUrl(imageUrl);
            }

            return ResponseEntity.ok(productoService.guardarProducto(producto));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Producto>> listarTodos() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    @GetMapping("/ofertas")
    public ResponseEntity<List<Producto>> listarOfertas() {
        return ResponseEntity.ok(productoService.listarOfertas());
    }

    @GetMapping("/destacados")
    public ResponseEntity<List<Producto>> listarDestacados() {
        return ResponseEntity.ok(productoService.listarDestacados());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> buscarPorId(@PathVariable Long id) {
        Optional<Producto> producto = productoService.buscarPorId(id);

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

    @GetMapping("/buscar")
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
        @RequestParam String nombre,
        @RequestParam String descripcion,
        @RequestParam BigDecimal precio,
        @RequestParam String marca,
        @RequestParam Integer stock,
        @RequestParam Long categoriaId,
        @RequestParam(required = false) Boolean activo,
        @RequestParam(required = false) Boolean enOferta,
        @RequestParam(required = false) BigDecimal precioOferta,
        @RequestParam(required = false) Boolean destacado,
        @RequestParam(required = false) MultipartFile imagen
    ) {
        try {
            Producto producto = productoService.buscarPorId(id).orElseThrow();
            producto.setNombre(nombre);
            producto.setDescripcion(descripcion);
            producto.setPrecio(precio);
            producto.setMarca(marca);
            producto.setStock(stock);
            producto.setActivo(activo != null ? activo : true);
            producto.setEnOferta(enOferta != null ? enOferta : false);
            producto.setPrecioOferta(precioOferta);
            producto.setDestacado(destacado != null ? destacado : false);
            Categoria categoria = categoriaService.buscarPorId(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            producto.setCategoria(categoria);

            if (imagen != null && !imagen.isEmpty()) {
                String imageUrl = cloudinaryService.subirImagen(imagen);
                producto.setImagenUrl(imageUrl);
            }

            return ResponseEntity.ok(productoService.guardarProducto(producto));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}/desactivar")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> desactivarProducto(@PathVariable Long id) {
        productoService.desactivarProducto(id);
        return ResponseEntity.noContent().build();
    }
}
