package com.example.Miarma.repositories;

import com.example.Miarma.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserEntityRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findFirstByUsername(String username);

    boolean existsByUsername(String nombre);



}

