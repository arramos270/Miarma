package com.example.Miarma.repositories;

import com.example.Miarma.models.MeGusta;
import com.example.Miarma.models.Post;
import com.example.Miarma.models.Seguimiento;
import com.example.Miarma.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface MeGustaRepository extends JpaRepository<MeGusta, UUID> {

    //LE HE MANDADO SEGUIMIENTO A
    @Query("select g from MeGusta g where g.propietario = :id")
    public List<Seguimiento> getLosPostsQueMeGustan(@Param("id") UUID id);

    MeGusta findByPost(Post post);
}
