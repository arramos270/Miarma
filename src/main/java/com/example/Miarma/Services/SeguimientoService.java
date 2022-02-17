package com.example.Miarma.Services;

import com.example.Miarma.Models.Seguimiento;
import com.example.Miarma.Models.UserEntity;
import com.example.Miarma.Repositories.SeguimientoRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class SeguimientoService {

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
}
