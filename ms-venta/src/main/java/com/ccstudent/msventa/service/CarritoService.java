package com.ccstudent.msventa.service;

import com.ccstudent.msventa.entity.CarritoItem;

import java.util.List;

public interface CarritoService {
    CarritoItem agregarItem(Integer userId, Integer libroId, Integer cantidad);
    void eliminarItem(Integer userId, Integer itemId);
    List<CarritoItem> listarItems(Integer userId);
}