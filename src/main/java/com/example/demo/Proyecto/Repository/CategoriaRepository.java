package com.example.demo.Proyecto.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.Proyecto.Model.Categoria;

import java.util.List;
import java.util.Optional;


@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    //Busca categorias por nombre exacto
    Optional<Categoria> findByNombre(String nombre);

    //Busca todas las categorias cuyo nombre contenga el texto
    List<Categoria> findByNombreContainingIgnoreCase(String nombre);

    boolean existsByNombre(String nombre);

    void deleteByNombre(String nombre);
}