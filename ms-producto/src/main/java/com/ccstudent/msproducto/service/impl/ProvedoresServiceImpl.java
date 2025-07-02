package com.ccstudent.msproducto.service.impl;

import com.ccstudent.msproducto.entity.Provedores;
import com.ccstudent.msproducto.repository.ProvedoresRepository;
import com.ccstudent.msproducto.service.ProvedoresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProvedoresServiceImpl implements ProvedoresService {
    @Autowired
    private ProvedoresRepository provedoresRepository;
    @Override
    public List<Provedores> listar(){
        return provedoresRepository.findAll();
    }
    @Override
    public Provedores guardar(Provedores provedores){
        return provedoresRepository.save(provedores);
    }
    @Override
    public Provedores editar(Provedores provedores, Integer id) {
        if (provedoresRepository.existsById(id)) {
            provedores.setId(id);
            return provedoresRepository.save(provedores);

        }
        return null;
    }
    @Override
    public void eliminar(Integer id){
        provedoresRepository.deleteById(id);

    }
    @Override
    public Optional<Provedores> listarPorId(Integer id) {
        return provedoresRepository.findById(id);
    }
}
