package com.ccstudent.msproducto.service.impl;

import com.ccstudent.msproducto.entity.Categoria;
import com.ccstudent.msproducto.repository.CategoriaRepository;
import com.ccstudent.msproducto.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaServiceImpl implements CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Override
    public List<Categoria> listar(){
        return categoriaRepository.findAll();
    }
    @Override
    public Categoria guardar(Categoria categoria){
        return categoriaRepository.save(categoria);
    }
    @Override
    public Categoria editar(Categoria categoria, Integer id) {
        if (categoriaRepository.existsById(id)) {
            categoria.setId(id);
            return categoriaRepository.save(categoria);

        }
        return null;
    }
    @Override
    public void eliminar(Integer id){
        categoriaRepository.deleteById(id);

    }
    @Override
    public Optional<Categoria> listarPorId(Integer id) {
        return categoriaRepository.findById(id);
    }
}
