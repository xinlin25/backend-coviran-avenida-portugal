package com.example.demo.Proyecto.DTO;

import com.example.demo.Proyecto.Enum.Rol;

public record ActualizarUsuarioDTO(
    String nombreCompleto,
    String tlf,
    String direccion,
    Rol rol,
    boolean enabled
) {}