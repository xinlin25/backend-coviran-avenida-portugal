package com.example.demo.Proyecto.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Proyecto.Enum.EstadoCarrito;
import com.example.demo.Proyecto.Model.Carrito;
import com.example.demo.Proyecto.Model.Usuario;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    Optional<Carrito> findByUsuarioAndEstado(Usuario usuario, EstadoCarrito estado);
}

