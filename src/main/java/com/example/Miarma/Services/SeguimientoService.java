package com.example.Miarma.Services;

import com.example.Miarma.Models.Seguimiento;
import com.example.Miarma.Models.UserEntity;
import com.example.Miarma.Repositories.SeguimientoRepository;
import com.example.Miarma.Repositories.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class SeguimientoService  extends BaseService<Seguimiento, UUID, SeguimientoRepository> {

    private final SeguimientoRepository seguimientoRepository;
    private List<UserEntity> listaUsuarios;

    public List<UserEntity> yoSigoAEstos(List<Seguimiento> listaSeguimiento){
        listaSeguimiento.forEach(i ->
                listaUsuarios.add(i.getSeguido())
        );
        return listaUsuarios;
    }

    public List<UserEntity> estosMeSiguenAMi(List<Seguimiento> listaSeguimiento){
        listaSeguimiento.forEach(i ->
                listaUsuarios.add(i.getSeguidor())
        );
        return listaUsuarios;
    }

    public List<Seguimiento> miListaDeSolicitudesEnEspera(UUID userId){
        return seguimientoRepository.getMyFollowersWaiting(userId);
    }
}
