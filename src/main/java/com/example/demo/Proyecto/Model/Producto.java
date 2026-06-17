package com.example.demo.Proyecto.Model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "productos")
@Getter
@Setter
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique =  true)
    @NotBlank
    @Size(min = 2, max = 100)
    private String nombre;

    @Column(nullable = false)
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal precio;

    @Column(nullable = false, length = 250)
    @NotBlank
    @Size(min = 2, max = 250)
    private String descripcion;

    @Convert(converter = StringListConverter.class)
    @Column(name = "imagen_url", columnDefinition = "TEXT")
    private List<String> imagenUrl = new ArrayList<>();

    @Column(nullable = false)
    private boolean enOferta = false;

    @Column
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal precioOferta;

    @Column(nullable = false)
    private boolean destacado = false;

    @NotNull
    @Column(nullable = false)
    private boolean activo = true;

    @JsonIgnoreProperties({"productos"})
    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Column(nullable = false)
    @NotBlank
    private String marca;

    @Column(nullable = false)
    private int stock;
}
