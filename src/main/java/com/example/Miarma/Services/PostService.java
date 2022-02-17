package com.example.Miarma.Services;

import com.example.Miarma.Models.Post;
import com.example.Miarma.Models.UserEntity;
import com.example.Miarma.Repositories.PostRepository;
import com.example.Miarma.Repositories.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class PostService extends BaseService<Post, UUID, PostRepository> {

    public List<Post> listarAllPostPublicos(){
        return repositorio.listaPostPublicos();
    }

    public List<Post> listarTodosPostsUsuarioX(UUID id){
        return repositorio.listaPostDeUsuario(id);
    }

    public List<Post> listarTodosPostsPublicosUsuarioX(UUID id){
        return repositorio.listaPostPublicosDeUsuario(id);
    }

}
