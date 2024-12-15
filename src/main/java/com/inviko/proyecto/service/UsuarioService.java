package com.inviko.proyecto.service;

import com.inviko.proyecto.model.Usuario;
import com.inviko.proyecto.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> getAllUsuarios(){
        return (List<Usuario>) usuarioRepository.findAll();
    }

    public Optional<Usuario> findUsuarioById(Long id){
        return usuarioRepository.findById(id);
    }

    public Usuario saveUsuario(Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    public void deleteUsuario(Long id){
        usuarioRepository.deleteById(id);
    }
}
