package com.example.demo.Proyecto.DTO;

public record ActualizarCategoriaDTO(
    String nombre,
    Long categoriaPadreId,
    Boolean activo
) {}