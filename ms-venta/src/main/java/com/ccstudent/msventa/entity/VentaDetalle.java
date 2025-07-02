package com.ccstudent.msventa.entity;

import com.ccstudent.msventa.dto.ProductoDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class VentaDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer productoId;
    private Integer cantidad;
    private Double precio;

    @ManyToOne
    @JoinColumn(name = "venta_id")
    @JsonBackReference
    private Venta venta;

    @Transient
    private ProductoDto producto;
}
