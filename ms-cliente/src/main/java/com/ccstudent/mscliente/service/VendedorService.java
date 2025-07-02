package com.ccstudent.mscliente.service;

import com.ccstudent.mscliente.entity.Vendedor;

import java.util.List;

public interface VendedorService {
    public List<Vendedor> listar();
    public Vendedor guardar(Vendedor vendedor);
    public Vendedor editar(Vendedor vendedor, Integer id);
    public void eliminar(Integer id);
}
