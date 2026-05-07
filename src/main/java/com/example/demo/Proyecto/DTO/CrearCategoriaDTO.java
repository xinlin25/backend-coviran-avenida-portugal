package com.example.demo.Proyecto.DTO;

public record CrearCategoriaDTO(
    String nombre,
    Long categoriaPadreId,
    Boolean activo
) {}