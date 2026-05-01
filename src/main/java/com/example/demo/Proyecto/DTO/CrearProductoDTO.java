package com.example.demo.Proyecto.DTO;

import java.math.BigDecimal;

public record CrearProductoDTO(
    String nombre,
    BigDecimal precio,
    String descripcion,
    String marca,
    int stock,
    Long categoriaId
) {}