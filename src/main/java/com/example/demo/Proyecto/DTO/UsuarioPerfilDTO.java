package com.example.demo.Proyecto.DTO;

import com.example.demo.Proyecto.Enum.Rol;

public record UsuarioPerfilDTO(
    Long id,
    String nombreCompleto,
    String correo,
    String tlf,
    String direccion,
    Rol rol
) {}
