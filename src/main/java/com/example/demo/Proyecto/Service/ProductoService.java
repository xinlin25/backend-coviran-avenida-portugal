package com.example.demo.Proyecto.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.Proyecto.Model.Categoria;
import com.example.demo.Proyecto.Model.Producto;
import com.example.demo.Proyecto.Repository.CategoriaRepository;
import com.example.demo.Proyecto.Repository.ProductoRepository;

@Service
public class ProductoService {
    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    public Optional<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombre(nombre);
    }

    public List<Producto> buscarPorNombreParcial(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Producto> buscarPorCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId);
    }

    public List<Producto> listarActivos() {
        return productoRepository.findByActivoTrue();
    }

    public boolean existePorNombre(String nombre) {
        return productoRepository.existsByNombre(nombre);
    }

    public void borrarPorId(Long id) {
        productoRepository.deleteById(id);
    }

    public Categoria obtenerCategoria(Long categoriaId) {
    return categoriaRepository.findById(categoriaId)
        .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
}
}
