package com.ccstudent.msventa.service.impl;

import com.ccstudent.msventa.entity.CarritoItem;
import com.ccstudent.msventa.repository.CarritoItemRepository;
import com.ccstudent.msventa.service.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarritoServiceImpl implements CarritoService {
    @Autowired
    private CarritoItemRepository carritoItemRepository;

    @Override
    public CarritoItem agregarItem(Integer userId, Integer productoId, Integer cantidad) {
        CarritoItem item = new CarritoItem();
        item.setUserId(userId);
        item.setProductoId(productoId);
        item.setCantidad(cantidad);
        return carritoItemRepository.save(item);
    }

    @Override
    public void eliminarItem(Integer userId, Integer itemId) {
        CarritoItem item = carritoItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item no encontrado"));
        if (!item.getUserId().equals(userId)) {
            throw new RuntimeException("Acceso denegado");
        }
        carritoItemRepository.delete(item);
    }

    @Override
    public List<CarritoItem> listarItems(Integer userId) {
        return carritoItemRepository.findByUserId(userId);
    }
}
