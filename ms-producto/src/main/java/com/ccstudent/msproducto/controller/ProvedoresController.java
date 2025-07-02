package com.ccstudent.msproducto.controller;

import com.ccstudent.msproducto.entity.Provedores;
import com.ccstudent.msproducto.service.ProvedoresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/provedores")
public class ProvedoresController {
    @Autowired
    private ProvedoresService provedoresService;
    @GetMapping
    public List<Provedores> listarprovedores(){
        return provedoresService.listar();
    }
    @PostMapping
    public Provedores crearprovedores(@RequestBody Provedores provedores){
        return provedoresService.guardar(provedores);
    }
    @DeleteMapping("/{id}")
    public String eliminarprovedores(@PathVariable Integer id){
        provedoresService.eliminar(id);
        return "se elimino correctamente";
    }
    @PostMapping("/{id}")
    public Provedores editarprovedotres(@RequestBody Provedores provedores, @PathVariable Integer id){
        return provedoresService.editar(provedores, id);
    }

}
