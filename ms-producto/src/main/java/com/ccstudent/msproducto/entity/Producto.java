package com.ccstudent.msproducto.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String titulo;
    private String color;
    private String descripcion;
    private Integer stock;
    private Double precio;
    private LocalDate anio;
    private String imagenUrl;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "provedores_id", nullable = false)
    private Provedores provedores;
}
