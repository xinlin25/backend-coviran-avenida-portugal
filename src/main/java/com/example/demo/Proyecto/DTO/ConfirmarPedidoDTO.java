package com.example.demo.Proyecto.DTO;

import com.example.demo.Proyecto.Enum.MetodoPago;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmarPedidoDTO {
    private MetodoPago metodoPago;
    private String especificacionesEntrega;
}