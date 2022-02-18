package com.example.Miarma.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name="seguimientos")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seguimiento {

    @EmbeddedId
    private SeguimientoPK id;

    @MapsId("idSeguidor")
    @ManyToOne
    UserEntity seguidor; //Este sigue al otro

    @MapsId("idSeguido")
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
