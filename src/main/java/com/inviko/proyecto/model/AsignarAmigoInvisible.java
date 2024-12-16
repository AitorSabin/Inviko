package com.inviko.proyecto.model;

public class AsignarAmigoInvisible {
    private Usuario usuario;
    private Usuario amigoInvisible;

    public AsignarAmigoInvisible(Usuario usuario, Usuario amigoInvisible) {
        this.usuario = usuario;
        this.amigoInvisible = amigoInvisible;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getAmigoInvisible() {
        return amigoInvisible;
    }

    public void setAmigoInvisible(Usuario amigoInvisible) {
        this.amigoInvisible = amigoInvisible;
    }
}

