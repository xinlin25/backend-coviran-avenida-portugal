package com.example.demo.Proyecto.Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.Proyecto.Enum.Estado;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pedidos")
@Getter
@Setter
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonIgnore
    private Usuario cliente;

    @ManyToOne
    @JoinColumn(name = "empleado_id")
    @JsonIgnore
    private Usuario empleado;

    @Column(nullable = false)
    @NotNull
    @JsonIgnore 
    private double total;

    @Column(nullable = false)
    @NotNull
    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private Estado estado;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<DetallePedido> detalles = new ArrayList<>();
}