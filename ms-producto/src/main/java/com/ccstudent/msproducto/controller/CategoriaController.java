package com.ccstudent.msproducto.controller;

import com.ccstudent.msproducto.entity.Categoria;
import com.ccstudent.msproducto.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {
    @Autowired
    private CategoriaService categoriaService;
    @GetMapping
    public List<Categoria> listarcategorias(){
        return categoriaService.listar();
    }
    @PostMapping
    public Categoria crearcategoria(@RequestBody Categoria categoria){
        return categoriaService.guardar(categoria);
    }
    @DeleteMapping("/{id}")
    public String eliminarcategoria(@PathVariable Integer id){
        categoriaService.eliminar(id);
        return "se elimino correctamente";
    }
    @PostMapping("/{id}")
    public Categoria editarcategoria(@RequestBody Categoria categoria, @PathVariable Integer id){
        return categoriaService.editar(categoria, id);
    }

}