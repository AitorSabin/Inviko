package com.inviko.proyecto.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Entity(name = "Usuario")
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    @NotEmpty(message = "El Nombre del Usuario es Obligatorio")
    private String nombre;

    @Column(name = "apellido")
    @NotEmpty(message = "El Apellido del Usuario es Obligatorio")
    private String apellido;

    @Column(name = "email")
    @NotEmpty(message = "El Email del Usuario es Obligatorio")
    private String email;

    @Column(name = "telefono")

    private String telefono;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "usuario_grupo", joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "grupo_id", referencedColumnName = "id")
    )
    private List<Grupo> grupos;

    public List<Grupo> getGrupos() {
        return grupos;
    }

    public void setGrupos(List<Grupo> grupos) {
        this.grupos = grupos;
    }

    public void addGrupo(Grupo grupo) {
        if (grupo != null && !grupos.contains(grupo)) {
            grupos.add(grupo);
            grupo.addUsuario(this); // Asociar el usuario con el grupo
        }
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

}
