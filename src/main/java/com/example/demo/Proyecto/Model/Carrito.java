package com.example.demo.Proyecto.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.Proyecto.Enum.EstadoCarrito;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "carritos")
@Getter
@Setter
public class Carrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id")
    @JsonIgnore
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCarrito estado;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @OneToMany(
        mappedBy = "carrito",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<CarritoItem> items = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        this.estado = EstadoCarrito.ACTIVO;
    }
}