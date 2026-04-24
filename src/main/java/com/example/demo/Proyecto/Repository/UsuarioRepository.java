package com.example.demo.Proyecto.Repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Proyecto.Model.Usuario;
import com.example.demo.Proyecto.Enum.Rol;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Método derivado (seguro por diseño)
    Optional<Usuario> findByCorreo(String correo);

    boolean existsByCorreo(String correo);

    Optional<Usuario> findByTlf(String tlf);

    List<Usuario> findByRol(Rol rol);

    //Consulta JPQL parametrizada (ejemplo explícito contra SQL Injection)
    @Query("SELECT u FROM Usuario u WHERE u.correo = :correo")
    Optional<Usuario> buscarPorCorreoSeguro(@Param("correo") String correo);

    //Busqueda personalizada con LIKE
    @Query("""
        SELECT u FROM Usuario u 
        WHERE LOWER(u.nombreCompleto) LIKE LOWER(CONCAT('%', :query, '%'))
        OR LOWER(u.correo) LIKE LOWER(CONCAT('%', :query, '%'))
        OR u.tlf LIKE CONCAT('%', :query, '%')
    """)
    List<Usuario> buscar(@Param("query") String query);
}