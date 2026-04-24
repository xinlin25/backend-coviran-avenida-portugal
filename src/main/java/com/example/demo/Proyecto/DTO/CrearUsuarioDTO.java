package com.example.demo.Proyecto.DTO;

import com.example.demo.Proyecto.Enum.Rol;

public record CrearUsuarioDTO(
    String nombreCompleto,
    String correo,
    String password,
    String tlf,
    String direccion,
    Rol rol,
    boolean enabled
) {}