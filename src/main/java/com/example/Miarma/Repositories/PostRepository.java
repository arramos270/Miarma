package com.example.Miarma.Repositories;

import com.example.Miarma.Models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {

    @Query("select p from Post p where p.publica = true")
    List<Post> listaPostPublicos();

    @Query("select p from Post p where p.idCreador = :idCreador")
    List<Post> listaPostDeUsuario(@Param("idCreador") UUID idCreador);

    @Query("select p from Post p where p.idCreador = :idCreador and p.publica = true")
    List<Post> listaPostPublicosDeUsuario(@Param("idCreador") UUID idCreador);

}
