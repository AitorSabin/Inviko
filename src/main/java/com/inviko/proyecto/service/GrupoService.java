package com.inviko.proyecto.service;

import com.inviko.proyecto.model.Grupo;
import com.inviko.proyecto.repository.GrupoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GrupoService {

    private final GrupoRepository grupoRepository;


    public GrupoService(GrupoRepository grupoRepository) {
        this.grupoRepository = grupoRepository;
    }

    public List<Grupo> getAllGrupos() {
        return (List<Grupo>) grupoRepository.findAll();
    }

    public Optional<Grupo> findGrupoById(Long id) {
        return grupoRepository.findById(id);
    }

    public Grupo saveGrupo(Grupo grupo) {
        return grupoRepository.save(grupo);
    }

    public void deleteGrupo(Long id) {
        grupoRepository.deleteById(id);
    }
}
