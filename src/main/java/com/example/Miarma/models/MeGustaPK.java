package com.example.Miarma.models;

import java.io.Serializable;
import java.util.UUID;

public class MeGustaPK implements Serializable {

    private UUID idPropietario;
    private UUID idPost;

    public MeGustaPK(UUID idPropietario, UUID idPost) {
        this.idPropietario = idPropietario;
        this.idPost = idPost;
    }

    public UUID getIdPropietario() {
        return idPropietario;
    }

    public void setIdPropietario(UUID idPropietario) {
        this.idPropietario = idPropietario;
    }

    public UUID getIdPost() {
        return idPost;
    }

    public void setIdPost(UUID idPost) {
        this.idPost = idPost;
    }
}
