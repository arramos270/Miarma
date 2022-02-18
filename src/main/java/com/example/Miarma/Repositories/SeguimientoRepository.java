package com.example.Miarma.Repositories;

import com.example.Miarma.Models.Seguimiento;
import com.example.Miarma.Models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SeguimientoRepository extends JpaRepository<Seguimiento, UUID> {
    //No ponemos cuando el estado de la petición de seguimiento sea "DENEGADA" porque en ese caso se borrará ese seguimiento

    //LE HE MANDADO SEGUIMIENTO A
    @Query("select s from Seguimiento s where s.seguidor = :id and s.estado = EN_ESPERA")
    public List<Seguimiento> getMyFollowsWaiting(@Param("id") UUID id);

    //ME HAN MANDADO SEGUIMIENTO
    @Query("select s from Seguimiento s where s.seguido = :id and s.estado = EN_ESPERA")
    public List<Seguimiento> getMyFollowersWaiting(@Param("id") UUID id);

    UserEntity findBySeguidor(UserEntity seguidor);

    UserEntity findBySeguido(UserEntity seguido);
}
