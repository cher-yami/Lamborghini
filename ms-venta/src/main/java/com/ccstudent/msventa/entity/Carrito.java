package com.ccstudent.msventa.entity;


import com.ccstudent.msventa.dto.ProductoDto;
import jakarta.persistence.*;

@Entity
public class Carrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "venta_id")
    private Venta venta;

    @Transient
    private ProductoDto producto;

    private Integer cantidad;
}
