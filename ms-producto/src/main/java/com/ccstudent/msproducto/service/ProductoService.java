package com.ccstudent.msproducto.service;

import com.ccstudent.msproducto.entity.Producto;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductoService {
    List<Producto> listar();
    Producto guardar(String titulo, String color, String descripcion, Integer stock, Double precio, Integer categoriaId, Integer provedoresId, MultipartFile file, LocalDate anio);
    Producto editar(Integer id, String titulo, String color, String descripcion, Integer stock, Double precio, Integer categoriaId, Integer provedoresId, MultipartFile file, LocalDate anio);
    void eliminar(Integer id);
    Optional<Producto> listarPorId(Integer id);
    Producto actualizarStock(Integer id, Integer cantidad);
    List<Producto> listarPorCategoria(Integer categoriaId);
    List<Producto> listarPorAnio(LocalDate anio);
    List<Producto> listarDesdeAnio(LocalDate anio);
}
