package com.ccstudent.msproducto.entity;

import com.ccstudent.msproducto.models.Formato;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;
    private String nombre;
    private String clasificacion;
    @Enumerated(EnumType.STRING)
    private Formato formato;
    private String usoPrincipal;     // Ej: "Educación", "Gaming", "Ofimática", etc.
}