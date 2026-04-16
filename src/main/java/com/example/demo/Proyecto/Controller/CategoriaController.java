package com.example.demo.Proyecto.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Proyecto.Model.Categoria;
import com.example.demo.Proyecto.Service.CategoriaService;



@RestController
@RequestMapping("/categorias")
public class CategoriaController {
    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    public ResponseEntity<Categoria> crearCategoria(@RequestBody Categoria categoria) {
        if (categoriaService.existePorNombre(categoria.getNombre())) 
            return ResponseEntity.badRequest().build();

        Categoria c = categoriaService.guardarCategoria(categoria);
        return ResponseEntity.ok(c);
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> listarCategorias() {
        List<Categoria> categorias = categoriaService.listarTodas();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/nombre/")
    public ResponseEntity<Categoria> buscarPorNombre(@RequestParam String nombre) {
        Optional<Categoria> categoria = categoriaService.buscarPorNombre(nombre);
        //Si existe 200 OK si no 404 Not Found
        return categoria.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar/")
    public ResponseEntity<List<Categoria>> buscarPorNombreParcial(@RequestParam String nombre) {
        List<Categoria> categorias = categoriaService.buscarPorNombreParcial(nombre);
        return ResponseEntity.ok(categorias);
    }
    
    @DeleteMapping("/nombre/{nombre}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> borrarPorNombre(@PathVariable String nombre) {
        if (!categoriaService.existePorNombre(nombre)) 
            return ResponseEntity.notFound().build();

        categoriaService.borrarPorNombre(nombre);
        return ResponseEntity.noContent().build();
    }
}