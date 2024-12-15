package com.inviko.proyecto.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Entity(name = "Grupo")
@Table(name = "grupo")
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_grupo")
    @NotEmpty(message = "El Nombre del Grupo es Obligatorio")
    private String nombreGrupo;

    @Column(name = "descripcion")
    @NotEmpty(message = "La descripcion del Grupo es Obligatoria")
    private String descripcion;

    @Column(name = "estado")
    @NotEmpty(message = "El Estado del Grupo es Obligatorio")
    private String estado;

    @ManyToMany(mappedBy = "grupos")
    private List<Usuario> usuarios;

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public void addUsuario(Usuario usuario) {
        if (usuario != null && !usuarios.contains(usuario)) {
            usuarios.add(usuario);
            usuario.addGrupo(this); // Asocia este grupo con el usuario
        }
    }

    public void eliminarUsuario(Usuario usuario) {
        usuarios.remove(usuario);
        usuario.getGrupos().remove(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreGrupo() {
        return nombreGrupo;
    }

    public void setNombreGrupo(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
