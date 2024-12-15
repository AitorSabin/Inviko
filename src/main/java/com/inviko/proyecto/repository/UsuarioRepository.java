package com.inviko.proyecto.repository;

import com.inviko.proyecto.model.Usuario;
import org.springframework.data.repository.CrudRepository;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
}
