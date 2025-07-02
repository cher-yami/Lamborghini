package com.ccstudent.mscliente.controller;

import com.ccstudent.mscliente.entity.Cliente;
import com.ccstudent.mscliente.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cliente")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;
    @GetMapping
    public ResponseEntity<List<Cliente>> listar(){
        return ResponseEntity.ok(clienteService.listar());
    }
    @PostMapping
    public ResponseEntity<Cliente> guardar(@RequestBody Cliente cliente){
        return ResponseEntity.ok(clienteService.guardar(cliente));
    }
    @GetMapping("/{id}")
    public  ResponseEntity<Cliente> busacarPorId(@PathVariable(required = true) Integer id){
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> editar(@PathVariable(required = true) Integer id, @RequestBody Cliente cliente){
        cliente.setId(id);
        return  ResponseEntity.ok(clienteService.editar(cliente));
    }
    @DeleteMapping("/{id}")
    public String eliminar(@PathVariable(required = true) Integer id){
        clienteService.eliminar(id);
        return "Eliminacion completa";
    }
}
