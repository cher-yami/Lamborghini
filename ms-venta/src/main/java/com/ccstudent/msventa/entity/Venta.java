package com.ccstudent.msventa.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    private Integer userId;
    private double total;
    private double igv;
    private double totalConIgv;
    private String userName;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<VentaDetalle> detalles = new ArrayList<>();
}