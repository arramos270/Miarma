package com.example.Miarma.Models;

import javax.persistence.ManyToOne;

public class Seguimiento {

    @ManyToOne
    UserEntity seguidor; //Este sigue al otro

    @ManyToOne
    UserEntity seguido; //Este es el seguido

    EstadoPeticion estado = EstadoPeticion.EN_ESPERA;

    public UserEntity getSeguidor() {
        return seguidor;
    }

    public void setSeguidor(UserEntity seguidor) {
        this.seguidor = seguidor;
    }

    public UserEntity getSeguido() {
        return seguido;
    }

    public void setSeguido(UserEntity seguido) {
        this.seguido = seguido;
    }

    public EstadoPeticion getEstado() {
        return estado;
    }

    public void setEstado(EstadoPeticion estado) {
        this.estado = estado;
    }
}
