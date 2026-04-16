package com.example.demo.Proyecto.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.Proyecto.Model.Categoria;
import com.example.demo.Proyecto.Repository.CategoriaRepository;

import jakarta.transaction.Transactional;

@Service
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    public CategoriaService (CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    //Inserta o Actualiza la categoria en la bd
    public Categoria guardarCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    //Optional por si no existe
    public Optional<Categoria> buscarPorNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre);
    }

    public List<Categoria> buscarPorNombreParcial(String nombre) {
        return categoriaRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public boolean existePorNombre(String nombre) {
        return categoriaRepository.existsByNombre(nombre);
    }

    @Transactional
    public void borrarPorNombre(String nombre) {
        categoriaRepository.deleteByNombre(nombre);
    }

    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }
}
