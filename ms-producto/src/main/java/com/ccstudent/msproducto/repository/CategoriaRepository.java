package com.ccstudent.msproducto.repository;

import com.ccstudent.msproducto.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface CategoriaRepository extends JpaRepository<Categoria,Integer> {
}
