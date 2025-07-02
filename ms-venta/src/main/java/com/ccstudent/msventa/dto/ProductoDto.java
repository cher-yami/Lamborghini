package com.ccstudent.msventa.dto;

import lombok.Data;

@Data
public class ProductoDto {
    private Integer id;
    private String titulo;
    private String color;
    private Integer stock;
    private Double precio;
}
