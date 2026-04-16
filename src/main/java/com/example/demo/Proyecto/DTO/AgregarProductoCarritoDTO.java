package com.example.demo.Proyecto.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AgregarProductoCarritoDTO {
    @NotNull
    private Long productoId;

    @Min(1)
    private int cantidad;
}