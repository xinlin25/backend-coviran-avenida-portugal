package com.example.demo.Proyecto.Enum;

public enum Estado {
    PENDIENTE,
    REPARTO,
    COMPLETADO,
    CANCELADO;

    public boolean puedeTransicionarA(Estado nuevo) {
        return switch(this) {
            case PENDIENTE -> nuevo == REPARTO || nuevo == CANCELADO;
            case REPARTO -> nuevo == COMPLETADO;
            case COMPLETADO -> false;
            case CANCELADO -> false;
        };
    }
}