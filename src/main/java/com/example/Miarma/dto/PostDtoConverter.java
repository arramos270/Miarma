package com.example.Miarma.dto;

import com.example.Miarma.models.Post;
import org.springframework.stereotype.Component;

@Component
public class PostDtoConverter {

    public GetPostDto convertPostToGetPostDto(Post post) {
        return GetPostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .description(post.getDescription())
                .archivo(post.getArchivo())
                .escalado(post.getEscalado())
                .nombreCreador(post.getPropietario().getUsername())
                .avatar(post.getPropietario().getAvatar())
                .fechaPublicacion(post.getFechaPublicacion())
                .isPublic(post.isPublica())
                .build();
    }

    public Post convertCreatePostDtoToPost(CreatePostDto postDto, Post post, String archivo, String escalado) {
        return Post.builder()
                .id(post.getId())
                .title(postDto.getTitle())
                .description(postDto.getDescription())
                .fechaPublicacion(post.getFechaPublicacion())
                .publica(postDto.isPublic())
                .archivo(archivo)
                .escalado(escalado)
                .propietario(post.getPropietario())
                .build();
    }
}
