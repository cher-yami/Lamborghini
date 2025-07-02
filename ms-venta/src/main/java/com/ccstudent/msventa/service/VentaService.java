package com.ccstudent.msventa.service;

import com.ccstudent.msventa.entity.Venta;

import java.util.List;

public interface VentaService {
    Venta realizarVenta(String token);
    List<Venta> listarVentas();
    Venta obtenerVentaPorId(Integer id);
    Venta obtenerVentaConDetalles(Integer ventaId);
}