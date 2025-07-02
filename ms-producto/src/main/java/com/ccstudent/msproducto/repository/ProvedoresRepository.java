package com.ccstudent.msproducto.repository;

import com.ccstudent.msproducto.entity.Provedores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvedoresRepository extends JpaRepository<Provedores,Integer> {

}
