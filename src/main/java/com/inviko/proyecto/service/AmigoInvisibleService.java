package com.inviko.proyecto.service;

import com.inviko.proyecto.model.AsignarAmigoInvisible;
import com.inviko.proyecto.model.Grupo;
import com.inviko.proyecto.model.Usuario;
import com.inviko.proyecto.repository.GrupoRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AmigoInvisibleService {

    private final GrupoRepository grupoRepository;

    public AmigoInvisibleService(GrupoRepository grupoRepository) {
        this.grupoRepository = grupoRepository;
    }

    public List<AsignarAmigoInvisible> jugarAmigoInvisible(Long grupoId) {

        Grupo grupo = grupoRepository.findById(grupoId).orElseThrow(() -> new RuntimeException("Grupo no encontrado"));
        List<Usuario> usuariosDelGrupo = grupo.getUsuarios();
        if (usuariosDelGrupo.size() <= 1) {
            throw new RuntimeException("No hay suficientes usuarios en el grupo para jugar al Amigo Invisible.");
        }
        List<AsignarAmigoInvisible> asignaciones;
        boolean esValido;

        do {
            Collections.shuffle(usuariosDelGrupo);
            asignaciones = new ArrayList<>();
            esValido = true;

            for (int i = 0; i < usuariosDelGrupo.size(); i++) {
                Usuario usuario = usuariosDelGrupo.get(i);
                Usuario amigoInvisible = usuariosDelGrupo.get((i + 1) % usuariosDelGrupo.size());

                if (usuario.equals(amigoInvisible)) {
                    esValido = false;
                    break;
                }

                asignaciones.add(new AsignarAmigoInvisible(usuario, amigoInvisible));
            }
        } while (!esValido);

        return asignaciones;
    }
}
