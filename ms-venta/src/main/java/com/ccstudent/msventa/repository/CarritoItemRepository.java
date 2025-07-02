package com.ccstudent.msventa.repository;

import com.ccstudent.msventa.entity.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarritoItemRepository extends JpaRepository<CarritoItem, Integer> {
    List<CarritoItem> findByUserId(Integer userId);
}