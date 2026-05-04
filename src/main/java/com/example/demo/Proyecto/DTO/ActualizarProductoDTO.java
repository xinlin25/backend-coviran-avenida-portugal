package com.example.demo.Proyecto.DTO;
import java.math.BigDecimal;

public record ActualizarProductoDTO(
    String nombre,
    String descripcion,
    BigDecimal precio,
    String marca,
    Integer stock,
    Long categoriaId,
    Boolean activo
) {}