package com.ccstudent.mscliente.service.impl;

import com.ccstudent.mscliente.entity.Cliente;
import com.ccstudent.mscliente.repository.ClienteRepository;
import com.ccstudent.mscliente.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;
    @Override
    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente buscarPorId(Integer id) {
        return clienteRepository.findById(id).get();
    }

    @Override
    public Cliente editar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public void eliminar(Integer id) {
        clienteRepository.deleteById(id);
    }
}
