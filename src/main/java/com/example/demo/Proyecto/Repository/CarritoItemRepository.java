package com.example.demo.Proyecto.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Proyecto.Model.Carrito;
import com.example.demo.Proyecto.Model.CarritoItem;
import com.example.demo.Proyecto.Model.Producto;

public interface CarritoItemRepository extends JpaRepository<CarritoItem, Long> {
    Optional<CarritoItem> findByCarritoAndProducto(Carrito carrito, Producto producto);
}
