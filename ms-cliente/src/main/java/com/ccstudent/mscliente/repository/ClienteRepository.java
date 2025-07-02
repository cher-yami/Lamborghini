package com.ccstudent.mscliente.repository;

import com.ccstudent.mscliente.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente,Integer> {
}
