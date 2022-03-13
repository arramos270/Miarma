package com.example.Miarma.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    private String cuerpo;

    @ManyToOne
    private UserEntity creador;

    @Builder.Default
    private LocalDateTime fechaPublicacion = LocalDateTime.now();
}
