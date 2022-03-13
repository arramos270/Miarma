package com.example.Miarma.dto;

import lombok.*;

import javax.persistence.Lob;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class CreatePostDto {

    private String title;

    @Lob
    private String description;

    private boolean isPublic;
}
