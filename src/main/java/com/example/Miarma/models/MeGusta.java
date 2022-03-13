package com.example.Miarma.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name="megusta")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MeGusta {

    @EmbeddedId
    private MeGustaPK id;

    @ManyToOne
    UserEntity propietarioDelMeGusta;

    @ManyToOne
    Post postGustado;

    public UserEntity getPropietarioDelMeGusta() {
        return propietarioDelMeGusta;
    }

    public void setPropietarioDelMeGusta(UserEntity propietarioDelMeGusta) {
        this.propietarioDelMeGusta = propietarioDelMeGusta;
    }

    public Post getPostGustado() {
        return postGustado;
    }

    public void setPostGustado(Post postGustado) {
        this.postGustado = postGustado;
    }
}