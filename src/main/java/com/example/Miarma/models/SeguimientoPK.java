package com.example.Miarma.models;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class SeguimientoPK implements Serializable {

    private UUID idSeguido;
    private UUID idSeguidor;

    public UUID getIdSeguido() {
        return idSeguido;
    }

    public void setIdSeguido(UUID idSeguido) {
        this.idSeguido = idSeguido;
    }

    public UUID getIdSeguidor() {
        return idSeguidor;
    }

    public void setIdSeguidor(UUID idSeguidor) {
        this.idSeguidor = idSeguidor;
    }

    public SeguimientoPK(UUID idSeguido, UUID idSeguidor) {
        this.idSeguido = idSeguido;
        this.idSeguidor = idSeguidor;
    }
}
