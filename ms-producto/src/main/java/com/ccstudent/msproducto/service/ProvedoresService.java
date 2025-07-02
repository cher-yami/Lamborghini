package com.ccstudent.msproducto.service;

import com.ccstudent.msproducto.entity.Provedores;

import java.util.List;
import java.util.Optional;

public interface ProvedoresService {
    public List<Provedores> listar();
    public Provedores guardar(Provedores provedores);
    public Provedores editar(Provedores provedores, Integer id);
    public void eliminar(Integer id);
    public Optional<Provedores> listarPorId(Integer id);
}
