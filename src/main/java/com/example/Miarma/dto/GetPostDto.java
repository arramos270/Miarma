package com.example.Miarma.dto;

import lombok.*;

import javax.persistence.Lob;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class GetPostDto {

    private UUID id;

    private String title;

    @Lob
    private String description;

    private String archivo;

    private String escalado;

    private String nombreCreador;

    private String avatar;

    private boolean isPublic;

    private LocalDateTime fechaPublicacion;
}
