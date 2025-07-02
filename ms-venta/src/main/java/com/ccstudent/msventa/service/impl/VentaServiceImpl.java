package com.ccstudent.msventa.service.impl;

import com.ccstudent.msventa.dto.ProductoDto;
import com.ccstudent.msventa.entity.CarritoItem;
import com.ccstudent.msventa.entity.Venta;
import com.ccstudent.msventa.entity.VentaDetalle;
import com.ccstudent.msventa.feign.AuthFeign;
import com.ccstudent.msventa.feign.ProductoFeign;
import com.ccstudent.msventa.repository.CarritoItemRepository;
import com.ccstudent.msventa.repository.VentaRepository;
import com.ccstudent.msventa.service.VentaService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class VentaServiceImpl implements VentaService {

    private static final double IGV = 0.18;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private CarritoItemRepository carritoItemRepository;

    @Autowired
    private AuthFeign authFeign;

    @Autowired
    private ProductoFeign productoFeign;

    @Override
    @Transactional
    public Venta realizarVenta(String token) {
        Integer userId = authFeign.getUserId(token).getBody();
        String userName = authFeign.getUserName(token).getBody();
        List<CarritoItem> items = carritoItemRepository.findByUserId(userId);

        if (items.isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        Venta venta = new Venta();
        venta.setUserId(userId);
        venta.setUserName(userName);
        venta.setFecha(new Date());

        double total = 0;

        for (CarritoItem item : items) {
            ResponseEntity<ProductoDto> response = productoFeign.listarProducto(item.getProductoId());
            if (response.getStatusCode().is2xxSuccessful()) {
                ProductoDto producto = response.getBody();
                if (producto.getStock() < item.getCantidad()) {
                    throw new RuntimeException("No hay suficiente stock para el producto: " + producto.getTitulo());
                }

                VentaDetalle detalle = new VentaDetalle();
                detalle.setVenta(venta);
                detalle.setProductoId(producto.getId());
                detalle.setCantidad(item.getCantidad());
                detalle.setPrecio(producto.getPrecio());
                detalle.setProducto(producto);  // Guardar producto en detalle
                venta.getDetalles().add(detalle);

                // Actualizar stock
                productoFeign.actualizarStock(producto.getId(), producto.getStock() - item.getCantidad());

                total += producto.getPrecio() * item.getCantidad();
            } else {
                throw new RuntimeException("No se pudo obtener información del producto con ID: " + item.getProductoId());
            }
        }

        double igv = total * IGV;
        double totalConIgv = total + igv;

        venta.setTotal(total);
        venta.setIgv(igv);
        venta.setTotalConIgv(totalConIgv);

        venta = ventaRepository.save(venta);
        carritoItemRepository.deleteAll(items);

        return venta;
    }

    @Override
    public List<Venta> listarVentas() {
        return ventaRepository.findAll();
    }

    @Override
    public Venta obtenerVentaPorId(Integer id) {
        return ventaRepository.findById(id).orElse(null);
    }

    @Override
    public Venta obtenerVentaConDetalles(Integer ventaId) {
        Venta venta = ventaRepository.findById(ventaId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        for (VentaDetalle detalle : venta.getDetalles()) {
            ResponseEntity<ProductoDto> response = productoFeign.listarProducto(detalle.getProductoId());
            if (response.getStatusCode().is2xxSuccessful()) {
                detalle.setProducto(response.getBody());
            } else {
                throw new RuntimeException("No se pudo obtener información del producto con ID: " + detalle.getProductoId());
            }
        }

        return venta;
    }
}
