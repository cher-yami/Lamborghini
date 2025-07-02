package com.ccstudent.msproducto.repository;

import com.ccstudent.msproducto.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    List<Producto> findByCategoriaId(Integer categoriaId);
    List<Producto> findByAnio(LocalDate anio);
    List<Producto> findByAnioAfter(LocalDate anio);
}
