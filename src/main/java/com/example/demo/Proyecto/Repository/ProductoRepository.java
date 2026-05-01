package com.example.demo.Proyecto.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.demo.Proyecto.Model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    Optional<Producto> findByNombre(String nombre);
    
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    List<Producto> findByCategoriaId(Long categoriaId);

    List<Producto> findByActivoTrue();

    boolean existsByNombre(String nombre);

    @Query("""
        SELECT p FROM Producto p
        WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :query, '%'))
        OR LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :query, '%'))
    """)
    List<Producto> buscar(@Param("query") String query);
}