package com.ccstudent.mscliente.controller;

import com.ccstudent.mscliente.entity.Vendedor;
import com.ccstudent.mscliente.service.VendedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendedor")
public class VendedorController {
    @Autowired
    private VendedorService vendedorService;
    @GetMapping
    public List<Vendedor> listarvendedor(){
        return vendedorService.listar();
    }
    @PostMapping
    public Vendedor crearvendedor(@RequestBody Vendedor vendedor){
        return vendedorService.guardar(vendedor);
    }
    @DeleteMapping("/{id}")
    public String eliminarcategoria(@PathVariable Integer id){
        vendedorService.eliminar(id);
        return "se elimino correctamente";
    }
    @PostMapping("/{id}")
    public Vendedor editarcategoria(@RequestBody Vendedor vendedor, @PathVariable Integer id){
        return vendedorService.editar(vendedor, id);
    }
}
